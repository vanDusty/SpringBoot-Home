package cn.van.config.web.controller;

import cn.van.config.config.CustomizeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: CustomizeConfigController
 *
 * @author: Van
 * Date:     2018-12-13 16:02
 * Description: 从 myConfig.properties 获取配置
 * Version： V1.0
 */
@RestController
@RequestMapping("/customize")
public class CustomizeConfigController {
    @Autowired
    CustomizeConfig customizeConfig;

    @GetMapping("/getDesc")
    public String getDesc() {
        String result = "读取 myConfig.properties 的配置为：";
        System.out.println("myConfig.properties 配置开始读取");
        System.out.println("读取配置信息：");
        System.out.println("title: " + customizeConfig.getTitle());
        System.out.println("desc: " + customizeConfig.getDescription());
        System.out.println("myConfig.properties 文件的属性读取完毕！");
        result += customizeConfig.toString();
        return result;
    }
}
