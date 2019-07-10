package cn.van.tx.sms.utils;

import cn.van.tx.sms.domain.SmsParams;
import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.*;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: TXSmsUtil
 *
 * @author: Van
 * Date:     2019-07-10 10:45
 * Description: 腾讯云发送验证码工具类
 * Version： V1.0
 */
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
