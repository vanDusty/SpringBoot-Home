package cn.van.sharding.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Classname ShardingApplication
 * @Description
 * @Author Van
 * @Date 2019-07-26 17:21
 * @Version 1.0
 */
@MapperScan("cn.van.sharding.demo.mapper")
@SpringBootApplication
public class ShardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingApplication.class, args);
    }

}
