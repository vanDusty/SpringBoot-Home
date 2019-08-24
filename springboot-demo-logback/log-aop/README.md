# SpringBoot 配置 AOP 打印日志

> 在项目开发中，日志系统是必不可少的，用`AOP`在Web的请求做入参和出参的参数打印，同时对异常进行日志打印，避免重复的手写日志，完整案例见文末源码。

## 一、`Spring AOP`的介绍

> `AOP`(Aspect-Oriented Programming，面向切面编程)，它利用一种"横切"的技术，将那些多个类的共同行为封装到一个可重用的模块。便于减少系统的重复代码，降低模块之间的耦合度，并有利于未来的可操作性和可维护性。

`AOP`中有以下概念：

- `Aspect`（切面）：声明类似于`Java`中的类声明，在`Aspect`中会包含一些Pointcut及相应的Advice。
- `Joint point`（连接点）：表示在程序中明确定义的点。包括方法的调用、对类成员的访问等。
- `Pointcut`（切入点）：表示一个组`Joint point`，如方法名、参数类型、返回类型等等。
- `Advice`（通知）：`Advice`定义了在`Pointcut`里面定义的程序点具体要做的操作，它通过(`before`、`around`、`after`(`return`、`throw`)、`finally`来区别实在每个`Joint` `point`之前、之后还是执行 前后要调用的代码。
- `Before`：在执行方法前调用`Advice`，比如请求接口之前的登录验证。
- `Around`：在执行方法前后调用`Advice`，这是最常用的方法。
- `After`：在执行方法后调用`Advice`，`after`、`return`是方法正常返回后调用，`after\throw`是方法抛出异常后调用。
- `Finally`：方法调用后执行`Advice`，无论是否抛出异常还是正常返回。
- `AOP proxy`：`AOP proxy`也是`Java`对象，是由`AOP`框架创建，用来完成上述动作，`AOP`对象通常可以通过`JDK dynamic proxy`完成，或者使用`CGLIb`完成。
- `Weaving`：实现上述切面编程的代码织入，可以在编译时刻，也可以在运行时刻，`Spring`和其它大多数Java框架都是在运行时刻生成代理。

## 二、项目示例


当然，在使用该案例之前，如果需要了解日志配置相关，可参考 [SpringBoot 异步输出 Logback 日志](SpringBoot 异步输出 Logback 日志)， 本文就不再概述了。

### 2.1 在pom引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    <!-- 分析客户端信息的工具类-->
    <dependency>
        <groupId>eu.bitwalker</groupId>
        <artifactId>UserAgentUtils</artifactId>
        <version>1.20</version>
    </dependency>
    <!-- lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>1.8.4</scope>
    </dependency>
</dependencies>
```

### 2.2 `Controller` 切面：`WebLogAspect`

```java 
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    /**
     * 进入方法时间戳
     */
    private Long startTime;
    /**
     * 方法结束时间戳(计时)
     */
    private Long endTime;

    public WebLogAspect() {
    }


    /**
     * 定义请求日志切入点，其切入点表达式有多种匹配方式,这里是指定路径
     */
    @Pointcut("execution(public * cn.van.log.aop.controller.*.*(..))")
    public void webLogPointcut() {
    }

    /**
     * 前置通知：
     * 1. 在执行目标方法之前执行，比如请求接口之前的登录验证;
     * 2. 在前置通知中设置请求日志信息，如开始时间，请求参数，注解内容等
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取请求头中的User-Agent
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        //打印请求的内容
        startTime = System.currentTimeMillis();
        log.info("请求开始时间：{}" + LocalDateTime.now());
        log.info("请求Url : {}" + request.getRequestURL().toString());
        log.info("请求方式 : {}" + request.getMethod());
        log.info("请求ip : {}" + request.getRemoteAddr());
        log.info("请求方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("请求参数 : {}" + Arrays.toString(joinPoint.getArgs()));
        // 系统信息
        log.info("浏览器：{}", userAgent.getBrowser().toString());
        log.info("浏览器版本：{}", userAgent.getBrowserVersion());
        log.info("操作系统: {}", userAgent.getOperatingSystem().toString());
    }

    /**
     * 返回通知：
     * 1. 在目标方法正常结束之后执行
     * 1. 在返回通知中补充请求日志信息，如返回时间，方法耗时，返回值，并且保存日志信息
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "webLogPointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        endTime = System.currentTimeMillis();
        log.info("请求结束时间：{}" + LocalDateTime.now());
        log.info("请求耗时：{}" + (endTime - startTime));
        // 处理完请求，返回内容
        log.info("请求返回 : {}" + ret);
    }

    /**
     * 异常通知：
     * 1. 在目标方法非正常结束，发生异常或者抛出异常时执行
     * 1. 在异常通知中设置异常信息，并将其保存
     *
     * @param throwable
     */
    @AfterThrowing(value = "webLogPointcut()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        // 保存异常日志记录
        log.error("发生异常时间：{}" + LocalDateTime.now());
        log.error("抛出异常：{}" + throwable.getMessage());
    }
}
```



### 2.3 编写测试

```java 
@RestController
@RequestMapping("/log")
public class LogbackController {

