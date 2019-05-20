package com.hogan.common.mailhelper;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName:MailBean
 * Description:邮件实体
 * User:hogan.li
 * Date:2018/10/24.
 */
public class MailBean implements Serializable {

    private String subject;                 // 邮件主题

    private String content;                 // 邮件正文

    private String sendUserName;            // 发件人姓名

    private String sendAccount;             // 发件人账号

    private String sendPassword;            // 发送密码

    private String toUserAccount;           // 收件人账户（单个）

    private String toUserName;              // 收件人姓名（单个）

    private List<String> toUsersAccount;    // 收件人账户（一个或多个）

    private List<String> toUsersName;       // 收件人姓名（一个或多个）

//    private String ccUsers;                 // 抄送人，支持多个英文","分割

    private String ccUserAccount;           // 抄送人账户（单个）

    private List<String> ccUsersAccount;    // 抄送人账户（多个）

    private String multipartFilesPath;      // 附件地址，支持多个英文","分割


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendAccount() {
        return sendAccount;
    }

    public void setSendAccount(String sendAccount) {
        this.sendAccount = sendAccount;
    }

    public String getSendPassword() {
        return sendPassword;
    }

    public void setSendPassword(String sendPassword) {
        this.sendPassword = sendPassword;
    }

//    public String getToUser() {
//        return toUser;
//    }
//
//    public void setToUser(String toUser) {
//        this.toUser = toUser;
//    }

    public String getToUserAccount() {
        return toUserAccount;
    }

    public void setToUserAccount(String toUserAccount) {
        this.toUserAccount = toUserAccount;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

//    public String[] getToUsers() {
//        return toUsers;
//    }
//
//    public void setToUsers(String[] toUsers) {
//        this.toUsers = toUsers;
//    }

    public List<String> getToUsersAccount() {
        return toUsersAccount;
    }

    public void setToUsersAccount(List<String> toUsersAccount) {
        this.toUsersAccount = toUsersAccount;
    }

    public List<String> getToUsersName() {
        return toUsersName;
    }

    public void setToUsersName(List<String> toUsersName) {
        this.toUsersName = toUsersName;
    }

//    public String getCcUsers() {
//        return ccUsers;
//    }
//
//    public void setCcUsers(String ccUsers) {
//        this.ccUsers = ccUsers;
//    }

    public String getCcUserAccount() {
        return ccUserAccount;
    }

    public void setCcUserAccount(String ccUserAccount) {
        this.ccUserAccount = ccUserAccount;
    }

    public List<String> getCcUsersAccount() {
        return ccUsersAccount;
    }

    public void setCcUsersAccount(List<String> ccUsersAccount) {
        this.ccUsersAccount = ccUsersAccount;
    }

    public String getMultipartFilesPath() {
        return multipartFilesPath;
    }

    public void setMultipartFilesPath(String multipartFilesPath) {
        this.multipartFilesPath = multipartFilesPath;
    }

}
