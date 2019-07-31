package cn.van.mybatis.multipledata;


import cn.van.mybatis.multipledata.aspect.DynamicDataSourceAnnotationAdvisor;
import cn.van.mybatis.multipledata.config.DynamicDataSourceRegister;
import cn.van.mybatis.multipledata.aspect.DynamicDataSourceAnnotationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(DynamicDataSourceRegister.class)
@MapperScan("cn.van.mybatis.multipledata.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class Application {
    @Bean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor() {
        return new DynamicDataSourceAnnotationAdvisor(new DynamicDataSourceAnnotationInterceptor());
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
