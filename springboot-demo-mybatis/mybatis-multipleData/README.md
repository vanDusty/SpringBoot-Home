# SpringBoot 整合Mybatis 多数据源

在业务场景中，随着数据量迅速增长，一个库一个表已经满足不了我们的需求的时候，我们就会考虑分库分表的操作，本文主要介绍SpringBoot + Mybatis 如何实现多数据源，动态数据源切换，可用于读写分离或多库存储。

> 主要的配置说明见代码注释

## 一、 数据库准备

> 为了展示本文多数据源配置的灵活性，本文先配置两个数据库，最后新增第三个数据库，真正实现“零配置扩展”。

1. 新建二个数据库名分别为master,slave1；
2. 分别在master、slave1中创建表和插入测试数据。

* master 库 sql

```sql
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                      `id` bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
                      `user_name` varchar(50) NOT NULL COMMENT '用户名',
                      `user_age` int(3) DEFAULT 0 COMMENT '用户年龄'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '主库用户表';

INSERT INTO `user` VALUES (1, '张三', 27);
```

* slave1 库 sql

```sql

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                      `id` bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
                      `user_name` varchar(50) NOT NULL COMMENT '用户名',
                      `user_age` int(3) DEFAULT 0 COMMENT '用户年龄'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '从库slave1用户表';

INSERT INTO `user` VALUES (2, '李四', 30);
```

## 二、 项目配置

### 2.1 项目依赖

```pom
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>

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

### 2.2 项目配置文件`application.yml`

其中 `master` 数据源一定是要配置，它是我们的默认数据源，其次`cluster`集群中，其他的数据不配置也不会影响程序员运行，如果你想添加新的一个数据源 就在cluster下新增一个数据源即可，其中key为必须项，用于数据源的唯一标识，以及接下来切换数据源的标识。

```
server:
  port: 8084
spring:
  datasource:
    # 主数据库
    master:
      password: master
      url: jdbc:mysql://47.98.178.84:3306/master?useUnicode=true&characterEncoding=UTF-8
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: master
#      type: com.alibaba.druid.pool.DruidDataSource
    # 从数据库集群（可继续扩展)
    cluster:
    - key: slave1
      password: slave_1
      url: jdbc:mysql://47.98.178.84:3306/slave_1?useUnicode=true&characterEncoding=UTF-8
#      idle-timeout: 20000
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: slave_1
#      type: com.alibaba.druid.pool.DruidDataSource
    druid:
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
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: cn.van.mybatis.multipleData.entity
# mybatis sql 日志
logging:
  level:
    cn:
      van:
        mybatis:
          multipleData:
            mapper: debug
