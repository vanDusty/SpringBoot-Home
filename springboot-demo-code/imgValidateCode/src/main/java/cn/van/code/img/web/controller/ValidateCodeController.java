package cn.van.code.img.web.controller;

import cn.van.code.img.util.ImgValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ValidateCodeController
 *
 * @author: Van
 * Date:     2019-09-15 13:57
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RestController
@RequestMapping("/code")
public class ValidateCodeController {

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 生成图片验证码
     * @return
     */
    @GetMapping("/getImgCode")
    public Map<String, String> getImgCode() {

        Map<String, String> result = new HashMap<>();

        try {
            // 获取 4位数验证码
            result= ImgValidateCodeUtil.getImgCodeBaseCode(4);
            // 将验证码存入redis 中（有效时长5分钟）
            cacheImgCode(result);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    /**
     * 校验验证码
     * @param imgCodeKey
     * @param imgCode
     * @return
     */
    @GetMapping("/checkImgCode")
    public String checkImgCode(String imgCodeKey, String imgCode) {
        String cacheCode = redisTemplate.opsForValue().get(imgCodeKey);
        if (null == cacheCode) {
            return "图片验证码已过期，请重新获取";
        }
        if (cacheCode.equals(imgCode.toLowerCase())) {
            return "验证码输入正确";
        }
        return "验证码输入错误";

    }

    /**
     * 将验证码存入redis 中
     * @param result
     */
    public void cacheImgCode(Map<String, String> result) {
        String imgCode = result.get("imgCode");
        UUID randomUUID = UUID.randomUUID();
        String imgCodeKey = randomUUID.toString();
        System.out.println("imgCodeKey:" + imgCodeKey);
        // 图片验证码有效时间 ：5 分钟
        redisTemplate.opsForValue().set(imgCodeKey, imgCode, 5, TimeUnit.MINUTES);
        result.put("imgCodeKey", imgCodeKey);
    }
}
