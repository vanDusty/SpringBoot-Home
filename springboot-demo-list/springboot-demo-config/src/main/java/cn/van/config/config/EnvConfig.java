package cn.van.config.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: EnvConfig
 *
 * @author: Van
 * Date:     2018-12-07 10:43
 * Description: 环境配置类
 * Version： V1.0
 */
@Data
@Configuration
public class EnvConfig {

    @Value("${title}")
    private String title;
}
