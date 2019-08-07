# SpringBoot 分库分表ShardingSphere实战


## 一、 ShardingSphere

> 在我们的项目中，随着数据量的不断扩大，单表数据达到千万级别,查询性能急剧降低，这时候需要我们做分库分表，降低单表的数据量和单个数据库的请求压力。我们公司之前采用的是当当维护的`Sharding-jdbc`,后来官方又恢复维护了，也就是`ShardingSphere `。


1. `ShardingSphere`是一套开源的分布式数据库中间件解决方案组成的生态圈，它由`Sharding-JDBC`、`Sharding-Proxy`和`Sharding-Sidecar`（计划中）这3款相互独立的产品组成。 他们均提供标准化的数据分片、分布式事务和数据库治理功能，可适用于如`Java`同构、异构语言、容器、云原生等各种多样化的应用场景。
1. `ShardingSphere`定位为关系型数据库中间件，旨在充分合理地在分布式的场景下利用关系型数据库的计算和存储能力，而并非实现一个全新的关系型数据库。 它与`NoSQL`和`NewSQL`是并存而非互斥的关系。`NoSQL`和`NewSQL`作为新技术探索的前沿，放眼未来，拥抱变化，是非常值得推荐的。反之，也可以用另一种思路看待问题，放眼未来，关注不变的东西，进而抓住事物本质。 关系型数据库当今依然占有巨大市场，是各个公司核心业务的基石，未来也难于撼动，我们目前阶段更加关注在原有基础上的增量，而非颠覆。

**....图一**

1. `sharding-jdbc` 定位为轻量级`Java`框架，在`Java`的`JDBC`层提供的额外服务。 它使用客户端直连数据库，以`jar`包形式提供服务，无需额外部署和依赖，可理解为增强版的`JDBC`驱动，完全兼容JDBC和各种`ORM`框架。
1. 适用于任何基于`Java`的`ORM`框架，如：`JPA`, `Hibernate`, `Mybatis`, `Spring JDBC Template`或直接使用`JDBC`。 基于任何第三方的数据库连接池，如：`DBCP`, `C3P0`, `BoneCP`, `Druid`, `HikariCP`等。 支持任意实现JDBC规范的数据库。目前支持`MySQL`，`Oracle`，`SQLServer`和`PostgreSQL`。

**图二。。。。。**


## 二、 项目示例

> 本项目基于`SpringBoot + ShardingSphere + Mybatis `分库分表
> 

### 2.1 数据库准备

库表结构如下：

```
ds0
  ├── user_0 
  └── user_1 

ds1
  ├── user_0 
  └── user_1 
```

1. 为了演示，这里以分两个库两个表为例，实际上，我们项目中会更多，例如我们公司用户信息按照用户表id分成了100个表；
1. 因为是分库分表，两个库结构与所有的表结构一定是一致的。

### 2.2 项目配置

* 项目依赖

```pom
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--mysql-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    <!--Mybatis-->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.2</version>
    </dependency>
    <!--shardingsphere start-->
    <dependency>
        <groupId>io.shardingsphere</groupId>
        <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
        <version>3.1.0</version>
    </dependency>
    <dependency>
        <groupId>io.shardingsphere</groupId>
        <artifactId>sharding-jdbc-spring-namespace</artifactId>
        <version>3.1.0</version>
    </dependency>
    <!--shardingsphere end-->
    <!--lombok-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.8</version>
    </dependency>
</dependencies>
```

* application.yml


```yml
# 数据源 ds0,ds1
sharding:
  jdbc:
    datasource:
      names: ds0,ds1
      # 第一个数据库
      ds0:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.jdbc.Driver
        jdbc-url: jdbc:mysql://47.98.178.84:3306/ds0?characterEncoding=utf-8
        username: ds0
        password: password
      # 第二个数据库
      ds1:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.jdbc.Driver
        jdbc-url: jdbc:mysql://47.98.178.84:3306/ds1?characterEncoding=utf-8
        username: ds1
        password: password

    # 水平拆分的数据库（表） 配置分库 + 分表策略 行表达式分片策略
    # 分库策略
    config:
      sharding:
        default-database-strategy:
          inline:
            sharding-column: id
            algorithm-expression: ds$->{id % 2}

        # 分表策略 其中user为逻辑表 分表主要取决于user_age行
        tables:
          user:
            actual-data-nodes: ds$->{0..1}.user_$->{0..1}
            table-strategy:
              inline:
                sharding-column: user_age
                # 分片算法表达式
                algorithm-expression: user_$->{user_age % 2}

      # 打印执行的数据库以及语句
      props..sql.show: true
# bean被重复定义时覆盖
spring:
  main:
    allow-bean-definition-overriding: true
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: cn.van.sharding.demo.entity
```

通过配置文件方式实现分库以及分表：
1. 逻辑表 `user`：水平拆分的数据库（表）的相同逻辑和数据结构表的总称（该表并不存在）；
1. 真实表`user_0`、`user_1`：在分片的数据库中真实存在的物理表；
1. 分库规则：根据用户id分库，用2取模，余数为0放入`ds0`库，余数为1放入`ds1`库；
1. 分表规则：根据用户user_age分表，用2取模，余数为0放入`user_0`表，余数为1放入`user_1`表；


### 2.3 实体类

