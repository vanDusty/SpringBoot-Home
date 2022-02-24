package cn.van.springboot.config.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: AggregateConfig
 *
 * @Author: VanFan
 * Date:     2022/2/24 8:40 PM
 * Description: 集合配置文件
 * Version： V1.0
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "aggregate.collection")
public class AggregateConfig {
    /**
     * 数组配置
     */
    String [] arr;
    /**
     * list集合配置
     */
    List<String> list;
    /**
     * list集合对象配置
     */
    List<BeanObject> bean;
    /**
     * map 配置
     */
    Map<String, Object> map;
}
