# Spring Boot集成mybatis generator自动生成代码插件

> 本文主要介绍使用Maven插件的方式自动生成Mybatis相应的实体、sql映射文件、Dao等，能应付简单的CRUD（Create, Retrieve, Update, Delete）


## 一、背景


## 二、上手使用

### 2.1 引入`MyBatis Generator`的Maven插件

```
<build>
    <plugins>
        <!--mybatis自动生成代码插件-->
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.6</version>
            <configuration>
                <!-- 是否覆盖，true表示会替换生成的JAVA文件，false则不覆盖 -->
                <overwrite>true</overwrite>
            </configuration>
            <dependencies>
                <!--mysql驱动包-->
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>5.1.45</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

### 2.2 `MyBatis Generator`一般配置


### 2.2.1 首先，在数据库执行以下sql生成即将操作的表`tbl_user`

```sql
DROP TABLE IF EXISTS `tbl_user`;
CREATE TABLE `tbl_user` (
  `id` bigint(20) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `user_age` int(11) DEFAULT NULL COMMENT '用户年龄'
);
```

### 2.2.2 `generatorConfig.xml` 配置如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!-- 导入配置文件 -->
    <properties resource="application.properties"/>

    <!-- defaultModelType="flat" 设置复合主键时不单独为主键创建实体 -->
    <context id="MySql" defaultModelType="flat">

        <!-- 生成的POJO实现java.io.Serializable接口 -->
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin" />

        <!--注释-->
        <commentGenerator>
            <!-- 将数据库中表的字段描述信息添加到注释 -->
            <!--<property name="addRemarkComments" value="true"/>-->
            <!-- 注释里不添加日期 -->
            <property name="suppressDate" value="true"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>

        </commentGenerator>
        <!-- 数据库连接，直接通过${}读取application.properties里的配置 -->
        <jdbcConnection
                driverClass="${spring.datasource.driver-class-name}"
                connectionURL="${spring.datasource.url}"
                userId="${spring.datasource.username}"
                password="${spring.datasource.password}"/>

        <!-- 生成实体对象，并将类放到cn.van.generator.entity包下 -->
        <javaModelGenerator targetPackage="cn.van.generator.entity" targetProject="src/main/java"></javaModelGenerator>
        <!-- 生成mapper xml文件，并放到resources下的mapper文件夹下 -->
        <sqlMapGenerator targetPackage="mapper"  targetProject="src/main/resources"></sqlMapGenerator>
        <!-- 生成mapper xml对应dao接口，放到cn.van.generator.mapper包下-->
        <javaClientGenerator targetPackage="cn.van.generator.mapper" targetProject="src/main/java" type="XMLMAPPER"></javaClientGenerator>

        <!-- table标签可以有多个，至少一个，tableName指定表名，可以使用_和%通配符 -->
        <!-- domainObjectName/mapperName 可以自定义实体名称和Mapper名称 enable**:不生成复杂的sql -->
        <table tableName="tbl_user" domainObjectName="UserDO" mapperName="UserDao" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false">

            <!-- 是否只生成entity对象 -->
            <property name="modelOnly" value="false"/>
            <!-- 数据库中表名有时我们都会带个前缀，而实体又不想带前缀，这个配置可以把实体的前缀去掉 -->
            <domainObjectRenamingRule searchString="^Tbl" replaceString=""/>

        </table>
    </context>
</generatorConfiguration>
```

> 配置中用到的数据库信息使用`application.properties`引入，如下：

```
spring.datasource.url=jdbc:mysql://47.98.178.84:3306/hope?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.username=hope
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

> 更多关于`generatorConfig.xml`配置可以 [参考官方文档](http://www.mybatis.org/generator/configreference/xmlconfig.html)

## 2.3 运行Maven插件生成代码

`idea` - `Maven` - `Plugins` - `mybatis-generator` - `mybatis-generator-generate`

### 3.2 Demo源码

[Spring Boot集成mybatis generator自动生成代码插件](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-generator)

如果帮你解决了问题麻烦点个star