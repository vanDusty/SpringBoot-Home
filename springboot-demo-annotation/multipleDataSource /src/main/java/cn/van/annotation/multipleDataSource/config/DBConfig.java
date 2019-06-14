package cn.van.annotation.multipleDataSource.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class DBConfig {
    @ConfigurationProperties(prefix = "spring.datasource.master")
    @Bean("masterDS")
    public DataSource masterDS(){
        return new DruidDataSource();
    }
    @ConfigurationProperties(prefix = "spring.datasource.slaves")
    @Bean("slavesDS")
    @Primary
    public DataSource slavesDS(){
        return new DruidDataSource();
    }
//  该处必须要自己进行加载操作数据库框架中的数据源，否则动态数据源将不起效
    @Resource
    private DataSource dataSource;
    @Bean
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }
}
