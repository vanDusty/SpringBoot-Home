package com.van.dusty.properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:my2.properties")
@ConfigurationProperties(prefix = "my2")
public class MyProperties2 {

    private int age;
    private String name;
    private String email;
    // 省略 get set

    @Override
    public String toString() {
        return "MyProperties2{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}