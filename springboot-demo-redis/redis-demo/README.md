# SpringBoot 配置 Redis

> 作为目前最火的的NoSql数据库,Redis现在已成为后台开发人员的标配，本文主要主要介绍`SpringBoot 2.* `和 `Redis` 的整合。


## 一、`StringRedisTemplate``与`RedisTemplate`

### 1.1 两者区别

1. `StringRedisTemplate`继承自`RedisTemplate`;
1. **两者的数据是不共通的**：`StringRedisTemplate`只能管理`StringRedisTemplate`里面的数据，同样，`RedisTemplate`只能管理`RedisTemplate`中的数据；
1. `RedisTemplate `中存取数据都是字节数组，`StringRedisTemplate `中存取数据都是字符串。

### 1.2 `RedisTemplate`中定义了5种数据结构操作

```java 
redisTemplate.opsForValue();　　//操作字符串
redisTemplate.opsForHash();　　 //操作hash
redisTemplate.opsForList();　　 //操作list
redisTemplate.opsForSet();　　  //操作set
redisTemplate.opsForZSet();　 　//操作有序set
```

### 1.3 `StringRedisTemplate`的使用
 
为了演示，本文仅以`SpringBoot`中使用`StringRedisTemplate`为例。

## 二、上手实战

### 2.1 导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.0</version>
</dependency>
```

### 2.2 `application.yml`中`redis`参数配置

```yml
spring:
  redis:
    host: 47.98.178.84
    port: 6379
    database: 0
    password: van12345
    timeout: 60s  # 连接超时时间，2.0 中该参数的类型为Duration，这里在配置的时候需要指明单位
    # 连接池配置，2.0中直接使用jedis或者lettuce配置连接池（使用lettuce，依赖中必须包含commons-pool2包）
    lettuce:
      pool:
        # 最大空闲连接数
        max-idle: 500
        # 最小空闲连接数
        min-idle: 50
        # 等待可用连接的最大时间，负数为不限制
        max-wait:  -1s
        # 最大活跃连接数，负数为不限制
        max-active: -1
```

### 2.3 Redis配置类-RedisConfig

**网上很多这个配置，都是2.0以下版本的，这里的配置适配2.1.1**

```java

/**
 * 〈redis配置类〉<br>
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-21
 * @since 1.0.0
 */
@Configuration
@EnableCaching//开启注解
public class RedisConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cn.van.redis.demo.config = RedisCacheConfiguration.defaultCacheConfig();  // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        cn.van.redis.demo.config = cn.van.redis.demo.config.entryTtl(Duration.ofMinutes(1))     // 设置缓存的默认过期时间，也是使用Duration设置
                .disableCachingNullValues();     // 不缓存空值

        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames =  new HashSet<>();
        cacheNames.add("my-redis-cache1");
        cacheNames.add("my-redis-cache2");

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("my-redis-cache1", cn.van.redis.demo.config);
        configMap.put("my-redis-cache2", cn.van.redis.demo.config.entryTtl(Duration.ofSeconds(120)));

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)     // 使用自定义的缓存配置初始化一个cacheManager
                .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .withInitialCacheConfigurations(configMap)
                .build();
        return cacheManager;
    }
}
```

### 2.4 封装的redis操作String类型工具类-StringCache

> 具体操作说明，见代码注释

```java
@Component
public class StringCache {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     * @param timeout （以秒为单位）
     */
    public void setValue(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    public void delKey(String key) {
        redisTemplate.delete(key);
    }
    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    public boolean existKey(String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     *
     * @param key
     * @return value
     */
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key
     * @return
     */
    public long getRemainingTime(String key) {
        return redisTemplate.getExpire(key);
    }
}
```

### 2.5 测试类

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class StringCacheTest {

    private static final Logger logger = LoggerFactory.getLogger(StringCacheTest.class);

    @Autowired
    private StringCache stringCache;

    /**
     * 测试get/set/delete key
     */
    @Test
    public void setAndGet() {
        stringCache.setValue("name","redis测试");
        String name = stringCache.getValue("name");
        logger.info(name);
        stringCache.delKey("name");
        name = stringCache.getValue("name");
        logger.info(name);
    }

    /**
     * 测试设置有效时长的key
     */
    @Test
    public void getRemainingTime() {
        stringCache.setValue("hello","hello word", 40);
        logger.info("剩余存活时间:{}秒",stringCache.getRemainingTime("hello"));
    }

    /**
     * 测试过了有效时长的key，是否被删除
     */
    @Test
    public void exist() {
        boolean i = stringCache.existKey("hello");
        if (i) {
            logger.info("该键还存在");
            logger.info("剩余存活时间:{}秒",stringCache.getRemainingTime("hello"));
        }else {
            logger.info("该键已过期");
        }
    }

}
```

## 三、 Redis 可视化客户端 - rdm

> 通常情况下，我们可以在命令行下查看 Redis 数据库，但是可视化工具能更真实地让我们看到数据的结构。

这里分享我用的一个简单的客户端，打开[链接](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-redis/file/redis-desktop-manager-0.8.3-2550.dmg)，点击**Download**即可下载。
