package com.hogan.common.mailhelper;

import org.thymeleaf.context.Context;

/**
 * ClassName:MailSendService
 * Description:邮件发送接口
 * User:dada
 * Date:2018/11/21 17:40
 */
public interface MailSendService {

    /**
     * 发送html格式的邮件
     */
    public Boolean sendHtmlMail(MailBean mailBean, Context context, String template);

    /**
     * 发送exchange协议的邮件
     */
    public Boolean sendExchangeMail(MailBean mailBean, Context context, String template);
}
