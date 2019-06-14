package cn.van.annotation.multipleDataSource.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static cn.van.annotation.multipleDataSource.enums.DBEnum.MASTER;
import static cn.van.annotation.multipleDataSource.enums.DBEnum.SLAVES;

@Configuration
public class MultipleDBConfig {
    /**
     * 将多个数据源放入动态数据源当中AbstractRoutingDataSource
     * 以下两个参数跟DBConfig bean配置中保持一致
     * @param masterDS
     * @param slavesDS
     * @return
     */
    @Bean
    public AbstractRoutingDataSource dongtaiAbstractDataSource(@Qualifier("masterDS")DataSource masterDS,
                                                               @Qualifier("slavesDS")DataSource slavesDS){
        MyDatasource dataSource = new MyDatasource();
        Map<Object, Object> targetDataSources = new HashMap();
        targetDataSources.put(MASTER,masterDS);
        targetDataSources.put(SLAVES,slavesDS);
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(masterDS); // 设置默认数据源
        return dataSource;
    }

}
