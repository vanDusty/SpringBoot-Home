package cn.van.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author Van
 * @since 2019.2.26
 */
@SpringBootApplication
@EnableScheduling // 开启定时
public class SpringBootDemoTimeTaskApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootDemoTimeTaskApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoTimeTaskApplication.class);
        logger.info("SpringBootDemoTimeTaskApplication start!");
    }
}
