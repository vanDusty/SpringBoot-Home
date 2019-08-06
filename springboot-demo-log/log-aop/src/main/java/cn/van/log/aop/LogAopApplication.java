package cn.van.log.aop;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author Van
 * @since 2019.2.26
 */
@SpringBootApplication
@Slf4j
public class LogAopApplication {


    public static void main(String[] args) {
        SpringApplication.run(LogAopApplication.class);
        log.info("LogAopApplication start!");
    }
}
