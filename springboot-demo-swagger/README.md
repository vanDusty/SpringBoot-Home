# SpringBoot 配置 Swagger2

## 一、背景


## 二、上手使用

### 2.1 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!--Swagger-ui配置-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

### 2.2 Swagger全局配置

```java
@EnableSwagger2
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        //header中增加 token_id参数，没有可以去掉
//        ParameterBuilder token = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<Parameter>();
//        token.name("token_id").description("user token")
//                .modelRef(new ModelRef("string")).parameterType("header")
//                .required(false).build();
//        pars.add(token.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定controller存放的目录路径
                .apis(RequestHandlerSelectors.basePackage("cn.van.swagger.controller"))
                .paths(PathSelectors.any())
                .build();
//                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title("这里是Swagger文档标题")
                // 文档描述
                .description("这里是文档描述")
                .termsOfServiceUrl("https://www.dustyblog.cn")
                .version("v1.0")//定义版本号
                .build();
    }

}
```

### 2.3 SwaggerController控制层代码

```java
@Api(value = "Swagger测试", tags = "测试swagger配置")
@RestController
@RequestMapping("/test")
public class SwaggerController {
    /**
     * value: 表示描述
     * httpMethod: 支持的请求方式 // 可省略
     * response: 返回的自定义的实体类 // 可省略
     * @return
     */
    @ApiOperation(value = "sayHello", httpMethod = "GET")
    @GetMapping("/sayHello")
    public String sayHello(){
        return "hello Swagger!";
    }
}
```

- 说明
1. 类上面用`@Api`注解;
2. 方法上用`@ApiOperation`注解;

### 2.4 界面使用-测试

启动项目，打开项目文档地址：[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## 三 扩展

### 3.1 增加密码

> 接口文档我们往往需要让有权限的人查看，所以我们可以根据 Spring-Security增加账号密码管理

#### 3.1.1 新增依赖

```xml
<!--Swagger-ui 密码配置配置-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

#### 3.1.2 配置文件中增加账号密码配置

```yml
# Swagger-ui 密码配置配置
Spring:
  security:
    basic:
      path: /swagger-ui.html
      enabled: true
    user:
      name: admin
      password: 123456
```

> 增加了依赖和账号密码后重启项目，再次打开文档地址就要去输入账号和密码输入对应的账号和密码就可以登录了。

### 3.2 Demo源码

[https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-swagger](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-swagger)
如果帮你解决了问题麻烦点个star