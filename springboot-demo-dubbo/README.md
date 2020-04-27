# SpringBoot 整合Dubbo

> 注册中心采用的是zookeeper，详见[Mac 安装并运行 Zookeeper](https://blog.csdn.net/weixin_42036952/article/details/89449242)

## 一、新建项目

### 1.1 使用IDEA新建一个Maven主项目-`dubbo`

项目`pom.xml`

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <artifactId>dubbo</artifactId>
    <groupId>cn.van</groupId>
    <version>1.0-SNAPSHOT</version>

    <!--在这里设置打包类型为pom，作用是为了实现多模块项目-->
    <packaging>pom</packaging>
    <!-- 这里是我们子模块的设置 -->
    <modules>
        <module>dubbo-api</module>
        <module>dubbo-provider</module>
        <module>dubbo-consumer</module>
    </modules>

    <!-- 第一步：添加SpringBoot的parent -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.1.RELEASE</version>

    </parent>

    <!-- 设置我们项目的一些版本属性 -->
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>1.8</java.version>
        <dubbo.version>2.5.5</dubbo.version>
        <zkclient.version>0.10</zkclient.version>
        <lombok.version>1.16.18</lombok.version>
        <spring-boot.version>2.1.1.RELEASE</spring-boot.version>
    </properties>

    <!-- 声明一些项目依赖管理，方便我们的依赖版本管理 -->

    <dependencies>
        <!--&lt;!&ndash; Springboot依赖 &ndash;&gt;-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>

        <!-- Springboot-web依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>

        <!-- 使用lombok实现JavaBean的get、set、toString、hashCode、equals等方法的自动生成  -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- Dubbo依赖 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
        </dependency>

        <!-- zookeeper的客户端依赖 -->
        <dependency>
            <groupId>com.101tec</groupId>
            <artifactId>zkclient</artifactId>
            <version>${zkclient.version}</version>
        </dependency>

    </dependencies>
</project>

```

### 1.2 创建`dubbo`的子模块项目

#### 1.2.1 创建暴露Dubbo服务接口项目`dubbo-api`

`pom.xml`

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dubbo</artifactId>
        <groupId>cn.van</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>dubbo-api</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>

</project>
```

#### 1.2.2 创建Dubbo服务接口提供者项目`dubbo-provider `

`pom.xml`

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dubbo</artifactId>
        <groupId>cn.van</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>dubbo-provider</artifactId>

    <dependencies>
        <dependency>
            <groupId>cn.van</groupId>
            <artifactId>dubbo-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

#### 1.2.3 创建Dubbo服务接口消费者项目`dubbo-consumer `

`pom.xml`

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dubbo</artifactId>
        <groupId>cn.van</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>dubbo-consumer</artifactId>

    <dependencies>
        <dependency>
            <groupId>cn.van</groupId>
            <artifactId>dubbo-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

## 二、dubbo-api子项目

### 2.1 并创建一个实体类用于测试User.java

```java
 */
package cn.van.dubbo.domain;

import lombok.Data;
import java.io.Serializable;
/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */

@Data
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private Integer age;
    private Integer gender;
}
```

### 2.2 创建需要dubbo暴露的接口`TestService.java`

```java
package cn.van.dubbo.service;

import cn.van.dubbo.domain.UserDomain;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */
public interface TestService {

    String sayHello(String str);

    User findUser();
}
```

## 三、`dubbo-provider`子项目

### 3.1 首先实现我们在`dubbo-api`上定义的接口

创建一个TestServiceImpl类并实现TestService

```java
package cn.van.dubbo.service.impl;

import cn.van.dubbo.domain.UserDomain;
import cn.van.dubbo.service.DubboService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */
@Service("testService")
public class TestServiceImpl implements TestService {

    @Override
    public String sayHello(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(new Date()) + ": " + str;
    }

    @Override
    public User findUser() {
        User userDomain = new User();
        userDomain.setId(1001);
        userDomain.setUsername("scott");
        userDomain.setPassword("tiger");
        userDomain.setAge(20);
        userDomain.setGender(0);
        return userDomain;
    }
}
```
### 3.2 dubbo提供者配置

在`resources`下创建一个`dubbo`文件夹，在`config`下创建`spring-dubbo.xml`配置文件。

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <dubbo:application name="${dubbo.application.name}"/>
    <!-- 注册中心的ip地址 -->
    <dubbo:registry protocol="zookeeper" address="${dubbo.registry.address}"/>
    <!-- 协议/端口-->
    <dubbo:protocol name="dubbo" port="${dubbo.protocol.port}"/>
    <!-- 暴露的接口 -->
    <dubbo:service ref="testService" interface="cn.van.dubbo.service.DubboService"
                   timeout="3000" />
</beans>
```

### 3.3 新建SpringBoot的启动类`ProviderApplication.java`

```java
package cn.van.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 启动类
 *
 * @author Van
 * @since 2019.2.26
 */
@SpringBootApplication
@ImportResource({"classpath:dubbo/spring-dubbo.xml"})
public class ProviderApplication {

    private static final Logger logger = LoggerFactory.getLogger(ProviderApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class);
        logger.info("ProviderApplication start!");
    }
}

