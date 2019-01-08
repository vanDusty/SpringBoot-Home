package com.van.dusty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第一个springboot程序的启动类
 * 其中 @RestController 等同于 （@Controller 与 @ResponseBody）
 */

@RestController
@SpringBootApplication
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

    @GetMapping("/demo")
    public String demo() {
        return "Hello Luis";
    }
}
