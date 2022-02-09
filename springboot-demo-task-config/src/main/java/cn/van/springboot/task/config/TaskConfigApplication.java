package cn.van.springboot.task.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan("cn.van.springboot.task.config.mapper")
public class TaskConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskConfigApplication.class);
        log.info("TaskApplication start!");
    }
}
