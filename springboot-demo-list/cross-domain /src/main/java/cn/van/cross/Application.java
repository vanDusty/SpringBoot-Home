package cn.van.cross;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: Application
 *
 * @author: Van
 * Date:     2019-08-02 11:29
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        log.info("cn.van.cross.Application start!");
    }
}
