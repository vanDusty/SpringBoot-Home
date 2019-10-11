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
package cn.van.swagger.group.config;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: Swagger2Config
 *
 * @author: Van
 * Date:     2019-04-10 19:18
 * Description: Swagger配置类
 * Version： V1.0
 */
@EnableSwagger2
@Configuration
public class Swagger2Config {

    /**
     * 定义分隔符
     */
    private static final String SEPARATE = ";";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定controller存放的目录路径
                // 单个路径
                // .apis(RequestHandlerSelectors.basePackage("cn.van.swagger.group.web.controller"))
                // 多个路径
                .apis(basePackage("cn.van.swagger.group.web.controller" + SEPARATE +"cn.van.swagger.group.api"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title("这里是Swagger2构建Restful APIs")
                // 文档描述
                .description("这里是文档描述")
                .termsOfServiceUrl("https://www.dustyblog.cn")
                //定义版本号
                .version("v1.0")
                .build();
    }
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(SEPARATE)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}