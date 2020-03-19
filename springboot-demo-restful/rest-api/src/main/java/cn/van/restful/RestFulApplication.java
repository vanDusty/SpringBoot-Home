/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UserApplication
 * Author:   zhangfan
 * Date:     2019-01-29 17:09
 * Description: 用户模块启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.restful;


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
public class RestFulApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestFulApplication.class, args);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}