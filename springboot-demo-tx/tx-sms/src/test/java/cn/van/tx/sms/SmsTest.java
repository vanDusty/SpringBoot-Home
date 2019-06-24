/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: SmsTest
 * Author:   zhangfan
 * Date:     2019-06-12 16:19
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.tx.sms;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-06-12
 * @since 1.0.0
 */

// 参考文章 https://mp.weixin.qq.com/s/kWR-awfOutRZNAmpxvNzQA
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsTest {

    // 短信应用 SDK AppID
    int appid = 1400218546; // 1400开头

    // 短信应用SDK AppKey
    String appkey = "appkey";

    // 需要发送短信的手机号码
    String[] phoneNumbers = {"17098705205"};

    // 短信模板ID，需要在短信应用中申请
    int templateId = 350978; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
    //templateId7839对应的内容是"您的验证码是: {1}"
// 签名
    String smsSign = "风尘博客"; // NOTE: 签名参数使用的是`签名内容`，而不是`签名ID`。这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台申请

    @Test
    public void sendSms(){

        try {

            String[] params = {"123456","5"};//数组具体的元素个数和模板中变量个数必须一致，例如示例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0],
                    templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.println(result);
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
    }
}