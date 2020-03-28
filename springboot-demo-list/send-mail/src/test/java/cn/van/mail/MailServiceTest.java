package cn.van.mail;

import cn.van.mail.domain.Mail;
import cn.van.mail.service.MailService;
import cn.van.mail.util.EmailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MailServiceTest
 *
 * @author: Van
 * Date:     2019-08-09 18:18
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Resource
    MailService mailService;

    /**
     * 发送纯文本邮件
     */
    @Test
    public void sendSimpleMail() {
        Mail mail = new Mail();
//        mail.setReceiver("17098705205@163.com");
        mail.setReceiver("van93@qq.com");
        mail.setSubject("测试简单邮件");
        mail.setText("测试简单内容");
        mailService.sendSimpleMail(mail);
    }

    /**
     * 发送邮件并携带附件
     */
    @Test
    public void sendAttachmentsMail() throws MessagingException {
        Mail mail = new Mail();
//        mail.setReceiver("17098705205@163.com");
        mail.setReceiver("van93@qq.com");
        mail.setSubject("测试附件邮件");
        mail.setText("附件邮件内容");
        mail.setFilePath("file/dusty_blog.jpg");
        mailService.sendAttachmentsMail(mail);
    }

    /**
     * 测试模版邮件邮件
     */
    @Test
    public void sendTemplateMail() throws MessagingException {
        Mail mail = new Mail();
//        mail.setReceiver("17098705205@163.com");
        mail.setReceiver("van93@qq.com");
        mail.setSubject("测试模版邮件邮件");
        //创建模版正文
        Context context = new Context();
        // 设置模版需要更换的参数
        context.setVariable("verifyCode", "6666");
        mail.setEmailTemplateContext(context);
        // 模版名称(模版位置位于templates目录下)
        mail.setEmailTemplateName("emailTemplate");
        mailService.sendTemplateMail(mail);
    }

}
