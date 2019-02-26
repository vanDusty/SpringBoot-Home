package cn.van.logback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author Van
 * @since 2019.2.26
 */
@SpringBootApplication
public class SpringBootDemoLogbackApplication {

//    private static Logger logger = LoggerFactory.getLogger(SpringBootDemoLogbackApplication.class);

    public static void main(String[] args) {
//        logger.trace("trace level log.");
//        logger.debug("debug level log.");
//        logger.info("info level log.");
//        logger.warn("warn level log.");
//        logger.error("error level log.");
        SpringApplication.run(SpringBootDemoLogbackApplication.class);
    }
}
