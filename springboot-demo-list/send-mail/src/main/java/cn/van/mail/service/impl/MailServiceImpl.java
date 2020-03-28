package cn.van.mail.service.impl;

import cn.van.mail.domain.Mail;
import cn.van.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MailServiceImpl
 *
 * @author: Van
 * Date:     2019-08-02 17:28
 * Description: 发送邮件接口实现
 * Version： V1.0
 */
@Service("mailService")
@Slf4j
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    TemplateEngine templateEngine;
    /**
     * 默认发送者为配置中邮箱账号
     */
    @Value("${spring.mail.username}")
    private String sender;


    @Override
    public void sendSimpleMail(Mail mail){
        checkMail(mail);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(mail.getReceiver());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getText());
        mailSender.send(mailMessage);
        saveMail(mail);
    }

    @Override
    public void sendAttachmentsMail(Mail mail) throws MessagingException {
        checkMail(mail);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(sender);
        helper.setTo(mail.getReceiver());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getText());
        File file = new File(mail.getFilePath());
        helper.addAttachment(file.getName(), file);
        mailSender.send(mimeMessage);
        saveMail(mail);
    }

    @Override
    public void sendTemplateMail(Mail mail) throws MessagingException {
        checkMail(mail);
        // templateEngine 替换掉动态参数，生产出最后的html
        String emailContent = templateEngine.process(mail.getEmailTemplateName(), mail.getEmailTemplateContext());

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(sender);
        helper.setTo(mail.getReceiver());
        helper.setSubject(mail.getSubject());
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
        saveMail(mail);
    }

    /**
     * 检测邮件信息类
     * @param mail
     */
    private void checkMail(Mail mail) {
        if (StringUtils.isEmpty(mail.getReceiver())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mail.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mail.getText()) && null == mail.getEmailTemplateContext()) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    /**
     * 将邮件保存到数据库
     * @param mail
     * @return
     */
    private Mail saveMail(Mail mail) {
        // todo 发送成功/失败将邮件信息同步到数据库
        return mail;
    }
}
