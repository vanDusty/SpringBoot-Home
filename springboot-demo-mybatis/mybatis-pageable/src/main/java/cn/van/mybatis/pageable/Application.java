/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: Application
 * Author:   Van
 * Date:     2019-01-29 17:09
 * Description: 启动类
 */
package cn.van.mybatis.pageable;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: Application
 *
 * @author: Van
 * Date:     2019-07-26 15:05
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.van.mybatis.pageable.mapper")
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}