    /**
     * 测试正常请求
     * @param msg
     * @return
     */
    @GetMapping("/{msg}")
    public String getMsg(@PathVariable String msg) {
        return "request msg : " + msg;
    }

    /**
     * 测试抛异常
     * @return
     */
    @GetMapping("/test")
    public String getException(){
        // 故意造出一个异常
        Integer.parseInt("abc123");
        return "success";
    }
}
```

### 2.3 `@Before`和`@AfterReturning`部分也可使用以下代码替代

```java
    /**
     * 在执行方法前后调用Advice，这是最常用的方法，相当于@Before和@AfterReturning全部做的事儿
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("webLogPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取请求头中的User-Agent
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        //打印请求的内容
        startTime = System.currentTimeMillis();
        log.info("请求Url : {}" , request.getRequestURL().toString());
        log.info("请求方式 : {}" , request.getMethod());
        log.info("请求ip : {}" , request.getRemoteAddr());
        log.info("请求方法 : " , pjp.getSignature().getDeclaringTypeName() , "." , pjp.getSignature().getName());
        log.info("请求参数 : {}" , Arrays.toString(pjp.getArgs()));
    // 系统信息
        log.info("浏览器：{}", userAgent.getBrowser().toString());
        log.info("浏览器版本：{}",userAgent.getBrowserVersion());
        log.info("操作系统: {}", userAgent.getOperatingSystem().toString());
        // pjp.proceed()：当我们执行完切面代码之后，还有继续处理业务相关的代码。proceed()方法会继续执行业务代码，并且其返回值，就是业务处理完成之后的返回值。
        Object ret = pjp.proceed();
        log.info("请求结束时间："+ LocalDateTime.now());
        log.info("请求耗时：{}" , (System.currentTimeMillis() - startTime));
        // 处理完请求，返回内容
        log.info("请求返回 : " , ret);
        return ret;
    }
```

## 三、 测试

启动测试类进行测试，本案例提供了正常运行的日志打印和异常的日志打印。

### 3.1 请求入口`LogbackController.java`

```java
@RestController
@RequestMapping("/log")
public class LogbackController {

    /**
     * 测试正常请求
     * @param msg
     * @return
     */
    @GetMapping("/normal/{msg}")
    public String getMsg(@PathVariable String msg) {
        return msg;
    }

    /**
     * 测试抛异常
     * @return
     */
    @GetMapping("/exception/{msg}")
    public String getException(@PathVariable String msg){
        // 故意造出一个异常
        Integer.parseInt("abc123");
        return msg;
    }
}
```

#### 3.2 测试正常请求

打开浏览器，访问[http://localhost:8082/log/normal/hello](http://localhost:8082/log/normal/hello)

日志打印如下：

```xml
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [65] [INFO ] 请求开始时间：2019-02-24T22:37:50.892
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [66] [INFO ] 请求Url : http://localhost:8082/log/normal/hello
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [67] [INFO ] 请求方式 : GET
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [68] [INFO ] 请求ip : 0:0:0:0:0:0:0:1
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [69] [INFO ] 请求方法 : 
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [70] [INFO ] 请求参数 : [hello]
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [72] [INFO ] 浏览器：CHROME
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [73] [INFO ] 浏览器版本：76.0.3809.100
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [74] [INFO ] 操作系统: MAC_OS_X
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [88] [INFO ] 请求结束时间：2019-02-24T22:37:50.901
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [89] [INFO ] 请求耗时：14
[2019-02-24 22:37:50.050] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-1] [91] [INFO ] 请求返回 : hello
```

#### 3.3 测试异常情况

访问：[http://localhost:8082/log/exception/hello](http://localhost:8082/log/exception/hello)

```xml
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [65] [INFO ] 请求开始时间：2019-02-24T22:39:57.728
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [66] [INFO ] 请求Url : http://localhost:8082/log/exception/hello
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [67] [INFO ] 请求方式 : GET
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [68] [INFO ] 请求ip : 0:0:0:0:0:0:0:1
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [69] [INFO ] 请求方法 : 
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [70] [INFO ] 请求参数 : [hello]
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [72] [INFO ] 浏览器：CHROME
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [73] [INFO ] 浏览器版本：76.0.3809.100
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [74] [INFO ] 操作系统: MAC_OS_X
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [104] [ERROR] 发生异常时间：2019-02-24T22:39:57.731
[2019-02-24 22:39:57.057] [cn.van.log.aop.aspect.WebLogAspect] [http-nio-8082-exec-9] [105] [ERROR] 抛出异常：For input string: "abc123"
[2019-02-24 22:39:57.057] [org.apache.juli.logging.DirectJDKLog] [http-nio-8082-exec-9] [175] [ERROR] Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.NumberFormatException: For input string: "abc123"] with root cause
java.lang.NumberFormatException: For input string: "abc123"
```

## 三、源码

[Github 示例代码](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-log/log-aop)