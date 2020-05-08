package cn.van.fil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @公众号： 风尘博客
 * @Classname Application
 * @Description 启动类
 * @Date 2020/3/31 8:45 下午
 * @Author by Van
 */
@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        log.info("Application start!");
    }
}
