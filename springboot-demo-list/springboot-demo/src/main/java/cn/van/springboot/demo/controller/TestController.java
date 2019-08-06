package cn.van.springboot.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: TestController
 *
 * @author: Van
 * Date:     2018-12-11 15:07
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @RequestMapping("/sayHello")
    public String sayHello() {
        return "Hello World !";
    }

}
