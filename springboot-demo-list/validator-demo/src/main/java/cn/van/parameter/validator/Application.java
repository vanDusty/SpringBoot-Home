package cn.van.parameter.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @公众号： 风尘博客
 * @Classname Application
 * @Description 启动类
 * @Date 2019/9/09 9:32 下午
 * @Author by Van
 */
@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        logger.info("cn.van.parameter.validator.Application start!");
    }
}
