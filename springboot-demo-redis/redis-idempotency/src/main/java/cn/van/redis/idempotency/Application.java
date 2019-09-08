package cn.van.redis.idempotency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: Application
 *
 * @author: Van
 * Date:     2019-03-26 19:17
 * Description:
 * Version： V1.0
 */
@SpringBootApplication
@Slf4j
@EnableScheduling
public class Application extends WebMvcConfigurerAdapter {


    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        log.info("Application start!");
    }
    /**
     * 跨域
     * @return
     */
    // @Bean
    // public CorsFilter corsFilter() {
    //     final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    //     final CorsConfiguration corsConfiguration = new CorsConfiguration();
    //     corsConfiguration.setAllowCredentials(true);
    //     corsConfiguration.addAllowedOrigin("*");
    //     corsConfiguration.addAllowedHeader("*");
    //     corsConfiguration.addAllowedMethod("*");
    //     urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
    //     return new CorsFilter(urlBasedCorsConfigurationSource);
    // }


}
