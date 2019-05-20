package com.hogan.common.mailhelper;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ClassName:MailSendServiceImpl
 * Description:邮件发送实现类（负责发送邮件）
 * User:hogan.li
 * Date:2018/11/21 17:41
 */
@Service
public class MailSendServiceImpl implements MailSendService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private Environment env;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public Boolean sendHtmlMail(MailBean mailBean, Context context, String template) {

        Boolean result = false;

        // 动态设置发件人(使用公共账号发邮件给客户可以不设)
//        ((JavaMailSenderImpl) javaMailSender).setUsername(from);
//        ((JavaMailSenderImpl) javaMailSender).setPassword(password);

        /**
         *  重要参数
         *  阻止添加附件调用setFileName方法的时候,附件中文名称Base64加密超过60个字符从而导致中文附件名称分段加密；
         *  附件中文名称分段加密后将会导致一些邮件客户端与网页端附件显示异常或者附件中文名称乱码；
         */
        System.setProperty("mail.mime.splitlongparameters", "false");

        MimeMessage mm = javaMailSender.createMimeMessage();
        try {
            // 构建发送内容
            MimeMessageHelper helper = new MimeMessageHelper(mm, true);     // true表示需要创建一个multipart message
            // 邮件内容部件，包括文件和正文
            Multipart mp = new MimeMultipart();
            // 设置发件人
            helper.setFrom(from);

            // 设置单个收件人（如有有多个收件人，会被多个收件人覆盖，单个收件人不保留）
            if(StringUtils.isNotBlank(mailBean.getToUserAccount())){
                helper.setTo(mailBean.getToUserAccount());
            }

            // 设置多个收件人
            List<String> toList = mailBean.getToUsersAccount();
            if (!CollectionUtils.isEmpty(toList)) {
                String[] toUsersAccount = new String[toList.size()];
                toList.toArray(toUsersAccount);
                helper.setTo(toUsersAccount);
            }

            // 设置单个抄送人
            if(StringUtils.isNotBlank(mailBean.getCcUserAccount())){
                helper.setCc(mailBean.getCcUserAccount());
            }

            // 设置多个抄送人
            List<String> ccList = mailBean.getCcUsersAccount();
            if (!CollectionUtils.isEmpty(ccList)) {
                String[] ccUsersAccount = new String[ccList.size()];
                ccList.toArray(ccUsersAccount);
                helper.setCc(ccUsersAccount);
            }

            // 设置邮件主题
            helper.setSubject(mailBean.getSubject());

            // 设置附件
            String multipartFilesPath = mailBean.getMultipartFilesPath();
            if(StringUtils.isNotBlank(multipartFilesPath)) {
                String filesPathArr[] = multipartFilesPath.split(",");
                for(String filePath : filesPathArr) {
                    if(StringUtils.isNotBlank(filePath)) {
                        File file = new File(filePath);
                        if(!file.exists()) {
                            continue;
                        }
                        // 把附件添加为部件的一部分
                        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(file);
                        attachmentBodyPart.setDataHandler(new DataHandler(source));
                        attachmentBodyPart.attachFile(file);
                        attachmentBodyPart.setFileName(MimeUtility.encodeText(file.getName()));
                        mp.addBodyPart(attachmentBodyPart);
                    }
                }
            }

            // 设置html内容
            String html = mailContentBuilder.buildHtml(context, template);
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(html, "text/html;charset=UTF-8");
            mp.addBodyPart(contentPart);

            // 设置发送的内容
            mm.setContent(mp);

            // 发送邮件
            javaMailSender.send(mm);

            // 保存已发送邮件
//            saveIntoSent(mm, mailBean);

            result = true;

        } catch (Exception e) {
            throw new RuntimeException("发送邮件失败：" + e);
        }

