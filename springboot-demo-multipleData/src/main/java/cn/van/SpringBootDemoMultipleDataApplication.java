package cn.van; /**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UserServiceApplication
 * Author:   zhangfan
 * Date:     2019-01-29 17:09
 * Description: 用户模块启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户模块启动类〉
 *
 * @author zhangfan
 * @create 2019-01-29
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("cn.van.dao")
public class SpringBootDemoMultipleDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoMultipleDataApplication.class, args);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}