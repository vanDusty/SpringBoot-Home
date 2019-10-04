package cn.van.jwt.demo.web.controller;
import cn.van.jwt.demo.annotation.JwtToken;
import cn.van.jwt.demo.util.JwtUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: JwtController
 *
 * @author: Van
 * Date:     2019-09-25 20:58
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RestController
@RequestMapping("/jwt")
public class JwtController {

    /**
     * 登录并获取token
     * @param userName
     * @param passWord
     * @return
     */
    @PostMapping("/login")
    public Object login( String userName, String passWord){
        JSONObject jsonObject=new JSONObject();
        // 检验用户是否存在(为了简单，这里假设用户存在，并制造一个uuid假设为用户id)
        String userId = UUID.randomUUID().toString();
        // 生成签名
        String token= JwtUtil.sign(userId);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("userName", userName);
        userInfo.put("passWord", passWord);
        jsonObject.put("token", token);
        jsonObject.put("user", userInfo);
        return jsonObject;
    }

    /**
     * 该接口需要带签名才能访问
     * @return
     */
    @JwtToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }
}
