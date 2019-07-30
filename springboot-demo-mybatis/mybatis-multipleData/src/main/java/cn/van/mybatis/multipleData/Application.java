package cn.van.mybatis.multipleData;


import cn.van.mybatis.multipleData.aspect.DynamicDataSourceAnnotationAdvisor;
import cn.van.mybatis.multipleData.config.DynamicDataSourceRegister;
import cn.van.mybatis.multipleData.aspect.DynamicDataSourceAnnotationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(DynamicDataSourceRegister.class)
@MapperScan("cn.van.mybatis.multipleData.mapper")
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
