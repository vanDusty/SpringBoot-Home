# SpringBoot：整合Generator 自动生成代码

> 本文主要介绍使用Maven插件的方式自动生成Mybatis相应的实体、sql映射文件、Dao等，能应付简单的CRUD（Create, Retrieve, Update, Delete）


## 一、上手使用

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

### 1.2 数据库配置

* `application.properties`

```yml
spring.datasource.url=jdbc:mysql://47.98.178.84:3306/root?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```


### 1.3 配置`generatorConfig.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!-- 导入配置文件 -->
    <properties resource="application.properties"/>

    <!--
    context:生成一组对象的环境
    id:必选，上下文id，用于在生成错误时提示
    defaultModelType:指定生成对象的样式
        1，conditional：类似hierarchical；
        2，flat：所有内容（主键，blob）等全部生成在一个对象中；
        3，hierarchical：主键生成一个XXKey对象(key class)，Blob等单独生成一个对象，其他简单属性在一个对象中(record class)
    targetRuntime:
        1，MyBatis3：默认的值，生成基于MyBatis3.x以上版本的内容，包括XXXBySample；
        2，MyBatis3Simple：类似MyBatis3，只是不生成XXXBySample；
    introspectedColumnImpl：类全限定名，用于扩展MBG
    -->

    <!-- defaultModelType="flat" 设置复合主键时不单独为主键创建实体 -->
    <context id="MySql" defaultModelType="flat" targetRuntime="MyBatis3Simple">

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

        <!-- 非必需，类型处理器，在数据库类型和java类型之间的转换控制--><!--一般保持默认-->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- 生成实体对象，并将类放到cn.van.generator.entity包下 -->
        <javaModelGenerator targetPackage="cn.van.mybatis.generator.entity" targetProject="src/main/java">
            <!-- 是否允许子包，即targetPackage.schemaName.tableName -->
            <property name="enableSubPackages" value="false"/>
            <!-- 是否对model添加 构造函数 -->
            <property name="constructorBased" value="true"/>
            <!-- 是否对类CHAR类型的列的数据进行trim操作 -->
            <property name="trimStrings" value="false"/>
            <!-- 建立的Model对象是否 不可改变  即生成的Model对象不会有 setter方法，只有构造方法 -->
            <property name="immutable" value="false"/>
        </javaModelGenerator>
        <!-- 生成mapper xml文件，并放到resources下的mapper文件夹下 -->
        <sqlMapGenerator targetPackage="mapper"  targetProject="src/main/resources"></sqlMapGenerator>
        <!-- 生成mapper xml对应dao接口，放到cn.van.generator.mapper包下-->
        <javaClientGenerator targetPackage="cn.van.mybatis.generator.mapper" targetProject="src/main/java" type="XMLMAPPER"></javaClientGenerator>
        <!-- 选择一个table来生成相关文件，可以有一个或多个table，必须要有table元素
            选择的table会生成一下文件：
            1，SQL map文件
            2，生成一个主键类；
            3，除了BLOB和主键的其他字段的类；
            4，包含BLOB的类；
            5，一个用户生成动态查询的条件类（selectByExample, deleteByExample），可选；
            6，Mapper接口（可选）

            tableName（必要）：要生成对象的表名；
            注意：大小写敏感问题。正常情况下，MBG会自动的去识别数据库标识符的大小写敏感度，在一般情况下，MBG会
                根据设置的schema，catalog或tablename去查询数据表，按照下面的流程：
                1，如果schema，catalog或tablename中有空格，那么设置的是什么格式，就精确的使用指定的大小写格式去查询；
                2，否则，如果数据库的标识符使用大写的，那么MBG自动把表名变成大写再查找；
                3，否则，如果数据库的标识符使用小写的，那么MBG自动把表名变成小写再查找；
                4，否则，使用指定的大小写格式查询；
            另外的，如果在创建表的时候，使用的""把数据库对象规定大小写，就算数据库标识符是使用的大写，在这种情况下也会使用给定的大小写来创建表名；
            这个时候，请设置delimitIdentifiers="true"即可保留大小写格式；

            可选：
            1，schema：数据库的schema；
            2，catalog：数据库的catalog；
            3，alias：为数据表设置的别名，如果设置了alias，那么生成的所有的SELECT SQL语句中，列名会变成：alias_actualColumnName
            4，domainObjectName：生成的domain类的名字，如果不设置，直接使用表名作为domain类的名字；可以设置为somepck.domainName，那么会自动把domainName类再放到somepck包里面；
            5，enableInsert（默认true）：指定是否生成insert语句；
            6，enableSelectByPrimaryKey（默认true）：指定是否生成按照主键查询对象的语句（就是getById或get）；
            7，enableSelectByExample（默认true）：MyBatis3Simple为false，指定是否生成动态查询语句；
            8，enableUpdateByPrimaryKey（默认true）：指定是否生成按照主键修改对象的语句（即update)；
            9，enableDeleteByPrimaryKey（默认true）：指定是否生成按照主键删除对象的语句（即delete）；
            10，enableDeleteByExample（默认true）：MyBatis3Simple为false，指定是否生成动态删除语句；
            11，enableCountByExample（默认true）：MyBatis3Simple为false，指定是否生成动态查询总条数语句（用于分页的总条数查询）；
            12，enableUpdateByExample（默认true）：MyBatis3Simple为false，指定是否生成动态修改语句（只修改对象中不为空的属性）；
            13，modelType：参考context元素的defaultModelType，相当于覆盖；
            14，delimitIdentifiers：参考tableName的解释，注意，默认的delimitIdentifiers是双引号，如果类似MYSQL这样的数据库，使用的是`（反引号，那么还需要设置context的beginningDelimiter和endingDelimiter属性）
            15，delimitAllColumns：设置是否所有生成的SQL中的列名都使用标识符引起来。默认为false，delimitIdentifiers参考context的属性

            注意，table里面很多参数都是对javaModelGenerator，context等元素的默认属性的一个复写；
         -->
        <!-- table标签可以有多个，至少一个，tableName指定表名，可以使用_和%通配符 -->
        <!-- domainObjectName/mapperName 可以自定义实体名称和Mapper名称 enable**:不生成复杂的sql -->
        <table tableName="tbl_user" domainObjectName = "UserDO" mapperName="UserMapper"  enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false">

            <!-- 是否只生成entity对象 -->
            <property name="modelOnly" value="false"/>
            <!-- 数据库中表名有时我们都会带个前缀，而实体又不想带前缀，这个配置可以把实体的前缀去掉 -->
            <domainObjectRenamingRule searchString="^Tbl" replaceString=""/>

        </table>
    </context>
</generatorConfiguration>



```

# 二、 自动生成代码

`idea` - `Maven` - `Plugins` - `mybatis-generator` - `mybatis-generator-generate`


