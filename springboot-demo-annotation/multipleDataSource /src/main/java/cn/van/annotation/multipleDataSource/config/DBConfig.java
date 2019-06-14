package cn.van.annotation.multipleDataSource.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DBConfig {
    @ConfigurationProperties(prefix = "spring.datasource.master")
    @Bean("masterDatasource")
    public DataSource masterDatasource(){
        return new DruidDataSource();
    }
    @ConfigurationProperties(prefix = "spring.datasource.slaves")
    @Bean("slavesDatasource")
    @Primary
    public DataSource slavesDatasource(){
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
