package cn.van.mail.util;

import cn.van.mail.domain.Mail;
import cn.van.mail.domain.MailDomain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.util.Date;

/**
 * @公众号： 风尘博客
 * @Classname EmailUtil
 * @Description TODO
 * Date:     2019-08-09 18:18
 * @Author by Van
 */
@Component
public class EmailUtil {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 构建复杂邮件信息类
     * @param mail
     * @throws MessagingException
     */
    public void sendMail(MailDomain mail) throws MessagingException {

        //true表示支持复杂类型
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
        //邮件发信人从配置项读取
        mail.setSender(sender);
        //邮件发信人
        messageHelper.setFrom(mail.getSender());
        //邮件收信人
        messageHelper.setTo(mail.getReceiver().split(","));
        //邮件主题
        messageHelper.setSubject(mail.getSubject());
        //邮件内容
        if (mail.getIsTemplate()) {
            // templateEngine 替换掉动态参数，生产出最后的html
            String emailContent = templateEngine.process(mail.getEmailTemplateName(), mail.getEmailTemplateContext());
            messageHelper.setText(emailContent, true);
        }else {
            messageHelper.setText(mail.getText());
        }
        //抄送
        if (!StringUtils.isEmpty(mail.getCc())) {
            messageHelper.setCc(mail.getCc().split(","));
        }
        //密送
        if (!StringUtils.isEmpty(mail.getBcc())) {
            messageHelper.setCc(mail.getBcc().split(","));
        }
        //添加邮件附件
        if (mail.getFilePath() != null) {
            File file = new File(mail.getFilePath());
            messageHelper.addAttachment(file.getName(), file);
        }
        //发送时间
        if (StringUtils.isEmpty(mail.getSentDate())) {
            messageHelper.setSentDate(mail.getSentDate());
        }
        //正式发送邮件
        mailSender.send(messageHelper.getMimeMessage());
    }

    /**
     * 检测邮件信息类
     * @param mail
     */
    private void checkMail(MailDomain mail) {
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
    private MailDomain saveMail(MailDomain mail) {
        // todo 发送成功/失败将邮件信息同步到数据库
        return mail;
    }
}
