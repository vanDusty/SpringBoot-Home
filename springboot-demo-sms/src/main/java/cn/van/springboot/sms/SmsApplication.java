/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: SmsApplication
 * Author:   zhangfan
 * Date:     2019-06-11 17:37
 * Description: 启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.springboot.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 〈〉<br>
 * 〈启动类〉
 *
 * @author zhangfan
 * @create 2019-06-11
 * @since 1.0.0
 */
@SpringBootApplication
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class, args);
    }
}