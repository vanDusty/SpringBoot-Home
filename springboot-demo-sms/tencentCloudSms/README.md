# SpringBoot 整合腾讯云发送短信

短信业务在我们日常使用的软件或者网站，应该算是无处不在的功能模块，比如注册、验证码功能。国内有很多互联网公司都提供短信业务，比如阿里、腾讯、七牛。本篇文章提及的是腾讯提供的短信服务。

### 一、腾讯短信服务

腾讯提供的短信业务，**提供每个月100 的免费额度**，用来测试足够了。打开[https://console.cloud.tencent.com/sms/smslist](https://console.cloud.tencent.com/sms/smslist)，在腾讯短信服务的页面，进行简单的注册以及简单的配置：创建签名和模板等，然后采用官方提供的SDK，调用接口即可。详细配置参见文章：[https://mp.weixin.qq.com/s/kWR-awfOutRZNAmpxvNzQA](https://mp.weixin.qq.com/s/kWR-awfOutRZNAmpxvNzQA)


[Java SDK文档](https://cloud.tencent.com/document/product/382/13613#%E5%8D%95%E5%8F%91%E7%9F%AD%E4%BF%A1)


### 二、SpringBoot 发送短信验证码

#### 2.1 项目配置文件

##### 2.1.1 所需要的依赖`pom.xml`

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>com.vaadin.external.google</groupId>
            <artifactId>android-json</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- 腾讯云sdk-->
<dependency>
    <groupId>com.github.qcloudsms</groupId>
    <artifactId>qcloudsms</artifactId>
    <version>1.0.6</version>
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

##### 2.1.2 项目配置文件`application.yml`

> 该配置在注册腾讯云以及配置后，可以在控制台拿到这些数据。

```
tx:
  sms:
    appId: appId
    appKey: appKey
    templateId: 1
    smsSign: 风尘博客
    smsEffectiveTime: 5
```

#### 2.2 封装一个验证码实体类`SmsParams`

> 这里为了简单，我只保留了必备的两个字断手机号和验证码，实际项目中还包括验证码类型、项目名称等等，请根据实际需要扩展。
> 

```
@Data
@Accessors(chain = true)
public class SmsParams {

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 手机号码
     */
    private String phone;


    public SmsParams(String phone, String verifyCode) {
       this.phone = phone;
       this.verifyCode = verifyCode;
    }
}
``` 

#### 2.3 封装发送验证码的工具类`TxCloudSmsUtil`

> 官网SDK 有：单发短信、指定模板 ID 单发短信、群发短信、指定模板群发短信以及拉取短信回执和短信回复状态（个人用户无法体验）等方式发送验证码，我这里只演示最常用的"指定模板 ID 单发短信"。


```
@Component
public class TxCloudSmsUtil {

    // 短信应用 SDK AppID
    @Value("${tx.sms.appId}")
    int appId; // 1400开头

    // 短信应用SDK AppKey
    @Value("${tx.sms.appKey}")
    String appKey;

    // 短信模板ID，需要在短信应用中申请
    @Value("${tx.sms.templateId}")
    int templateId ; // NOTE: 真实的模板ID需要在短信控制台中申请
    //我这里 templateId 对应的内容是"您的验证码是: {1}"
    // 签名
    @Value("${tx.sms.smsSign}")
    String smsSign ; // NOTE: 签名参数使用的是`签名内容`，而不是`签名ID`。这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台申请

    @Value("${tx.sms.smsEffectiveTime}")
    String smsEffectiveTime ;

    /**
     * 指定模板 ID 单发短信
     * @param smsParams
     */
    public String sendSms(SmsParams smsParams) {
        String rep = "error";
        try {
            String verifyCode = smsParams.getVerifyCode();
            // 数组具体的元素个数和模板中变量个数必须一致，例如示例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
            String[] params = {verifyCode,smsEffectiveTime};
            SmsSingleSender smsSingleSender = new SmsSingleSender(appId, appKey);
            // 签名参数未提供或者为空时，会使用默认签名发送短信
            SmsSingleSenderResult smsSingleSenderResult = smsSingleSender.sendWithParam("86", smsParams.getPhone(),
                    templateId, params, smsSign, "", "");
            System.out.println(smsSingleSenderResult);
            // 如果返回码不是0，说明配置有错，返回错误信息
            if (smsSingleSenderResult.result == 0) {
                rep = "success";
            } else {
                rep = smsSingleSenderResult.errMsg;
            }
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return rep;
    }

}
```

简单解释下，我这里封装的方法，如果短信发送成功，返回"success"，失败会返回`smsSingleSenderResult.errMsg`的详细原因（判断依据是：成功发送到返回码是：0）。实际运用中，可以在调用的地方对返回结果进行处理。

#### 2.4 测试发送验证码

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsTest {

    @Resource
    TxCloudSmsUtil txCloudSmsUtil;
    @Test
    public void testSms() {
        // 生成随机的六位数验证码
        Random random = new Random(4);
        Integer verifyCode = random.nextInt(1000000);
        SmsParams smsParams = new SmsParams("17098705205",verifyCode.toString());
        System.out.println("生成的验证码为：" + verifyCode);
        // smsParams.setPhone("17098705205").setVerifyCode();
        String str = txCloudSmsUtil.sendSms(smsParams);
        System.out.println(str);
    }

}
```

手机上接收到消息如下：

>【风尘博客】你的验证码为971862，该验证码5分钟内有效，请保护好您的验证码,如非本人操作，请忽略本短信。
>

说明发送成功！


#### 3 源码地址

[https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-sms/tencentCloudSms](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-sms/tencentCloudSms)

> 预告一下，下一期出SpringBoot 整合阿里云发送短信验证码