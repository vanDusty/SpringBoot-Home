# SpringBoot 配置 Swagger2

## 一、背景介绍

### 1.1 Swagger 介绍
`Swagger` 是一套基于 `OpenAPI` 规范构建的开源工具，可以帮助我们设计、构建、记录以及使用 `Rest API`。`Swagger` 主要包含了以下三个部分：

1. `Swagger Editor`：基于浏览器的编辑器，我们可以使用它编写我们 `OpenAPI` 规范。
1. `Swagger UI`：它会将我们编写的 `OpenAPI` 规范呈现为交互式的 `API` 文档，后文我将使用浏览器来查看并且操作我们的 `Rest API`。
1. `Swagger Codegen`：它可以通过为 `OpenAPI`（以前称为 Swagger）规范定义的任何 `API` 生成服务器存根和客户端 `SDK` 来简化构建过程。


### 1.2 Swagger优缺点

* 优点

1. 易用性好，`Swagger UI`提供很好的API接口的UI界面，可以很方面的进行API接口的调用。
1. 时效性和可维护性好，`API`文档随着代码变更而变更。 `Swagger`是根据注解来生成文`API`档的，我们可以在变更代码的时候顺便更改相应的注解即可。
1. 易于测试，可以将文档规范导入相关的工具（例如 `SoapUI`）, 这些工具将会为我们自动地创建自动化测试。

* 缺点

1. 重复利用性差，因为`Swagger`毕竟是网页打开，在进行接口测试的时候很多参数无法进行保存，因此不易于重复利用。
1. 复杂的场景不易模拟，比如使用`token`鉴权的，可能每次都需要先模拟登录，再来进行接口调用。


### 1.3 Swagger 相关地址

1. Swagger官网：[http://swagger.io](http://swagger.io)
1. Swagger的GitHub地址：[https://github.com/swagger-api](https://github.com/swagger-api)

## 二、 上手使用

### 2.1 项目准备

* 项目依赖

```pom
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

* 项目配置

```yml
## 项目端口和项目路径
server:
  port: 8081
  servlet:
    context-path: /swagger
```


### 2.2 Swagger全局配置

> 在启动的时候添加`@EnableSwagger2`注解开启，然后再使用`@Bean`注解初始化一些相应的配置。
 

```java
@EnableSwagger2
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定controller存放的目录路径
                .apis(RequestHandlerSelectors.basePackage("cn.van.swagger.web.controller"))
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
}
```

### 2.3 `Controller` 控制层

`Swagger` 主要的使用就是在控制层这块，它是通过一些注解来为接口提供`API`文档。下列是`Swagger`的一些注解说明，更详细的可以查看官方的wiki文档。

1. `@Api` ：将类标记为`Swagger`资源，并给该类增加说明
1. `@ApiOperation` :注解来给接口方法增加说明；
1. `@ApiImplicitParam` ：表示`API`操作中的单个参数。
1. `@ApiImplicitParams` ：一个包装器，允许列出多个`ApiImplicitParam`对象。
1. `@ApiModel` ：提供有关`Swagger`模型的其他信息，比如描述`POJO`对象。
1. `@ApiModelProperty` ： 添加和操作模型属性的数据。
1. `@ApiOperation` ： 描述针对特定路径的操作或通常是`HTTP`方法。
1. `@ApiParam` ： 为操作参数添加其他元数据。
1. `@ApiResponse` ： 描述操作的可能响应。
1. `@ApiResponses` ： 一个包装器，允许列出多个`ApiResponse`对象。
1. `@Authorization` ： 声明要在资源或操作上使用的授权方案。
1. `@AuthorizationScope` ： 描述`OAuth2`授权范围。
1. `@ResponseHeader` ： 表示可以作为响应的一部分提供的标头。
1. `@ApiProperty` ： 描述`POJO`对象中的属性值。
1. `@ApiError` ： 接口错误所返回的信息

官方wiki文档地址:

[https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations)

#### 2.3.1 参数实体`User`

```java
@Data
@ApiModel(value = "用户信息对象", description = "姓名、性别、年龄")
public class User {
    @ApiModelProperty(value = "主键id")
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "用户性别")
    private String sex;
    @ApiModelProperty(value = "用户年龄")
    private int age;
}
```

#### 2.3.2 控制层示例

```java
@RestController
@RequestMapping("/swagger")
@Api(tags = "Swagger接口")
public class SwaggerController {
    /**
     *  无参方法
     * @return
     */
    @ApiOperation(value = "无参方法", httpMethod = "GET")
    @GetMapping("/sayHello")
    public String sayHello(){
        return "hello Swagger!";
    }

    /**
     * 单参方法
     * @param id
     * @return
     */
    @ApiImplicitParam( name = "id", value = "主键id")
    @ApiOperation(value = "单参方法", httpMethod = "POST")
    @PostMapping("/hasParam")
    public Long hasParam(Long id) {
        return id;
    }

    /**
     * 多参方法(required = true可指定某个参数为必填)
     * @param id
     * @param userName
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam( name = "id", value = "主键id", required = true),
            @ApiImplicitParam( name = "userName", value = "用户名", required = true)
    })
    @ApiOperation(value = "多参方法", httpMethod = "POST")
    @PostMapping("/hasParams")
    public String hasParams(Long id, String userName) {
        return userName;
    }

    /**
     * 参数是实体类的方法（需要在实体类中增加注解进行参数说明）
     * @param user
     * @return
     */
    @ApiOperation(value = "实体参数方法", httpMethod = "PUT")
    @PutMapping("/entityParam")
    public User entityParam(@RequestBody User user) {
        return user;
    }

}
```

### 2.4 测试

启动项目，打开项目文档地址：[http://localhost:8081/swagger/swagger-ui.html](http://localhost:8081/swagger/swagger-ui.html)



## 三 新增访问密码

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