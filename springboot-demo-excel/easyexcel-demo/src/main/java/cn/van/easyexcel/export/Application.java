/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: Application
 *
 * @author: Van
 * Date:     2019-10-11 14:15
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
package cn.van.easyexcel.export;


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
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}