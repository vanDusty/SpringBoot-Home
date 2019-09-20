package cn.van.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: RedisLockApplication
 *
 * @author: Van
 * Date:     2019-09-17 19:47
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@SpringBootApplication
@Slf4j
@MapperScan("cn.van.redis.lock.mapper")
public class RedisLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisLockApplication.class);
        log.info("cn.van.redis.lock.RedisLockApplication start!");
    }
}
