# SpringBoot 配置AOP自定义注解：打印入参出参

> 之前分享过[SpringBoot 配置 AOP 记录日志](https://blog.csdn.net/weixin_42036952/article/details/88063210#23_AopServiceTest_114) 可以通过切面的方式打印控制器层的日志，但是可能存在以下问题：
> 
> 1. 不够灵活，由于是以所有 Controller 方法中的方法为切面，也就是说切死了，如果说我们不想让某个接口打印出入参日志，就办不到了；
> 
> 1. Controller 包层级过深时，导致很多包下的接口切不到。
> 
> 所以，就想通过指定某些方法打印日志，即通过自定义注解打印日志。


## 一、添加所需依赖

```
<dependencies>
    <!-- aop 依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    <!-- web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- json -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.57</version>
    </dependency>
</dependencies>
```

## 二、自定义日志注解 `WebLog`

> 详细介绍见注解


```java
@Retention(RetentionPolicy.RUNTIME) // 什么时候使用该注解，我们定义为运行时；
@Target({ElementType.METHOD}) //用于什么地方，我们定义为作用于方法上；
@Documented //注解是否将包含在 JavaDoc 中
public @interface WebLog {
    String description() default "";
}
```

## 三、配置 AOP 切面

> 部分常见注解说明，本文用到的，见备注说明
> 
> 1. @Aspect：声明该类为一个注解类；
> 
> 1. @Pointcut：定义一个切点，后面跟随一个表达式，表达式可以定义为切某个注解，也可以切某个 package 下的方法；
> 
> 1. @Before: 在切点之前，织入相关代码；
> 
> 1. @After: 在切点之后，织入相关代码;
> 
> 1. @AfterReturning: 在切点返回内容后，织入相关代码，一般用于对返回值做些加工处理的场景；
> 
> 1. @AfterThrowing: 用来处理当织入的代码抛出异常后的逻辑处理;
> 
> 1. @Around: 环绕，可以在切入点前后织入代码，并且可以自由的控制何时执行切点；


```java
@Aspect // 申明这是一个切面
@Component // 使其被Spring扫描管理
public class WebLogAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** 切入点 **/
    @Pointcut("@annotation(cn.van.annotation.annotation.WebLog)")
    public void webLogPointcut(){}
    // 在执行方法前后调用Advice，相当于@Before和@AfterReturning一起做的事儿；
    @Around("webLogPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
        Long startTime = System.currentTimeMillis();
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取注解的属性值
        WebLog webLog = ((MethodSignature)pjp.getSignature()).getMethod().getAnnotation(WebLog.class);
        logger.info("请求方法描述：" + webLog.description() );
        logger.info("请求开始时间："+ LocalDateTime.now());
        // 记录下请求内容
        logger.info("请求Url : " + request.getRequestURL().toString());
        logger.info("请求方式 : " + request.getMethod());
        logger.info("请求ip : " + request.getRemoteAddr());
        logger.info("请求方法 : " + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        logger.info("请求参数 : " + Arrays.toString(pjp.getArgs()));
        Object obj = pjp.proceed();
        logger.info("请求结束时间："+ LocalDateTime.now());
        logger.info("请求返回 : " + JSON.toJSONString(obj));
        logger.info("日志耗时：{} ms",(System.currentTimeMillis() - startTime));
        return obj;
    }

}
```

## 四、测试及使用

### 4.1 建立启动类

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 4.2 新建controller测试

只需在我们需要打印日志的方法上添加 `@WebLog(description = "这里是方法描述")` 便可指定该方法日志被打印。

```java
@RestController
@RequestMapping("/")
public class WebLogTestController {

    /**
     * 一般请求，不需打印日志
     * @return
     */
    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello";
    }

    /**
     * 需要打印日志的请求
     * @param str
     * @return
     */
    @GetMapping("/webLog")
    @WebLog(description = "这里是方法描述")
    public String webLogTest(String str) {
        return "成功返回:" + str;
    }
}
```

### 4.3 启动项目，测试

1. 访问[http://localhost:8080/sayHello](http://localhost:8080/sayHello)，成功返回结果:`Hello`且控制台无日志；
2. 访问[http://localhost:8080/webLog?str=%22nice%22](http://localhost:8080/webLog?str=%22nice%22)，返回结果:`成功返回:"nice"`，日志显示如下：

```
2019-06-11 18:26:15.732  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 请求方法描述：这里是方法描述
2019-06-11 18:26:15.741  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 请求开始时间：2019-06-11T18:26:15.740
2019-06-11 18:26:15.741  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 请求Url : http://localhost:8080/webLog
2019-06-11 18:26:15.741  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 请求方式 : GET
2019-06-11 18:26:15.741  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 请求ip : 0:0:0:0:0:0:0:1
2019-06-11 18:26:15.742  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 请求方法 : cn.van.annotation.web.controller.WebLogTestController.webLogTest
2019-06-11 18:26:15.742  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 请求参数 : ["nice"]
2019-06-11 18:26:15.749  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 请求结束时间：2019-06-11T18:26:15.749
2019-06-11 18:26:15.802  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 请求返回 : "成功返回:\"nice\""
2019-06-11 18:26:15.803  INFO 5207 --- [nio-8080-exec-1] cn.van.annotation.aspect.WebLogAspect    : 日志耗时：73 ms
```

## 五、扩展

### 5.1 指定环境下该注解可用

如果不想在生产环境中打印日志，只想在开发环境或者测试环境中使用，只需为切面(`WebLogAspect `)添加`@Profile`就可以了，如下

```java
@Aspect
@Component
//@Profile("dev") //指定dev环境该注解有效，其他环境无效
public class WebLogAspect {
	...
}
```

### 5.2 多切面指定优先级

实际情况下我们的服务中可能不止定义了一个切面，比如说我们针对 Web 层的接口，不仅要打印日志，还要校验 token；那么，我们可以通过 `@Order(i)` 注解来指定优先级。


> 规律：
> 
> 1. 在切点之前， @Order 从小到大被执行，也就是说越小的优先级越高；
> 2. 在切点之后， @Order 从大到小被执行，也就是说越大的优先级越高；

### 5.3 源码地址

[SpringBoot 配置AOP自定义注解：打印入参出参](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-annotation/webLog)