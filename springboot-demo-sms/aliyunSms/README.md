> 相比于腾讯云的短信服务，阿里云的短信好像更高大上一些，例如：阿里云控制台可以直接生成`Java/Node.js/Go/Php`等语言的 [ApiDemo](https://api.aliyun.com/new#/?product=Dysmsapi&api=SendSms&params=%7B%22RegionId%22%3A%22cn-hangzhou%22%2C%22PhoneNumbers%22%3A%22%22%2C%22SignName%22%3A%22%22%2C%22TemplateCode%22%3A%22%22%7D&tab=DEMO&lang=JAVA),并可以在线测试。当然，**腾讯云也有它的优势：每个月每个月100条的免费额度！**
> 

### 一、阿里云短信控制台

跟腾讯云类似，需要一系列的注册，创建AccessId、AccessKey、签名、短信模板以及等待认证，这些，都是在项目配置中需要用到的。

> 更多内容，详见 [Java SDK](https://help.aliyun.com/document_detail/112148.html?spm=a2c4g.11186623.6.632.61ff50a4nXbDFp)，这里就不多赘述了

### 二、项目示例

#### 2.1 项目依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<!-- 阿里云 SDK -->
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-core</artifactId>
    <version>4.1.0</version>
</dependency>
<!-- lombok-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<!--fastjson-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.32</version>
</dependency>
```

#### 2.2 项目配置文件`application.yml`

```
aliyun:
  sms:
    accessKeyId: accessKeyId
    accessKeySecret: accessKeySecret
    signName: 风尘博客
    templateCode: SMS_170181587
    smsEffectiveTime: 5
```

#### 2.3 封装一个参数实体`SmsParams`

```java
@Data
@Accessors(chain = true)
public class SmsParams {
    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 手机号码(多个手机号以英文状态下","分割)
     */
    private String phoneNumbers;


    public SmsParams(String phoneNumbers, String verifyCode) {
        this.phoneNumbers = phoneNumbers;
        this.verifyCode = verifyCode;
    }
}
```

#### 2.4 封装发送验证码的工具类`AliYunSmsUtils`

> 官网SDK 有：单发短信、群发短信以及拉取短信回执等方式发送验证码，我这里只演示最常用的单发短信"。

```java

@Component
public class AliYunSmsUtils {

    /**
     * 主账号AccessKey的ID
     */
    @Value("${aliyun.sms.accessKeyId}")
    String accessKeyId;
    /**
     * 主账号AccessKey的Secret
     */
    @Value("${aliyun.sms.accessKeySecret}")
    String accessKeySecret;
    /**
     * 签名
     */
    @Value("${aliyun.sms.signName}")
    String signName ;
    /**
     * 短信模板ID
     */
    @Value("${aliyun.sms.templateCode}")
    String templateCode ;

    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    private static final String product = "Dysmsapi";
    /**
     * 产品域名,开发者无需替换
     */
    private static final String domain = "dysmsapi.aliyuncs.com";
    /**
     * 系统规定参数
     */
    private static final String action = "SendSms";

    /**
     * 短信发送接口，支持在一次请求中向多个不同的手机号码发送同样内容的短信。
     * @param smsParams
     * @return
     */
    public String sendSms(SmsParams smsParams) {
        // 初始化acsClient
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion("2017-05-25");
        request.setAction(action);
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);

        Map<String,String> map = new HashMap(1);
        map.put("code" , smsParams.getVerifyCode());
        // 短信模板变量对应的实际值，JSON格式。
        request.putQueryParameter("TemplateParam", JSON.toJSONString(map));
        // 支持对多个手机号码发送短信，手机号码之间以英文逗号（,）分隔。上限为1000个手机号码
        request.putQueryParameter("PhoneNumbers", smsParams.getPhoneNumbers());

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getHttpStatus());
        } catch (ServerException e) {
            return e.getErrMsg();
        } catch (ClientException e) {
            return e.getErrMsg();
        }
        return "success";
    }
}

```

#### 2.5 测试发送验证码

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsTest {

    @Resource
    AliYunSmsUtils aliYunSmsUtils;

    @Test
    public void testSendSms() {
        // 生成随机的六位数验证码
        Random random = new Random(4);
        Integer verifyCode = random.nextInt(1000000);
        SmsParams smsParams = new SmsParams("17098705205",verifyCode.toString());
        System.out.println("生成的验证码为：" + verifyCode);
        String str = aliYunSmsUtils.sendSms(smsParams);
        System.out.println("发送结果：" + str);
    }
}
```

手机上接收到消息如下：

>【风尘博客】你的验证码为971862，该验证码5分钟内有效，请保护好您的验证码,如非本人操作，请忽略本短信。
>

说明发送成功！


### 三、源码地址

[https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-sms/aliyunSms](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-sms/aliyunSms)