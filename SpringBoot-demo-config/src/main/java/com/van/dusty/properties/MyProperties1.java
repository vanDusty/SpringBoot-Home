package com.van.dusty.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "my1")
public class MyProperties1 {

    private int age;
    private String name;
    // 省略 get set

    @Override
    public String toString() {
        return "MyProperties1{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
