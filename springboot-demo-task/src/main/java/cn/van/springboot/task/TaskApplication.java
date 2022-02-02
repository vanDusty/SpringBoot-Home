package cn.van.springboot.task;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class);
        log.info("TaskApplication start!");
    }
}
