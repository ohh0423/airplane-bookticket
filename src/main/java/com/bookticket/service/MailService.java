package com.bookticket.service;

/**
 * 封装一个发邮件的接口，后边直接调用即可
 */

public interface MailService {
    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return void
     */
    void sendSimpleMail(String to, String subject, String content);
    
    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return void
     */

    public void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     * @return void
     */

    public void sendAttachmentsMail(String to, String subject, String content, String filePath);
}
