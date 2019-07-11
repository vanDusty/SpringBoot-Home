package cn.van.aliyun.sms.utils;

import cn.van.aliyun.sms.domain.SmsParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: AliYunSmsUtils
 *
 * @author: Van
 * Date:     2019-07-11 19:52
 * Description: 阿里云发送验证码工具类
 * Version： V1.0
 */
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