        return result;
    }

    /**
     * 发送exchange协议邮件
     */
    @Override
    public Boolean sendExchangeMail(MailBean mailBean, Context context, String template) {

        Boolean result = false;

        // The Exchange Server Version.
        ExchangeService exchangeService = new ExchangeService(ExchangeVersion.Exchange2007_SP1);

        // Credentials to sign in the MS Exchange Server.
        ExchangeCredentials exchangeCredentials = new WebCredentials(mailBean.getSendAccount(), mailBean.getSendPassword(), env.getProperty("exchange.domain"));
        exchangeService.setCredentials(exchangeCredentials);

        try {

            exchangeService.setUrl(new URI(env.getProperty("exchange.server")));

            EmailMessage em = new EmailMessage(exchangeService);

            // 设置发件人
            em.setFrom(new EmailAddress(mailBean.getSendAccount()));


            // 设置单个收件人（如有有多个收件人，会被多个收件人覆盖，单个收件人不保留）
            if(StringUtils.isNotBlank(mailBean.getToUserAccount())){
                em.getToRecipients().add(mailBean.getToUserAccount());
            }

            // 设置多个收件人
            List<String> toList = mailBean.getToUsersAccount();
            if (!CollectionUtils.isEmpty(toList)) {
                em.getToRecipients().addEmailRange(parseToEmailIterator(toList));
            }

            // 设置单个抄送人
            if(StringUtils.isNotBlank(mailBean.getCcUserAccount())){
                em.getCcRecipients().add(mailBean.getCcUserAccount());
            }

            // 设置多个抄送人
            List<String> ccList = mailBean.getCcUsersAccount();
            if (!CollectionUtils.isEmpty(ccList)) {
                em.getCcRecipients().addEmailRange(parseToEmailIterator(ccList));
            }

            // 设置邮件主题
            em.setSubject(mailBean.getSubject());

            // 设置附件
            String multipartFilesPath = mailBean.getMultipartFilesPath();
            if(StringUtils.isNotBlank(multipartFilesPath)) {
                String filesPathArr[] = multipartFilesPath.split(",");
                for(String filePath : filesPathArr) {
                    if(StringUtils.isNotBlank(filePath)) {
                        File file = new File(filePath);
                        if(!file.exists()) {
                            continue;
                        }
                        em.getAttachments().addFileAttachment(filePath);
                    }
                }
            }
            // 设置正文
            String html = mailContentBuilder.buildHtml(context, template);
            em.setBody(new MessageBody(html));
            // 发送
            em.send();
            result = true;
        } catch (Exception e) {
            throw new RuntimeException("发送邮件失败：" + e);
        }

        return result;
    }

    /**
     * 将邮件地址转换为exchange发送需要的邮件对象集合
     * @param toList
     * @return
     */
    private Iterator<EmailAddress> parseToEmailIterator(List<String> toList) {
        List<EmailAddress> emailAddresses = new ArrayList<>();
        for(String address : toList) {
            emailAddresses.add(new EmailAddress(address));
        }
        return emailAddresses.iterator();
    }

    /**
     * 邮件发送成功后保存到已发送邮件目录，可以同步到邮件客户端
     * @param msg
     * @param mailBean
     */
    private void saveIntoSent(MimeMessage msg, MailBean mailBean) {
        try {
            // 获取当前邮件会话
            Session imapSession = ((JavaMailSenderImpl) javaMailSender).getSession();
            // 连接当前账号的存储区
            IMAPStore store = (IMAPStore) imapSession.getStore("imap");
            store.connect(env.getProperty("spring.mail.host"), mailBean.getSendAccount(), mailBean.getSendPassword());
            // 从存储区获取已发送邮件的文件夹
            IMAPFolder folder = (IMAPFolder) store.getFolder("已发送邮件");
            // 如果文件夹不存在则创建它
            if (!folder.exists()) {
                folder.create(Folder.HOLDS_MESSAGES);
            }
            // 开启文件夹的读写权限
            folder.open(Folder.READ_WRITE);
            // 把邮件添加到已发送邮件的文件夹里面
            folder.appendMessages(new Message[]{msg});
            // 释放文件夹
            folder.expunge();
        } catch (Exception e) {
            throw new RuntimeException("保存已发送邮件失败：" + e);
        }

    }

}
