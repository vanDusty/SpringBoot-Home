/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: Application
 * Author:   zhangfan
 * Date:     2019-06-14 15:34
 * Description: 启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.annotation.multipleDataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 〈一句话功能简述〉<br> 
 * 〈启动类〉
 *
 * @author zhangfan
 * @create 2019-06-14
 * @since 1.0.0
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}