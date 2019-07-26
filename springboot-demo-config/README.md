# Spring Boot: 配置文件详解

> SpringBoot是为了简化Spring应用的创建、运行、调试、部署等一系列问题而诞生的产物，自动装配的特性让我们可以更好的关注业务本身而不是外部的XML配置，我们只需遵循规范，引入相关的依赖就可以轻易的搭建出一个 WEB 工程。

## 一、准备前提

为了让SpringBoot更好的生成数据，我们需要添加如下依赖（该依赖可以不添加，但是在 IDEA 不会有属性提示），该依赖只会在编译时调用，所以不用担心会对生产造成影响…

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

## 二、自定义属性配置

> SpringBoot 主配置文件默认为`application.yml`或者`application.properties`，我更喜欢用前者，所以文章中基本以第一种为例读取自定义属性

### 2.1 `application.yml` 自定义属性

```yml
server:
  port: 8081

original:
  config:
    title: OriginalConfig
    description: 主配置文件中属性
```
### 2.2 新建`OriginalConfig`配置类

> 该配置类用来映射我们在`application.yml`中的内容，这样一来我们就可以通过操作对象的方式来获得配置文件的内容了。

```java
@Data
@Component
public class OriginalConfig {
    /**
     * 注入 application.yml 配置
     *
     */
    @Value("${original.config.title}")
    private String title;

    @Value("${original.config.description}")
    private String description;
    
}
```

> 这里，可以采用`@ConfigurationProperties`简化`@Value`注入值的写法,详见文末Github源码写法。

### 2.3 书写控制器`OriginalConfigController`测试

```java
@RestController
@RequestMapping("/original")
public class OriginalConfigController {

    @Autowired
    OriginalConfig originalConfig;
    /**
     * 使用Environment类获取配置文件
     */
    @Autowired
    Environment env;

    @GetMapping("/getDesc")
    public String getDesc() {
        String result = "读取 application.yml 的配置为：";
        System.out.println("application.yml 配置开始读取");
        System.out.println("读取配置信息：");
        System.out.println("title: " + originalConfig.getTitle());
        System.out.println("desc: " + originalConfig.getDescription());
        System.out.println("Environment读取的端口：" + env.getProperty("server.port"));
        System.out.println("application.yml 文件的属性读取完毕！");
        result += originalConfig.toString();
        return result;
    }
}
```

1. 启动项目，然后访问[http://localhost:8081/original/getDesc](http://localhost:8081/original/getDesc)

1. 控制台打印如下：

```
application.yml 配置开始读取
读取配置信息：
title: OriginalConfig
desc: 主配置文件中属性
Environment读取的端口：8081
application.yml 文件的属性读取完毕！
```

## 三、自定义文件配置 

### 3.1 定义一个名为`myConfig.properties`的自定义文件

> 自定义配置文件的命名不强制application开头

```
customize.config.title=CustomizeConfig
customize.config.description=自定义配置文件中属性
```
   
### 3.2 新建`CustomizeConfig`配置类

> 该配置类用来映射我们在`myConfig.properties`中的内容，这样一来我们就可以通过操作对象的方式来获得配置文件的内容了。

```java
@Data
@Component
// 配置文件路径和编码格式
@PropertySource(value = "classpath:myConfig.properties", encoding = "utf-8")
public class CustomizeConfig {
    /**
     * 注入 myConfig.properties 配置
     *
     */
    @Value("${customize.config.title}")
    private String title;

    @Value("${customize.config.description}")
    private String description;
}
```

> 这里，可以采用`@ConfigurationProperties`简化`@Value`注入值的写法,详见文末Github源码写法。

### 2.3 书写控制器`OriginalConfigController`测试

```java
@RestController
@RequestMapping("/customize")
public class CustomizeConfigController {
    @Autowired
    CustomizeConfig customizeConfig;

    @GetMapping("/getDesc")
    public String getDesc() {
        String result = "读取 myConfig.properties 的配置为：";
        System.out.println("myConfig.properties 配置开始读取");
        System.out.println("读取配置信息：");
        System.out.println("title: " + customizeConfig.getTitle());
        System.out.println("desc: " + customizeConfig.getDescription());
        System.out.println("myConfig.properties 文件的属性读取完毕！");
        result += customizeConfig.toString();
        return result;
    }
}
```

1. 启动项目，然后访问[http://localhost:8081/customize/getDesc](http://localhost:8081/customize/getDesc)

1. 控制台打印如下：

```
myConfig.properties 配置开始读取
读取配置信息：
title: CustomizeConfig
desc: 自定义配置文件中属性
myConfig.properties 文件的属性读取完毕！
```



## 四、多环境化配置

> 真实的应用中，常常会有多个环境（如：本地，开发，测试，正式），不同的环境数据库连接都不一样，这个时候就需要用到`spring.profile.active`，它的格式为application-{profile}.yml，这里的`application`为前缀不能改，`{profile}`是我们自己定义的。

### 4.1 指定不同环境的路径

> 通过`server.servlet.context-path`将不同环境指定不同的路径。


* `application-local.yml`

```
title: local配置文件
server:
  servlet:
    context-path: /local
```

* `application-daily.yml`

```
title: daily配置文件
server:
  servlet:
    context-path: /daily
```

* `application-gray.yml`

```
title: gray配置文件
server:
  servlet:
    context-path: /gray
```


* `application-production.yml`

```
title: production配置文件
server:
  servlet:
    context-path: /production
```

### 4.2 新增配置类`EnvConfig`及测试的控制器`EnvController`

```java
@Data
@Configuration
public class EnvConfig {

    @Value("${title}")
    private String title;
}
```

```java
@RestController
@RequestMapping("/env")
public class EnvConfigController {

    @Autowired
    EnvConfig envConfig;

    @GetMapping("/getDesc")
    public String getDesc() {
        String result = "读取的配置为：";
        System.out.println("配置开始读取");
        System.out.println("读取配置信息：");
        System.out.println("title: " + envConfig.getTitle());
        System.out.println("属性读取完毕！");
        result += envConfig.toString();
        return result;
    }
}
```

### 4.3 激活某个环境配置

在`application.yml`配置文件中指定环境，例如指定daily环境：

```
spring:
  profiles:
    active: daily
```

### 4.4 测试

这个时候我们在次访问[http://localhost:8081/env/getDesc](http://localhost:8081/env/getDesc)就没用处了，因为我们设置了它的`context-path=/daily`，所以新的路径就是[http://localhost:8081/daily/env/getDesc](http://localhost:8081/daily/env/getDesc),可以通过此方式切换环境。