```

### 3.4 `application.yml`配置

```
# dubbo 相关配置
dubbo:
  application:
    name: provider
  registry:
    address: 127.0.0.1:2181
  protocol:
    port: 20880
```

## 四、dubbo-consumer子项目

### 4.1 新建一个测试controller `TestController `

```
package cn.van.dubbo.controller;

import cn.van.dubbo.domain.UserDomain;
import cn.van.dubbo.service.DubboService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */
@RestController
@RequestMapping("/")
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping("hello")
    public String hello() {
        return testService.sayHello("Hello springboot and dubbo!");
    }

    @GetMapping("userDomain")
    public User userDomain() {
        return testService.findUser();
    }
}
```

### 4.2 dubbo消费者配置

在`resources`下创建一个`dubbo`文件夹，在`config`下创建`spring-dubbo.xml`配置文件。

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
    <!-- 应用名 -->
    <dubbo:application name="consumer"/>
    <!-- 注册中心的ip地址 -->
    <dubbo:registry protocol="zookeeper" address="${dubbo.registry.address}"/>
    <!-- 消费方用什么协议获取服务（用dubbo协议在20880端口暴露服务）-->
    <dubbo:protocol name="dubbo" port="${dubbo.protocol.port}"/>
    <!-- 消费的服务 -->
    <dubbo:reference id="testService" interface="cn.van.dubbo.service.DubboService" check="false" timeout="5000" lazy="true" />

</beans>
```

### 4.3 新建SpringBoot的启动类`ConsumerApplication.java`

```java
package cn.van.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 启动类
 *
 * @author Van
 * @since 2019.2.26
 */
@SpringBootApplication
@ImportResource({"classpath:dubbo/spring-dubbo.xml"})
public class ConsumerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
        logger.info("ConsumerApplication start!");
    }
}

```

### 4.4 `application.yml`配置

```
server:
  # 指定端口号
  port: 8090
  servlet:
    # 指定项目地址
    context-path: /dubbo
# dubbo 相关配置
dubbo:
  registry:
    address: 127.0.0.1:2181
  protocol:
    port: 20880
```

## 五、启动项目，测试

1. 启动zookeeper；
2. 启动提供者项目`dubbo-provider`
3. 启动消费者项目`dubbo-consumer`
4. 访问测试：

[http://localhost:8090/dubbo/userDomain](http://localhost:8090/dubbo/userDomain)

[http://localhost:8090/dubbo/hello](http://localhost:8090/dubbo/hello)

## 四、源码及其延伸

1. 源码地址：[https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-dubbo](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-dubbo)
2. 整理不易，如果帮你解决了问题麻烦点个star，谢谢！