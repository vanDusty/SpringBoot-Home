package cn.van.tx.sms.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: SmsCode
 *
 * @author: Van
 * Date:     2019-07-10 10:46
 * Description: 验证码实体类
 * Version： V1.0
 */
@Data
@Accessors(chain = true)
public class SmsCode {

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 手机号码
     */
    private String phone;
    /**
     * 手机号的集合
     */
    private List<String> phoneList;

    /**
     * 区号
     */
    private Integer zoneNum;

    /**
     * 验证码类型。注册/忘记密码
     */
    private Integer type;

    /**
     * 项目名称
     */
    public String projectName;

    /**
     * 图片验证码
     */
    private String pictureCode;

    /**
     * 登录平台：ios/android/web
     */
    private String osType;

    public SmsCode(String phone, String verifyCode) {
       this.phone = phone;
       this.verifyCode = verifyCode;
    }
}
