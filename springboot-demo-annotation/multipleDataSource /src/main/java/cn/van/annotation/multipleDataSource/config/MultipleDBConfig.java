package cn.van.annotation.multipleDataSource.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultipleDBConfig {
    public static final String MASTER = "MASTER";
    public static final String SLAVES = "SLAVES";
//  将多个数据源放入动态数据源当中AbstractRoutingDataSource
    @Bean
    public AbstractRoutingDataSource dongtaiAbstractDataSource(@Qualifier("masterDatasource")DataSource masterDatasource,
                                                               @Qualifier("slavesDatasource")DataSource slavesDatasource){
        MyDatasource datasource = new MyDatasource();
        Map<Object, Object> targetDataSources = new HashMap();
        targetDataSources.put(MASTER,masterDatasource);
        targetDataSources.put(SLAVES,slavesDatasource);
        datasource.setTargetDataSources(targetDataSources);
        datasource.setDefaultTargetDataSource(masterDatasource);
        return datasource;
    }

}
