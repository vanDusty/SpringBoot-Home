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
package cn.van.mybatis.demo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

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
@MapperScan("cn.van.mybatis.demo.mapper")
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    // /**
    //  * 配置mybatis的分页插件pageHelper
    //  * @return
    //  */
    // @Bean
    // public PageHelper pageHelper() {
    //     PageHelper pageHelper = new PageHelper();
    //     Properties props = new Properties();
    //     props.setProperty("dialect", "mysql");
    //     // 表示支持从接口中读取pageNum和pageSize
    //     props.setProperty("supportMethodsArguments", "true");
    //     pageHelper.setProperties(props);
    //     return pageHelper;
    // }

}