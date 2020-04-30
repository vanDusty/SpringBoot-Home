package cn.van.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 启动类
 *
 * @author Van
 * @since 2019.2.26
 */
@SpringBootApplication
@ImportResource({"classpath:dubbo/spring-dubbo.xml"})
@Slf4j
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
        log.info("ConsumerApplication start!");
    }
}
