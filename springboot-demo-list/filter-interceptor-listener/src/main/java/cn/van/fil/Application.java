package cn.van.fil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @公众号： 风尘博客
 * @Classname Application
 * @Description TODO
 * @Date 2020/3/31 3:45 下午
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
