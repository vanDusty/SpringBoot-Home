# SpringBoot 发送邮件

发送邮件应该是网站的必备拓展功能之一，注册验证、忘记密码或者是给用户发送营销信息。正常我们会用`JavaMail`相关`api`来写发送邮件的相关代码，但现在`SpringBoot`提供了一套更简易使用的封装。

本文主要演示了` Spring Boot `整合邮件功能，包括发送简单文本邮件、附件邮件、模板邮件。

## 一、项目初始化配置

### 1.1 项目依赖

```pom
<dependencies>
    <!-- test 包-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    <!--mail -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
    <!--使用 Thymeleaf 制作邮件模板 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

    <!-- lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>1.8.4</scope>
    </dependency>
    <!-- Hutool 工具包-->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
        <version>4.5.1</version>
    </dependency>
</dependencies>
```

### 1.2 项目配置文件

我这里使用的是这QQ邮箱作为测试。**注意**：`spring.mail.password`这个值不是QQ邮箱的密码，而是QQ邮箱给第三方客户端邮箱生成的授权码。具体参见：[https://service.mail.qq.com/cgi-bin/help?subtype=1&&no=1001256&&id=28](https://service.mail.qq.com/cgi-bin/help?subtype=1&&no=1001256&&id=28)

```yaml
server:
  port: 8081
spring:
  mail:
    host: smtp.qq.com
# 邮箱账号
    username: van93@qq.com
# 邮箱授权码
    password: **************
    default-encoding: UTF-8
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
```

## 二、代码实现

### 2.1 邮件实体参数：`Mail.java`

```java
@Data
public class Mail {
	
	/** 发送者*/
	private String sender;
	
	/** 接受者  */
	private String receiver;
	
	/** 消息主题 */
	private String subject;
	
	/** 消息内容*/
	private String text;
	/**
	 * 附件/文件地址
	 */
	private String filePath;
	/**
	 * 附件/文件名称
	 */
	private String fileName;
	/**
	 * 模版名称
	 */
	private String emailTemplateName;
	/**
	 * 模版内容
	 */
	private Context emailTemplateContext;
	
}
```

### 2.2 发送邮件接口：`MailService.java`

```java
public interface MailService {
    /**
     * 发送普通邮件
     * @param mail
     */
    void sendSimpleMail(Mail mail);

    /**
     * 发送附件邮件
     * @param mail
     * @throws MessagingException
     */
    void sendAttachmentsMail(Mail mail) throws MessagingException;

    /**
     * 发送模版邮件
     * @param mail
     */
    void sendTemplateMail(Mail mail);
}
```


### 2.3 发送邮件接口实现：`MailServiceImpl.java `

```java
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
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(mail.getReceiver());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getText());
        mailSender.send(mailMessage);
    }

    /**
     * ResourceUtil 来自 Hutool工具包
     * @param mail
     * @throws MessagingException
     */
    @Override
    public void sendAttachmentsMail(Mail mail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(sender);
        helper.setTo(mail.getReceiver());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getText());
        URL resource = ResourceUtil.getResource(mail.getFilePath());
        String filePath = resource.getPath();
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator)).substring(1);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        helper.addAttachment(fileName, file);
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendTemplateMail(Mail mail){
        // templateEngine 替换掉动态参数，生产出最后的html
        String emailContent = templateEngine.process(mail.getEmailTemplateName(), mail.getEmailTemplateContext());

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(mail.getReceiver());
            helper.setSubject(mail.getSubject());
            helper.setText(emailContent, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
    }

}
```


### 2.4 测试类：`MailServiceTest.java`

1. 测试附件邮件时，附件放在`static`文件夹下；
2. 测试模版邮件时，模版放在`templates`文件夹下。

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Resource
    MailService mailService;

    /**
     * 测试简单邮件
     */
    @Test
    public void sendSimpleMail() {
        Mail mail = new Mail();
        mail.setReceiver("17098705205@163.com");
        mail.setSubject("测试简单邮件");
        mail.setText("测试简单内容");
        mailService.sendSimpleMail(mail);
    }

    /**
     * 测试附件邮件
     */
    @Test
    public void sendAttachmentsMail() throws MessagingException {
        Mail mail = new Mail();
        mail.setReceiver("17098705205@163.com");
        mail.setSubject("测试附件邮件");
        mail.setText("附件邮件内容");
        mail.setFilePath("static/dusty_blog.jpg");
        mailService.sendAttachmentsMail(mail);
    }

    /**
     * 测试模版邮件邮件
     */
    @Test
    public void sendTemplateMail(){
        Mail mail = new Mail();
        mail.setReceiver("17098705205@163.com");
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
```

## 三、 扩展

### 3.1 发送失败情况

因为各种原因，总会有邮件发送失败的情况，比如：邮件发送过于频繁、网络异常等。在出现这种情况的时候，我们一般会考虑重新重试发送邮件，会分为以下几个步骤来实现：

1. 接收到发送邮件请求，首先记录请求并且入库；
1. 调用邮件发送接口发送邮件，并且将发送结果记录入库；
1. 启动定时系统扫描时间段内，未发送成功并且重试次数小于3次的邮件，进行再次发送；

### 3.1 异步发送

很多时候邮件发送并不是我们主业务必须关注的结果，比如通知类、提醒类的业务可以允许延时或者失败。这个时候可以采用异步的方式来发送邮件，加快主交易执行速度，在实际项目中可以采用`MQ`发送邮件相关参数，监听到消息队列之后启动发送邮件。
