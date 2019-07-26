package cn.van.config.web.controller;

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
 * Description: 从 application.yml 获取配置
 * Version： V1.0
 */
@RestController
@RequestMapping("/original")
public class OriginalConfigController {

    @Autowired
    OriginalConfig originalConfig;
    /**
     * 使用Environment类获取配置文件
     */
    @Autowired
    Environment env;

    @GetMapping("/getDesc")
    public String getDesc() {
        String result = "读取 application.yml 的配置为：";
        System.out.println("application.yml 配置开始读取");
        System.out.println("读取配置信息：");
        System.out.println("title: " + originalConfig.getTitle());
        System.out.println("desc: " + originalConfig.getDescription());
        System.out.println("Environment读取的端口：" + env.getProperty("server.port"));
        System.out.println("application.yml 文件的属性读取完毕！");
        result += originalConfig.toString();
        return result;
    }
}
