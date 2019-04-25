# SpringBoot 配置多数据源最简解决方案

> 现在主流的多数据源切换方式有：Aop 动态切换/根据 Jpa 来做多数据源或者升级成Mybatis-Plus可以简单的配置多数据源，偶然见到大佬整理的很简单的多数据源的配置，改进一下，分享出来。

## 一、依赖

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <version>2.1.1.RELEASE</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>multipledataSimple</artifactId>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>

        <!--mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!-- mybatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>
        <!-- druid连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <!-- lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>1.8.4</scope>
        </dependency>
    </dependencies>
</project>
```

## 二、配置文件 `application.yml`

```
spring:
  datasource:
    druid:
      master: # 主数据源
        username: root
        password: password
        driver-class-name: com.mysql.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/root
        initialSize: 5
        minIdle: 5
        maxActive: 20
      db2: # 第二个数据源
        username: root
        password: password
        driver-class-name: com.mysql.jdbc.Driver
        jdbc-url: jdbc:mysql://127.0.0.1:3306/user
        initialSize: 5
        minIdle: 5
        maxActive: 20
```

一个 master 库和一个 db2 库，其中 master是主库。可以有多个从库，我这里只演示一个从库db2。

## 三、数据源配置

### 3.1 主数据源配置`DataSource1Config.java`

```java
/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: DataSource1Config
 * Author:   zhangfan
 * Date:     2019-03-29 15:03
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 〈主数据源配置〉<br>
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-29
 * @since 1.0.0
 */
@Configuration
@MapperScan(basePackages = "cn.van.mybatis.dao.master", sqlSessionTemplateRef  = "masterSqlSessionTemplate")
// 指定主库扫描的 dao包，并给 dao层注入指定的 SqlSessionTemplate
public class DataSource1Config {
//    首先创建 DataSource
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }
//  然后创建 SqlSessionFactory
    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return bean.getObject();
    }
//  再创建事务
    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("masterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
//  最后包装到 SqlSessionTemplate 中
    @Bean(name = "masterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```

1. 首先创建 `DataSource`，然后创建 `SqlSessionFactory`，再创建事务，最后包装到 `SqlSessionTemplate` 中。其中需要指定库的 mapper 文件地址，以及分库dao层代码
1. `@MapperScan(basePackages = "***", sqlSessionTemplateRef  = "***")`:指定主库扫描的 dao包，并给 dao层注入指定的 `SqlSessionTemplate`;
1. `@Primary` 说明指定了主库。

### 3.2 从库数据源配置`DataSource2Config.java`

```java
package cn.van.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-29
 * @since 1.0.0
 */
@Configuration
@MapperScan(basePackages = "cn.van.mybatis.dao", sqlSessionTemplateRef  = "db2SqlSessionTemplate")
// 指定分库扫描的 dao包，并给 dao层注入指定的 SqlSessionTemplate
public class DataSource2Config {
    //    首先创建 DataSource
    @Bean(name = "db2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.db2")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }
    //  然后创建 SqlSessionFactory
    @Bean(name = "db2SqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("db2DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return bean.getObject();
    }
    //  再创建事务
    @Bean(name = "db2TransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("db2DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    //  最后包装到 SqlSessionTemplate 中
    @Bean(name = "db2SqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```

相对于主库的配置，不同点有：

1. 方法上没有`@Primary`的注解，说明是从库；
1. `@MapperScan(basePackages = "***", sqlSessionTemplateRef  = "***")`:指定主库扫描的 dao包 不同(主库的放在dao.master包下，从库直接放在dao包下)；
1. `@ConfigurationProperties(prefix = "spring.datasource.druid.**")`：指定的数据源不同。

## 四、测试代码部分

### 4.1 数据表

我在两个库中建的表是一摸一样的，sql 文件为：

```sql
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(10) NOT NULL COMMENT '用户名',
  `pass_word` varchar(10) NOT NULL COMMENT '密码',
  `user_sex` varchar(10) DEFAULT NULL COMMENT '性别',
  `nick_name` varchar(10) DEFAULT NULL COMMENT '昵称',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

```

### 4.2 业务层接口和实现

`TestService.java`

```java
package cn.van.mybatis.service;

import cn.van.mybatis.entity.User;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-25
 * @since 1.0.0
 */
public interface TestService {
    // 插入主库
    void insertMater(User user);
    // 插入从库
    void insert(User user);
}
```

`TestServiceImpl.java`

```java
package cn.van.mybatis.service.impl;


import cn.van.mybatis.dao.master.User1Mapper;
import cn.van.mybatis.dao.User2Mapper;
import cn.van.mybatis.entity.User;
import cn.van.mybatis.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-25
 * @since 1.0.0
 */
@Service
public class TestServiceImpl implements TestService {
    @Resource
    User1Mapper user1Mapper;
    @Resource
    User2Mapper user2Mapper;

    public void insertMater(User user) {
        user1Mapper.insert(user);
    }

    public void insert(User user) {
        user2Mapper.insert(user);
    }
}
```

### 4.3 mapper 接口和映射文件xml基本完全一致

#### 4.3.1 mapper 接口

```java
public interface User1Mapper {
    void insert(User user);
}
```
#### 4.3.2 xml的sql

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.van.mybatis.dao.master.User1Mapper" >
    <resultMap id="BaseResultMap" type="cn.van.mybatis.entity.User" >
        <id column="id" property="id" jdbcType="BIGINT" />
        <result column="user_name" property="userName" />
        <result column="pass_word" property="passWord" />
        <result column="user_sex" property="userSex" />
        <result column="nick_name" property="nickName"/>
        <result column="gmt_create" property="gmtCreate"/>
    </resultMap>

    <sql id="Base_Column_List" >
        id, user_name, pass_word, user_sex, nick_name,gmt_create
    </sql>

    <insert id="insert" parameterType="cn.van.mybatis.entity.User" >
        INSERT INTO
        users
        (user_name,pass_word,user_sex,nick_name)
        VALUES
        (#{userName}, #{passWord}, #{userSex}, #{nickName})
    </insert>
</mapper>
```

### 4.4 单元测试`MybatisTest.java`

```java
package cn.van.mybatis;
import cn.van.mybatis.dao.master.User1Mapper;
import cn.van.mybatis.dao.User2Mapper;
import cn.van.mybatis.entity.User;
import cn.van.mybatis.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-29
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MybatisTest {


    @Resource
    TestService testService;
    // 插入数据到主库
    @Test
    public void insertMater() {
        System.out.println(111);
        User user = new User();
        user.setUserName("van");
        user.setPassWord("222");
        user.setUserSex("eeee");
        testService.insertMater(user);
    }
    // 插入数据到从库
    @Test
    public void insert() {
        System.out.println(111);
        User user = new User();
        user.setUserName("Van");
        user.setPassWord("222");
        user.setUserSex("eeee");
        testService.insert(user);
    }
}
```

## 五、其他

1. 参考文章：[Spring Boot(七)：Mybatis 多数据源最简解决方案](http://www.ityouknow.com/springboot/2016/11/25/spring-boot-multi-mybatis.html);
1. 源码地址：[https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-mybatis/multipledata-simple](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-mybatis/multipledata-simple);
1. 整理不易，如果帮你解决了问题麻烦点个star，谢谢！