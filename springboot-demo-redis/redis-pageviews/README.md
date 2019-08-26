# SpringBoot：高并发下浏览量入库设计


## 一、背景

> 文章浏览量统计，low的做法是：用户每次浏览，前端会发送一个`GET`请求获取一篇文章详情时，会把这篇文章的浏览量`+1`，存进数据库里。

### 1.1 这么做，有几个问题：

1. 在GET请求的业务逻辑里进行了数据的写操作！
2. 并发高的话，数据库压力太大；
3. 同时，如果文章做了缓存和搜索引擎如`ElasticSearch`的存储，同步更新缓存和`ElasticSearch `更新同步更新太耗时，不更新就会导致数据不一致性。

### 1.2 解决方案


- `HyperLogLog`

`HyperLogLog`是`Probabilistic data Structures`的一种，这类数据结构的基本大的思路就是使用统计概率上的算法，牺牲数据的精准性来节省内存的占用空间及提升相关操作的性能。

- 设计思路

 1. 为保证真实的博文浏览量，根据用户访问的`ip`和文章`id`,进行唯一校验，即同一个用户多次访问同一篇文章，改文章访问量只增加1；
 2. 将用户的浏览量用`opsForHyperLogLog().add(key,value)`的存储在`Redis`中，在半夜浏览量低的时候，通过定时任务，将浏览量更新至数据库中。


## 二、 手把手实现

### 2.1 项目配置

- `sql`

```sql
DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` varchar(1024) NOT NULL COMMENT '内容',
  `url` varchar(100) NOT NULL COMMENT '地址',
	`views` bigint(20) NOT NULL COMMENT '浏览量',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO article VALUES(1,'测试文章','content','url',10,NULL);
```

> 插入了一条数据，并设计访问量已经为10了，便于测试。

- 项目依赖`pom.xml`

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
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
<!-- redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.0</version>
</dependency>
<!-- lombok-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

- `application.yml`

```yml
spring:
  # 数据库配置
  datasource:
    url: jdbc:mysql://47.98.178.84:3306/dev
    username: dev
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  redis:
    host: 47.98.178.84
    port: 6379
    database: 1
    password: password
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


# mybatis
mybatis:
  mapper-locations: classpath:mapper/*.xml
#  type-aliases-package: cn.van.redis.view.entity
```
 
### 2.2 浏览量的切面设计
 
 - 自定义一个注解，用于新增文章浏览量到Redis中

 ```java
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageView {
    /**
     * 描述
     */
    String description()  default "";
}
 ```
 
 - 切面处理

 ```
 @Aspect
@Configuration
@Slf4j
public class PageViewAspect {

    @Autowired
    private RedisUtils redisUtil;

    /**
     * 切入点
     */
    @Pointcut("@annotation(cn.van.redis.view.annotation.PageView)")
    public void PageViewAspect() {

    }

    /**
     * 切入处理
     * @param joinPoint
     * @return
     */
    @Around("PageViewAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        Object[] object = joinPoint.getArgs();
        Object articleId = object[0];
        log.info("articleId:{}", articleId);
        Object obj = null;
        try {
            String ipAddr = IpUtils.getIpAddr();
            log.info("ipAddr:{}", ipAddr);
            String key = "articleId_" + articleId;
            // 浏览量存入redis中
            Long num = redisUtil.add(key,ipAddr);
            if (num == 0) {
                log.info("该ip:{},访问的浏览量已经新增过了", ipAddr);
            }
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }
}
 ```
 
 - 工具类`RedisUtils.java`

 ```java
 @Component
public  class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        redisTemplate.delete(key[0]);
    }

    /**
     * 计数
     * @param key
     * @param value
     */
    public Long add(String key, Object... value) {
        return redisTemplate.opsForHyperLogLog().add(key,value);
    }
    /**
     * 获取总数
     * @param key
     */
    public Long size(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

}
 ```
 
 - 工具类 `IpUtils.java`

 > 该工具类我在`Mac`下测试没问题，`Windows`下如果有问题，请反馈给我
 
 ```java
 @Slf4j
public class IpUtils {

    public static String getIpAddr() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            log.info("获取到的ip地址：{}", ip.getHostAddress());
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取ip地址失败，{}",e);
        }
        return null;
    }
}
 ```
 
### 2.3 同步任务`ArticleViewTask.java`

> `ArticleService.java`里面的代码比较简单，详见文末源码。

```java
@Component
@Slf4j
public class ArticleViewTask {

    @Resource
    private RedisUtils redisUtil;
    @Resource
    ArticleService articleService;

