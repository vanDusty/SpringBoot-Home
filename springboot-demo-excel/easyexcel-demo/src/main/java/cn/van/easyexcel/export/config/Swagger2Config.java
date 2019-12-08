/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: Swagger2Config
 * Author:   zhangfan
 * Date:     2019-04-08 16:20
 * Description: Swagger配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.easyexcel.export.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Swagger配置类〉
 *
 * @author zhangfan
 * @create 2019-04-08
 * @since 1.0.0
 */
@EnableSwagger2
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定controller存放的目录路径
                .apis(RequestHandlerSelectors.basePackage("cn.van.easyexcel.export.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title("导出 Excel Restful APIs")
                // 文档描述
                .description("easyexcel 示例")
                .termsOfServiceUrl("https://www.dustyblog.cn")
                //定义版本号
                .version("v1.0")
                .build();
    }

}