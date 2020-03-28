package cn.van.mail;

import cn.van.mail.domain.Mail;
import cn.van.mail.domain.MailDomain;
import cn.van.mail.util.EmailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * @公众号： 风尘博客
 * @Classname EmailUtilTest
 * @Description TODO
 * Date:     2019-08-09 18:18
 * @Author by Van
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailUtilTest {

    @Resource
    EmailUtil emailUtil;

    @Test
    public void sendMail() throws MessagingException {
        MailDomain mail = new MailDomain();
        mail.setReceiver("17098705205@163.com,vanzhangf@gmail.com");
        mail.setSubject("测试简单邮件");
        mail.setText("测试简单内容");
//        mailService.sendSimpleMail(mail);
        emailUtil.sendMail(mail);
    }
    /**
     * 工具方法测试
     * @throws MessagingException
     */
    @Test
    public void sendMail1() throws MessagingException {
        MailDomain mail = new MailDomain();
        mail.setReceiver("17098705205@163.com");
        mail.setSubject("测试附件邮件");
        mail.setText("附件邮件内容");
        mail.setFilePath("file/dusty_blog.jpg");
        emailUtil.sendMail(mail);
    }
    /**
     * 工具方法测试
     * @throws MessagingException
     */
    @Test
    public void sendMail2() throws MessagingException {
        MailDomain mail = new MailDomain();
        mail.setReceiver("17098705205@163.com");
        mail.setSubject("测试模版邮件邮件");
        //创建模版正文
        Context context = new Context();
        // 设置模版需要更换的参数
        context.setVariable("verifyCode", "6666");
        mail.setEmailTemplateContext(context);
        // 模版名称(模版位置位于templates目录下)
        mail.setEmailTemplateName("emailTemplate");
        mail.setIsTemplate(true);
        emailUtil.sendMail(mail);
    }

}
