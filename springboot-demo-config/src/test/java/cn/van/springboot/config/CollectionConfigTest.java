package cn.van.springboot.config;

import cn.van.springboot.config.config.AggregateConfig;
import cn.van.springboot.config.config.BeanObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: CollectionConfigTest
 *
 * @Author: VanFan
 * Date:     2022/2/24 8:43 PM
 * Description:
 * Version： V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CollectionConfigTest {

    @Resource
    private AggregateConfig aggregateConfig;

    @Value("${aggregate.collection.arr}")
    private String[] arrConfig;

    @Test
    public void arr() {
        log.info("注解获取数组：arr:{},{},{}", arrConfig[0], arrConfig[1], arrConfig[2]);
        String [] arr = aggregateConfig.getArr();
        log.info("配置类获取数组：arr:{},{},{}", arr[0], arr[1], arr[2]);
    }

    @Test
    public void list() {
        List<String> list = aggregateConfig.getList();
        log.info("配置类获取集合:{}",list);
        List<BeanObject> beanObjectList = aggregateConfig.getBean();
        log.info("配置类获取集合对象:{}", beanObjectList);
    }

    @Test
    public void map() {
        Map<String, Object> map = aggregateConfig.getMap();
        log.info("配置类获取map:{}",map);
    }
}
