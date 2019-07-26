# SpringBoot：整合Mybatis+Druid
> Druid是什么?
> 
>1. Druid是一个JDBC组件库，包括数据库连接池、SQL Parser等组件;
>1. DruidDataSource是最好的数据库连接池;
>1. Druid能够提供强大的监控(可视化)和扩展功能。

## 一、添加依赖和项目配置

* 项目依赖

```pom
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
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
```
* `application.yml`

```yml
#端口
server:
  port: 8082

# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/van_test
    username: root
    password: password
    driver-class-name: com.mysql.jdbc.Driver

    # 使用druid数据源
    type: com.alibaba.druid.pool.DruidDataSource
    # 配置获取连接等待超时的时间
    # 下面为连接池的补充设置，应用到上面所有数据源中
    # 初始化大小，最小，最大
    initialSize: 1
    minIdle: 3
    maxActive: 20
    # 配置获取连接等待超时的时间
    maxWait: 60000
    # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    timeBetweenEvictionRunsMillis: 60000
    # 配置一个连接在池中最小生存的时间，单位是毫秒
    minEvictableIdleTimeMillis: 30000
    validationQuery: select 'x'
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    # 打开PSCache，并且指定每个连接上PSCache的大小
    poolPreparedStatements: true
    maxPoolPreparedStatementPerConnectionSize: 20
    # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
    filters: stat,wall,slf4j
    # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
    # 合并多个DruidDataSource的监控数据
    useGlobalDataSourceStat: true

# mybatis
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: cn.van.mybatis.demo.entity
```

## 二、业务相关代码

### 2.1 建表sql

```sql
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT ''自增主键'',
  `user_name` varchar(50) NOT NULL COMMENT ''用户名'',
  `user_age` int(3) DEFAULT 0 COMMENT ''用户年龄''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 插入测试数据

INSERT INTO `tb_user` VALUES (1, ''张三'', 27);
INSERT INTO `tb_user` VALUES (2, ''李四'', 30);
INSERT INTO `tb_user` VALUES (3, ''王五'', 20);
```

### 2.2 表对应的实体类

```java
@Data
public class UserDO {
    private Long id;
    private String userName;
    private Integer userAge;
}
```

### 2.3 DAO层以及mapper.xml

* `UserMapper.java`

```java
public interface UserMapper {

    UserDO selectById(Long id);

    List<UserDO> selectAll();
}
```

* `UserMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.van.mybatis.druid.mapper.UserMapper" >
    <resultMap id="BaseResultMap" type="cn.van.mybatis.druid.entity.UserDO" >
        <id column="id" property="id"/>
        <result column="user_name" property="userName" />
        <result column="user_age" property="userAge" />
    </resultMap>

    <sql id="Base_Column_List" >
        id, user_name, user_age
    </sql>

    <select id="selectById" resultMap="BaseResultMap">
        select <include refid="Base_Column_List"/>
        from tb_user
        where id = #{id}
    </select>

    <select id="selectAll" resultMap="BaseResultMap">
      select <include refid="Base_Column_List"/>
      from tb_user
    </select>
</mapper>
```

### 2.4 业务层接口

* `UserService.java`

```java
public interface UserService {

    UserDO selectById(Long id);

    List<UserDO> selectAll();
}
```

* `UserServiceImpl.java`

```java
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    public UserDO selectById(Long id) {
        return userMapper.selectById(id);
    }

    public List<UserDO> selectAll() {
        return userMapper.selectAll();
    }
}
```

### 2.5 控制层测试接口

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/selectOne")
    public UserDO selectOne(Long id) {
        return userService.selectById(id);
    }

    @GetMapping("/selectAll")
    public List<UserDO> selectAll() {
        return userService.selectAll();
    }
}
```

### 2.6 启动类添加`mapper.xml`扫描

```
@SpringBootApplication
@MapperScan("cn.van.mybatis.druid.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 三、Druid配置类

这里需要注意的一两行配置：

```java      servletRegistrationBean.addInitParameter("loginUsername", "admin");
servletRegistrationBean.addInitParameter("loginPassword", "123456");
```

用于后面的Druid控制台访问的登陆。


```java
@Configuration
@Slf4j
public class DruidConfig {

    private static final String DB_PREFIX = "spring.datasource";
    /**
     *  主要实现WEB监控的配置处理
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        log.info("enter DruidConfig ............");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // IP白名单，多个用逗号分割， 如果allow没有配置或者为空，则允许所有访问
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1,192.168.2.25");
        // IP黑名单(共同存在时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
        //控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //所有请求进行监控处理
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    /**
     * 注入yml中配置的属性
     */
    @Data
    @ConfigurationProperties(prefix = DB_PREFIX)
    private class IDataSourceProperties {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private int initialSize;
        private int minIdle;
        private int maxActive;
        private int maxWait;
        private int timeBetweenEvictionRunsMillis;
        private int minEvictableIdleTimeMillis;
        private String validationQuery;
        private boolean testWhileIdle;
        private boolean testOnBorrow;
        private boolean testOnReturn;
        private boolean poolPreparedStatements;
        private int maxPoolPreparedStatementPerConnectionSize;
        private String filters;
        private String connectionProperties;

        @Bean     //声明其为Bean实例
        @Primary  //在同样的DataSource中，首先使用被标注的DataSource
        public DataSource dataSource() {
            DruidDataSource datasource = new DruidDataSource();
            datasource.setUrl(url);
            datasource.setUsername(username);
            datasource.setPassword(password);
            datasource.setDriverClassName(driverClassName);

            //configuration
            datasource.setInitialSize(initialSize);
            datasource.setMinIdle(minIdle);
            datasource.setMaxActive(maxActive);
            datasource.setMaxWait(maxWait);
            datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            datasource.setValidationQuery(validationQuery);
            datasource.setTestWhileIdle(testWhileIdle);
            datasource.setTestOnBorrow(testOnBorrow);
            datasource.setTestOnReturn(testOnReturn);
            datasource.setPoolPreparedStatements(poolPreparedStatements);
            datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
            try {
                datasource.setFilters(filters);
            } catch (SQLException e) {
                System.err.println("druid configuration initialization filter: " + e);
            }
            datasource.setConnectionProperties(connectionProperties);
            return datasource;
        }
    }

}
```

## 四、测试

### 4.1 访问数据库

1. [http://localhost:8082/user/selectAll](http://localhost:8082/user/selectAll)
1. [http://localhost:8082/user/selectOne?id=1](http://localhost:8082/user/selectOne?id=1)

如果数据返回，证明配置Mybatis成功！

### 4.2 Druid 监控

进入[http://localhost:8082/druid/login.html](http://localhost:8082/druid/login.html)登陆刚设置的用户名和密码即可访问Druid面板，更多详见官方文档[https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98](https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)