	// 每天凌晨一点执行
    @Scheduled(cron = "0 0 1 * * ? ")
    @Transactional(rollbackFor=Exception.class)
    public void createHyperLog() {
        log.info("浏览量入库开始");

        List<Long> list = articleService.getAllArticleId();
        list.forEach(articleId ->{
            // 获取每一篇文章在redis中的浏览量，存入到数据库中
            String key  = "articleId_"+articleId;
            Long view = redisUtil.size(key);
            if(view>0){
                ArticleDO articleDO = articleService.getById(articleId);
                Long views = view + articleDO.getViews();
                articleDO.setViews(views);
                int num = articleService.updateArticleById(articleDO);
                if (num != 0) {
                    log.info("数据库更新后的浏览量为：{}", views);
                    redisUtil.del(key);
                }
            }
        });
        log.info("浏览量入库结束");
    }

}
```

  
### 2.4 测试接口`PageController.java`

```java
@RestController
@Slf4j
public class PageController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisUtils redisUtil;

    /**
     * 访问一篇文章时，增加其浏览量:重点在的注解
     * @param articleId：文章id
     * @return
     */
    @PageView
    @RequestMapping("/{articleId}")
    public String getArticle(@PathVariable("articleId") Long articleId) {
        try{
            ArticleDO blog = articleService.getById(articleId);
            log.info("articleId = {}", articleId);
            String key = "articleId_"+articleId;
            Long view = redisUtil.size(key);
            log.info("redis 缓存中浏览数：{}", view);
            //直接从缓存中获取并与之前的数量相加
            Long views = view + blog.getViews();
            log.info("文章总浏览数：{}", views);
        } catch (Throwable e) {
            return  "error";
        }
        return  "success";
    }
}
```

> 这里，具体的`Service`中的方法因为都被我放在`Controller`中处理了，所以就是剩下简单的`Mapper`调用了，这里就不浪费时间了，详见文末源码。（按理说，这些逻辑处理，应该放在`Service`处理的，请按实际情况优化）
> 


## 三、 测试

启动项目，测试访问量，先请求[http://localhost:8080/1](http://localhost:8080/1),日志打印如下：

```xml
2019-03-2623:50:50.047  INFO 2970 --- [nio-8080-exec-1]  cn.van.redis.view.aspect.PageViewAspect  : articleId:1
2019-03-2623:50:50.047  INFO 2970 --- [nio-8080-exec-1] cn.van.redis.view.utils.IpUtils          : 获取到的ip地址：192.168.1.104
2019-03-2623:50:50.047  INFO 2970 --- [nio-8080-exec-1] cn.van.redis.view.aspect.PageViewAspect  : ipAddr:192.168.1.104
2019-03-2623:50:50.139  INFO 2970 --- [nio-8080-exec-1] io.lettuce.core.EpollProvider            : Starting without optional epoll library
2019-03-2623:50:50.140  INFO 2970 --- [nio-8080-exec-1] io.lettuce.core.KqueueProvider           : Starting without optional kqueue library
2019-03-2623:50:50.349  INFO 2970 --- [nio-8080-exec-1] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2019-03-2623:50:50.833  INFO 2970 --- [nio-8080-exec-1] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2019-03-2623:50:50.872  INFO 2970 --- [nio-8080-exec-1] c.v.r.v.web.controller.PageController    : articleId = 1
2019-03-2623:50:50.899  INFO 2970 --- [nio-8080-exec-1] c.v.r.v.web.controller.PageController    : redis 缓存中浏览数：1
2019-03-2623:50:50.900  INFO 2970 --- [nio-8080-exec-1] c.v.r.v.web.controller.PageController    : 文章总浏览数：11
```

观察一下，数据库，访问量确实没有增加，本机再次访问，发现，日志打印如下:

```xml 
2019-03-2623:51:14.658  INFO 2970 --- [nio-8080-exec-3] 
cn.van.redis.view.aspect.PageViewAspect  : articleId:1
2019-03-2623:51:14.658  INFO 2970 --- [nio-8080-exec-3] cn.van.redis.view.utils.IpUtils          : 获取到的ip地址：192.168.1.104
2019-03-2623:51:14.658  INFO 2970 --- [nio-8080-exec-3] cn.van.redis.view.aspect.PageViewAspect  : ipAddr:192.168.1.104
2019-03-2623:51:14.692  INFO 2970 --- [nio-8080-exec-3] cn.van.redis.view.aspect.PageViewAspect  : 该ip:192.168.1.104,访问的浏览量已经新增过了
2019-03-2623:51:14.752  INFO 2970 --- [nio-8080-exec-3] c.v.r.v.web.controller.PageController    : articleId = 1
2019-03-2623:51:14.760  INFO 2970 --- [nio-8080-exec-3] c.v.r.v.web.controller.PageController    : redis 缓存中浏览数：1
2019-03-2623:51:14.761  INFO 2970 --- [nio-8080-exec-3] c.v.r.v.web.controller.PageController    : 文章总浏览数：11
```


- 定时任务触发，日志打印如下

```xml
2019-03-27 01:00:00.265  INFO 2974 --- [   scheduling-1] cn.van.redis.view.task.ArticleViewTask   : 浏览量入库开始
2019-03-27 01:00:00.448  INFO 2974 --- [   scheduling-1] io.lettuce.core.EpollProvider            : Starting without optional epoll library
2019-03-27 01:00:00.449  INFO 2974 --- [   scheduling-1] io.lettuce.core.KqueueProvider           : Starting without optional kqueue library
2019-03-27 01:00:00.663  INFO 2974 --- [   scheduling-1] cn.van.redis.view.task.ArticleViewTask   : 数据库更新后的浏览量为：11
2019-03-27 01:00:00.682  INFO 2974 --- [   scheduling-1] cn.van.redis.view.task.ArticleViewTask   : 浏览量入库结束
```

观察一下数据库，发现`Redis`中数据库中的浏览量增加到`11`，同时，缓存中的浏览量没了，说明成功！


[Github 示例代码](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-list/cross-domain)

## 四、说明

### 4.1 更多文章

1. [风尘博客](https://www.dustyblog.cn/)
1. [风尘博客-博客园](https://www.cnblogs.com/vandusty)
1. [风尘博客-CSDN](https://blog.csdn.net/weixin_42036952)
1. [风尘博客-SegmentFault](https://segmentfault.com/u/dustyblog)

### 4。2 技术交流

风尘博客公众号：

![风尘博客](https://i.loli.net/2019/08/18/jiXBAot8fEWZd3p.png)