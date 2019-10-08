package cn.van.mybatis.plus.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MybatisPlusApplication
 *
 * @author: Van
 * Date:     2019-10-08 20:46
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@SpringBootApplication
@MapperScan("cn.van.mybatis.plus.demo.mapper")
public class MybatisPlusApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }
}
