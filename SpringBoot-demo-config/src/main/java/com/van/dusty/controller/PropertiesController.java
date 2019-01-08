package com.van.dusty.controller;

import com.van.dusty.properties.MyProperties1;
import com.van.dusty.properties.MyProperties2;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/properties")
@RestController
public class PropertiesController {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesController.class);

    private final MyProperties1 myProperties1;

    private final MyProperties2 myProperties2;

    @Autowired
    public PropertiesController(MyProperties1 myProperties1, MyProperties2 myProperties2) {
        this.myProperties1 = myProperties1;
        this.myProperties2 = myProperties2;
    }


    @GetMapping("/test1")
    public MyProperties1 getMyProperties1() {
        final String method = "myProperties1";
        logger.info("method = {},start",method);
        logger.info(myProperties1.toString());
        logger.info("method = {},end",method);
        return myProperties1;
    }

    @GetMapping("/test2")
    public MyProperties1 getMyProperties2() {
        final String method = "myProperties2";
        logger.info("method = {},start",method);
        logger.info(myProperties2.toString());
        logger.info("method = {},end",method);
        return myProperties1;
    }
}