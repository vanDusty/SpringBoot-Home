package cn.van.redis.view;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: Application
 *
 * @author: Van
 * Date:     2019-03-26 19:17
 * Description:
 * Version： V1.0
 */
@SpringBootApplication
@MapperScan("cn.van.redis.view.mapper")
@Slf4j
@EnableScheduling
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        log.info("Application start!");
    }
}
