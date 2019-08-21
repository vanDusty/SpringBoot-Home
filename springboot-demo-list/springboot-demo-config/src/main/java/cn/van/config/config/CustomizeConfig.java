package cn.van.config.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: CustomizeConfig
 *
 * @author: Van
 * Date:     2019-07-25 17:24
 * Description: @PropertySource :配置文件路径和编码格式
 * Version： V1.0
 */
@Data
@Component
@PropertySource(value = "classpath:myConfig.properties", encoding = "utf-8")
public class CustomizeConfig {
    /**
     * 注入 myConfig.properties 配置
     *
     */
    @Value("${customize.config.title}")
    private String title;

    @Value("${customize.config.description}")
    private String description;
}