* `User.java` 

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class User{

    private Long id;

    private String userName;

    private Integer userAge;
}
```

### 2.4 Mapper 接口及映射文件

* `UserMapper.java`

```java
public interface UserMapper{

    int insert(User user);

    List<User> selectAll();

}
```

* `UserMapper.xml`

```xml
<resultMap id="BaseResultMap" type="cn.van.sharding.demo.entity.User">
    <id column="id" jdbcType="BIGINT" property="id" />
    <result column="user_name" jdbcType="VARCHAR" property="userName" />
    <result column="user_age" jdbcType="INTEGER" property="userAge" />
</resultMap>
<sql id="Base_Column_List">
    id, user_name, user_age
</sql>

<insert id="insert">
    insert into user (id,name,age) values
    (#{id}, #{userName}, #{userAge})
</insert>

<select id="selectAll" resultMap="BaseResultMap">
    select <include refid="Base_Column_List"/>
    from user
</select>
```

### 2.5 `Service`层以及实现类

* `UserService.java`

```java
public interface UserService{

    /**
     * 保存用户信息
     * @param entity
     * @return
     */
    int save(User entity);

    /**
     * 查询全部用户信息
     * @return
     */
    List<User> getUserList();
}
```


* `UserServiceImpl.java`

```java
Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Override
    public int save(User entity) {
        return userMapper.insert(entity);
    }

    @Override
    public List<User> getUserList() {
        return userMapper.selectAll();
    }
}
```

### 2.5 控制类测试

> 记得在启动类上加上mapper文件扫描`@MapperScan("cn.van.sharding.demo.mapper")`

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/select")
    public List<User> select() {
        return userService.getUserList();
    }

    @GetMapping("/insert")
    public int insert(User user) {
        return userService.save(user);
    }

}
```

## 三、测试

### 3.1 测试插入

* 打开浏览器或者在postman测试以下请求：

1. [http://localhost:8080/insert?id=1&userName=Van&userAge=11](http://localhost:8080/insert?id=1&userName=Van&userAge=11)
1. [http://localhost:8080/insert?id=1&userName=Van&userAge=11](http://localhost:8080/insert?id=3&userName=Van&userAge=12)
1. [http://localhost:8080/insert?id=1&userName=Van&userAge=11](http://localhost:8080/insert?id=2&userName=Van&userAge=13)
1. [http://localhost:8080/insert?id=1&userName=Van&userAge=11](http://localhost:8080/insert?id=4&userName=Van&userAge=14)

* 控制台日志打印如下：

```xml
2019-08-07 22:25:07.502  INFO 725 --- [nio-8080-exec-2] ShardingSphere-SQL : Actual SQL: ds1 ::: insert into user_1 (id,user_name,user_age) values (?, ?, ?) ::: [[1, Van, 11]]
2019-08-07 22:30:47.808  INFO 725 --- [nio-8080-exec-7] ShardingSphere-SQL : Actual SQL: ds1 ::: insert into user_0 (id,user_name,user_age) values (?, ?, ?) ::: [[3, Van, 12]] 
2019-08-07 22:32:14.277  INFO 725 --- [io-8080-exec-10] ShardingSphere-SQL : Actual SQL: ds0 ::: insert into user_1 (id,user_name,user_age) values (?, ?, ?) ::: [[2, Van, 13]] 
2019-08-07 22:33:08.426  INFO 725 --- [nio-8080-exec-1] ShardingSphere-SQL : Actual SQL: ds0 ::: insert into user_0 (id,user_name,user_age) values (?, ?, ?) ::: [[4, Van, 14]]
```

通过日志结合数据库数据我们可以发现，测试通过，数据按照我们设定的分库分表规则成功插入。同时，该中间件也帮我们查询做了封装，省略了`join`多张表联合查询。

### 3.2 测试查询

* 查询全部数据

1. [http://localhost:8080/select](http://localhost:8080/select)

* 控制台日志

```
2019-08-07 22:38:26.822  INFO 725 --- [nio-8080-exec-6] ShardingSphere-SQL : Actual SQL: ds0 ::: select id, user_name, user_age from user_0
2019-08-07 22:38:26.822  INFO 725 --- [nio-8080-exec-6] ShardingSphere-SQL : Actual SQL: ds0 ::: select id, user_name, user_age from user_1
2019-08-07 22:38:26.822  INFO 725 --- [nio-8080-exec-6] ShardingSphere-SQL : Actual SQL: ds1 ::: select id, user_name, user_age from user_0
2019-08-07 22:38:26.822  INFO 725 --- [nio-8080-exec-6] ShardingSphere-SQL : Actual SQL: ds1 ::: select id, user_name, user_age from user_1
```

* 返回结果

```
[
    {
        "id": 4,
        "userName": "Van",
        "userAge": 14
    },
    {
        "id": 2,
        "userName": "Van",
        "userAge": 13
    },
    {
        "id": 3,
        "userName": "Van",
        "userAge": 12
    },
    {
        "id": 1,
        "userName": "Van",
        "userAge": 11
    }
]
```

## 四、 总结

如果说，我们只想做分表，但是不分库，也是完全可以的，只需要修改一下配置文件即可。而且我们可以发现，通过此中间件进行分库分表，无侵入我们的代码 可以在原有的基础上改动配置即可，非常方便。
