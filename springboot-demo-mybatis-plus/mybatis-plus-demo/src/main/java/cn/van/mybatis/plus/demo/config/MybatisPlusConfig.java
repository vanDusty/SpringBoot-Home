package cn.van.mybatis.plus.demo.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MybatisPlusConfig
 *
 * @author: Van
 * Date:     2019-10-08 20:43
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
// @Configuration
public class MybatisPlusConfig {

    /**
     * 注册分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return  new PaginationInterceptor();
    }

}