```

## 三、 多数据源配置

### 3.1 动态数据源注册`DynamicDataSourceRegister`

SpringBoot 无法为我们自动配置我们刚在配置文件中配置的多个数据源的，所以需要我们实现 `ImportBeanDefinitionRegistrar `接口实现数据源注册，同时实现 `EnvironmentAware` 读取`application.yml`配置。

```java
@Slf4j
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    /**
     * 配置上下文（也可以理解为配置文件的获取工具）
     */
    private Environment evn;

    /**
     * 别名
     */
    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

    /**
     * 由于部分数据源配置不同，所以在此处添加别名，避免切换数据源出现某些参数无法注入的情况
     */
    static {
        aliases.addAliases("url", new String[]{"jdbc-url"});
        aliases.addAliases("username", new String[]{"user"});
    }

    /**
     * 存储我们注册的数据源
     */
    private Map<String, DataSource> customDataSources = new HashMap<String, DataSource>();

    /**
     * 参数绑定工具 springboot2.0新推出
     */
    private Binder binder;

    /**
     * ImportBeanDefinitionRegistrar接口的实现方法，通过该方法可以按照自己的方式注册bean
     *
     * @param annotationMetadata
     * @param beanDefinitionRegistry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        // 获取所有数据源配置
        Map config, defauleDataSourceProperties;
        defauleDataSourceProperties = binder.bind("spring.datasource.master", Map.class).get();
        // 获取数据源类型(我这里都用druid连接池，所以修改下)
        // String typeStr = evn.getProperty("spring.datasource.master.type");
        String typeStr = evn.getProperty("spring.datasource.druid.type");
        // 获取数据源类型
        Class<? extends DataSource> clazz = getDataSourceType(typeStr);
        // 绑定默认数据源参数 也就是主数据源
        DataSource consumerDatasource, defaultDatasource = bind(clazz, defauleDataSourceProperties);
        DynamicDataSourceContextHolder.keys.add("master");
        log.info("注册默认数据源成功");
        // 获取其他数据源配置
        List<Map> configs = binder.bind("spring.datasource.cluster", Bindable.listOf(Map.class)).get();
        // 遍历从数据源
        for (int i = 0; i < configs.size(); i++) {
            config = configs.get(i);
            // 从库也都使用druid，所以也改动一下
            // clazz = getDataSourceType((String) config.get("type"));
            clazz = getDataSourceType(typeStr);
            defauleDataSourceProperties = config;
            // 绑定参数
            consumerDatasource = bind(clazz, defauleDataSourceProperties);
            // 获取数据源的key，以便通过该key可以定位到数据源
            String key = config.get("key").toString();
            customDataSources.put(key, consumerDatasource);
            // 数据源上下文，用于管理数据源与记录已经注册的数据源key
            DynamicDataSourceContextHolder.keys.add(key);
            log.info("注册数据源{}成功", key);
        }
        // bean定义类
        GenericBeanDefinition define = new GenericBeanDefinition();
        // 设置bean的类型，此处DynamicRoutingDataSource是继承AbstractRoutingDataSource的实现类
        define.setBeanClass(DynamicRoutingDataSource.class);
        // 需要注入的参数
        MutablePropertyValues mpv = define.getPropertyValues();
        // 添加默认数据源，避免key不存在的情况没有数据源可用
        mpv.add("defaultTargetDataSource", defaultDatasource);
        // 添加其他数据源
        mpv.add("targetDataSources", customDataSources);
        // 将该bean注册为datasource，不使用springboot自动生成的datasource
        beanDefinitionRegistry.registerBeanDefinition("datasource", define);
        log.info("注册数据源成功，一共注册{}个数据源", customDataSources.keySet().size() + 1);
    }

    /**
     * 通过字符串获取数据源class对象
     *
     * @param typeStr
     * @return
     */
    private Class<? extends DataSource> getDataSourceType(String typeStr) {
        Class<? extends DataSource> type;
        try {
            if (StringUtils.hasLength(typeStr)) {
                // 字符串不为空则通过反射获取class对象
                type = (Class<? extends DataSource>) Class.forName(typeStr);
            } else {
                // 默认为hikariCP数据源，与springboot默认数据源保持一致
                type = HikariDataSource.class;
            }
            return type;
        } catch (Exception e) {
            //无法通过反射获取class对象的情况则抛出异常，该情况一般是写错了，所以此次抛出一个runtimeexception
            throw new IllegalArgumentException("can not resolve class with type: " + typeStr);
        }
    }

    /**
     * 绑定参数，以下三个方法都是参考DataSourceBuilder的bind方法实现的，目的是尽量保证我们自己添加的数据源构造过程与springboot保持一致
     *
     * @param result
     * @param properties
     */
    private void bind(DataSource result, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        // 将参数绑定到对象
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        // 通过类型绑定参数并获得实例对象
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get();
    }

    /**
     * @param clazz
     * @param sourcePath 参数路径，对应配置文件中的值，如: spring.datasource
     * @param <T>
     * @return
     */
    private <T extends DataSource> T bind(Class<T> clazz, String sourcePath) {
        Map properties = binder.bind(sourcePath, Map.class).get();
        return bind(clazz, properties);
    }

    /**
     * EnvironmentAware接口的实现方法，通过aware的方式注入，此处是environment对象
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        log.info("开始注册数据源");
        this.evn = environment;
        // 绑定配置器
        binder = Binder.get(evn);
    }
}
```

1. 先读取master构建默认数据源,然后在构建cluster中的数据源； 
1. 在这里注册完数据源之后，我们需要通过@import注解把我们的数据源注册器导入到Spring中 即在启动类`Application.java`加上如下注解
`@Import(DynamicDataSourceRegister.class)`； 
1. 其中我们用到了一个`DynamicDataSourceContextHolder` 中的静态变量来保存我们已经注册成功的数据源的key以及用`DynamicRoutingDataSource`来通知Spring当前的数据源。

### 3.2 配置数据源上下文`DynamicDataSourceContextHolder`

我们需要新建一个数据源上下文，用于记录当前线程使用的数据源的`key`是什么，以及记录所有注册成功的数据源的`key`的集合。对于线程级别的私有变量，我们用`ThreadLocal`来实现。 

```java
@Slf4j
public class DynamicDataSourceContextHolder {

    /**
     * 存储已经注册的数据源的key
     */
    public static List<String> keys = new ArrayList<>();

    /**
     * 线程级别的私有变量
     */
    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    public static String getDataSourceRouterKey () {
        return HOLDER.get();
    }

    public static void setDataSourceRouterKey (String key) {
        log.info("切换至{}数据源", key);
        HOLDER.set(key);
    }

    /**
     * 设置数据源之前一定要先移除
     */
    public static void removeDataSourceRouterKey () {
        HOLDER.remove();
    }

    /**
     * 判断指定DataSource当前是否存在
     *
     * @param key
     * @return
     */
    public static boolean containsDataSource(String key){
        return keys.contains(key);
    }

}
```

### 3.3 动态数据源路由`DynamicRoutingDataSource`

Spring提供一个接口，名为`AbstractRoutingDataSource`的抽象类，我们只需要重写`determineCurrentLookupKey`方法就可以通知Spring用key获取当前的数据源。 

```java
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DynamicDataSourceContextHolder.getDataSourceRouterKey();
        log.info("当前数据源是：{}", dataSourceName);
        return DynamicDataSourceContextHolder.getDataSourceRouterKey();
    }
}
```

## 四、通过AOP注解实现数据源动态切换

### 4.1 自定义一个注解`DataSource`

> 该注解在对应的类或者方法设置他们的数据源的key，通过Aop拦截并且保存到数据源上下中。 

```java
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    //该值即key值,默认为master
    String value() default "master";
}
```

默认值是`master`,因为我们默认数据源的key是`master`。也可指定到别的库，比如`@DataSource("slave1")`

### 4.2 动态数据源拦截器`DynamicDataSourceAnnotationInterceptor`


```java
@Slf4j
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {



    /**
     * 缓存方法注解值
     */
    private static final Map<Method, String> METHOD_CACHE = new HashMap<Method, String>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String datasource = determineDatasource(invocation);
            if (!DynamicDataSourceContextHolder.containsDataSource(datasource)) {
                log.info("数据源[{}]不存在，使用默认数据源 >", datasource);
            }
            DynamicDataSourceContextHolder.setDataSourceRouterKey(datasource);
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.removeDataSourceRouterKey();
        }
    }

    private String determineDatasource(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (METHOD_CACHE.containsKey(method)) {
            return METHOD_CACHE.get(method);
        } else {
            DataSource ds = method.isAnnotationPresent(DataSource.class) ? method.getAnnotation(DataSource.class)
                    : AnnotationUtils.findAnnotation(method.getDeclaringClass(), DataSource.class);
            METHOD_CACHE.put(method, ds.value());
            return ds.value();
        }
    }

}
```

### 4.3 AOP 切面处理`DynamicDataSourceAnnotationAdvisor `

通过AOP拦截，获取注解上面的value的值key，然后取判断我们注册的keys集合中是否有这个key,如果没有，则使用默认数据源，如果有，则设置上下文中当前数据源的key为注解的value。 

```java
public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private Advice advice;

    private Pointcut pointcut;

    public DynamicDataSourceAnnotationAdvisor(DynamicDataSourceAnnotationInterceptor dynamicDataSourceAnnotationInterceptor) {
        this.advice = dynamicDataSourceAnnotationInterceptor;
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        Pointcut cpc = (Pointcut) new AnnotationMatchingPointcut(DataSource.class, true);
        // 类注解
        Pointcut clpc = (Pointcut) AnnotationMatchingPointcut.forClassAnnotation(DataSource.class);
        // 方法注解
        Pointcut mpc = (Pointcut) AnnotationMatchingPointcut.forMethodAnnotation(DataSource.class);
        return new ComposablePointcut(cpc).union(clpc).union(mpc);
    }
}
```

1. 继承AbstractPointcutAdvisor 类似于使用@Aspect注解
1. 实现BeanFactoryAware 接口的bean其实是希望知道自己属于哪一个BeanFactory

### 4.4 启动类

```java
@Import(DynamicDataSourceRegister.class)
@MapperScan("cn.van.mybatis.multipledata.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class Application {
    @Bean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor() {
        return new DynamicDataSourceAnnotationAdvisor(new DynamicDataSourceAnnotationInterceptor());
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 五、 测试

### 5.1 新建用户实体`User`

```java
@Data
public class User {
    private Long id;

    private String userName;

    private Integer userAge;

}
```

### 5.2 Mapper 接口及xml

* UserMapper.java

```java
public interface UserMapper {

    /**
     * 查询所有用户信息
     * @return
     */
    List<User> selectAll();
}
```

* UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.van.mybatis.multipledata.mapper.UserMapper">

    <resultMap id="BaseResultMap" type="cn.van.mybatis.multipledata.entity.User">
        <id column="id" jdbcType="BIGINT" property="id" />
        <result column="user_name" jdbcType="VARCHAR" property="userName" />
        <result column="user_age" jdbcType="INTEGER" property="userAge" />
    </resultMap>
    <sql id="Base_Column_List">
        id, user_name, user_age
    </sql>


    <select id="selectAll" resultMap="BaseResultMap">
        select <include refid="Base_Column_List"/>
        from user
    </select>

</mapper>
```

### 5.3 业务层及其实现

* UserService.java

```java
public interface UserService {
    /**
     * 主库（master）的全部用户数据
     * @return
     */
    List<User> selectMasterAll();

    /**
     * 从库(slave1)全部用户数据
     * @return
     */
    List<User> selectSlave1();

}
```

* UserServiceImpl.java

```java
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;
    @Override
    public List<User> selectMasterAll() {
        return userMapper.selectAll();
    }

    @DataSource("slave1")
    @Override
    public List<User> selectSlave1() {
        return userMapper.selectAll();
    }
}
```


### 5.4 测试类

```java
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMapperTest {

    @Resource
    private UserService userService;

    /**
     * 测试主库
     */
    @Test
    public void selectMaster() {
       List<User> users =  userService.selectMasterAll();
            users.forEach(user -> {
                System.out.println("id:" + user.getId());
                System.out.println("name:" + user.getUserName());
                System.out.println("password:" + user.getUserAge());
            });
    }

    /**
     * 测试从库slave1
     */
    @Test
    public void selectSlave1() {
        List<User> users =  userService.selectSlave1();
        users.forEach(user -> {
            System.out.println("id:" + user.getId());
            System.out.println("name:" + user.getUserName());
            System.out.println("password:" + user.getUserAge());
        });
    }
}
```

至此，demo即将结束。同时，为了验证一开始说的该方法扩展性很好，这里演示一下新增一个数据源的查询，如下：

### 5.5 新增一个数据源的配置 

* 在项目配置文件中添加新增的数据库配置，例如：

```yml
- key: slave2
      password: slave2
      url: jdbc:mysql://47.98.178.84:3306/slave2?useUnicode=true&characterEncoding=UTF-8
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: slave2
```

* `UserService.java` 新增一个查询方法

```java
/**
 * 从库(slave2)全部用户数据
 * @return
 */
List<User> selectSlave2();
```

* `UserServiceImpl.java` 实现该方法

```java
@DataSource("slave2")
@Override
public List<User> selectSlave2() {
    return userMapper.selectAll();
}
```

> 其实我们发现，新增一个数据源，唯一要做的，无非就是在操作该数据源的代码方法上加上`@DataSource("key")`的注解即可，是不是太简单了。
> 


### 5.6 待优化部分

1. 在配置Druid 连接池的时候，数据源总是指定失败，所以暂时先把Druid 配置去掉了，导致无法使用Druid进行监控。