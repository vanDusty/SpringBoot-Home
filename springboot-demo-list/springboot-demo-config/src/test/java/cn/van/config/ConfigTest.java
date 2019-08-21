package cn.van.config;

import cn.van.config.config.CustomizeConfig;
import cn.van.config.config.EnvConfig;
import cn.van.config.config.OriginalConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: ConfigTest
 *
 * @author: Van
 * Date:     2018-12-13 15:31
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigTest {

    @Autowired
    OriginalConfig originalConfig;

    /**
     * 读取自定义属性
     */
    @Test
    public void testOriginalConfig() {
        System.out.println("开始读取 application.yml 的自定义属性：");
        System.out.println("读取配置信息：");
        System.out.println("title: " + originalConfig.getTitle());
        System.out.println("desc: " + originalConfig.getDescription());
        System.out.println("application.yml 文件的属性读取完毕！");
    }

    @Autowired
    CustomizeConfig customizeConfig;

    /**
     * 从 myConfig.properties 获取配置
     */
    @Test
    public void testCustomizeConfig() {
        System.out.println("开始读取 myConfig.properties 文件的属性：");
        System.out.println("title: " + customizeConfig.getTitle());
        System.out.println("desc: " + customizeConfig.getDescription());
        System.out.println("myConfig.properties 文件的属性读取完毕！");
    }

    @Autowired
    EnvConfig envConfig;

    /**
     * 从 不同环境配置读取
     */
    @Test
    public void testEnvConfig() {
        System.out.println("开始读取 不同环境 文件的属性：");
        System.out.println("title: " + envConfig.getTitle());
        System.out.println("myConfig.properties 文件的属性读取完毕！");
    }


}
