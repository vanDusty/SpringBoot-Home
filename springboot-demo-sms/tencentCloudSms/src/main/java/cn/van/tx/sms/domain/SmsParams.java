package cn.van.tx.sms.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: SmsParams
 *
 * @author: Van
 * Date:     2019-07-10 10:46
 * Description: 验证码实体类
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
     * 手机号码
     */
    private String phone;


    public SmsParams(String phone, String verifyCode) {
       this.phone = phone;
       this.verifyCode = verifyCode;
    }
}
