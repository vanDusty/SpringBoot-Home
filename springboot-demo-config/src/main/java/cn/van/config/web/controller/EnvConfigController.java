package cn.van.config.web.controller;

import cn.van.config.config.EnvConfig;
import cn.van.config.config.OriginalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: OriginalConfigController
 *
 * @author: Van
 * Date:     2018-12-13 16:02
 * Description: 不同环境配置读取
 * Version： V1.0
 */
@RestController
@RequestMapping("/env")
public class EnvConfigController {

    @Autowired
    EnvConfig envConfig;

    @GetMapping("/getDesc")
    public String getDesc() {
        String result = "读取的配置为：";
        System.out.println("配置开始读取");
        System.out.println("读取配置信息：");
        System.out.println("title: " + envConfig.getTitle());
        System.out.println("属性读取完毕！");
        result += envConfig.toString();
        return result;
    }
}
