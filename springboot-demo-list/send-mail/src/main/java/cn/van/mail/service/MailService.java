package cn.van.mail.service;

import cn.van.mail.domain.Mail;

import javax.mail.MessagingException;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MailService
 *
 * @author: Van
 * Date:     2019-08-02 17:28
 * Description: 发送邮件接口
 * Version： V1.0
 */
public interface MailService {
    /**
     * 发送纯文本邮件
     * @param mail
     */
    void sendSimpleMail(Mail mail);

    /**
     * 发送邮件并携带附件
     * @param mail
     * @throws MessagingException
     */
    void sendAttachmentsMail(Mail mail) throws MessagingException;

    /**
     * 发送模版邮件
     * @param mail
     * @throws MessagingException
     */
    void sendTemplateMail(Mail mail) throws MessagingException;
}
