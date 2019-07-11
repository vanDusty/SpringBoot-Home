package cn.van.aliyun.sms.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: SmsParams
 *
 * @author: Van
 * Date:     2019-07-11 20:40
 * Description: 验证码参数实体
 * Version： V1.0
 */
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
