package cn.van.config.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: OriginalConfig
 *
 * @author: Van
 * Date:     2018-12-13 16:05
 * Description: 从 application.yml 获取配置
 * Version： V1.0
 */
@Data
@Component
// 采用下面的注解，可以省略@Value(xx)的注入，但是字段必须与配置文件中保持一致（文章中未采用该方式）
@ConfigurationProperties(prefix = "original.config")
public class OriginalConfig {
    /**
     * 注入 application.yml 配置
     *
     */
    // @Value("${original.config.title}")
    private String title;

    // @Value("${original.config.description}")
    private String description;

}
