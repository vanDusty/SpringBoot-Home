# SpringBoot 配置 AOP 记录日志

> 在项目开发中，日志系统是必不可少的，然而在项目中处理的一个Web的请求及其业务的处理，这些操作不需要我们在相应的方法中一个一个的去实现，这样也不利于维护。我们可以借助`Spring`的`AOP`来更加优雅的实现。接下来就介绍一下在`Spring Boot`中的实现。

## 一、`Spring AOP`的介绍

> `AOP`(Aspect-Oriented Programming，面向切面编程)，它利用一种"横切"的技术，将那些多个类的共同行为封装到一个可重用的模块。便于减少系统的重复代码，降低模块之间的耦合度，并有利于未来的可操作性和可维护性。

`AOP`中有以下概念：

- Aspect（切面）：声明类似于Java中的类声明，在Aspect中会包含一些Pointcut及相应的Advice。
- Joint point（连接点）：表示在程序中明确定义的点。包括方法的调用、对类成员的访问等。
- Pointcut（切入点）：表示一个组Joint point，如方法名、参数类型、返回类型等等。
- Advice（通知）：Advice定义了在Pointcut里面定义的程序点具体要做的操作，它通过(before、around、after(return、throw)、finally来区别实在每个Joint point之前、之后还是执行 前后要调用的代码。
- Before：在执行方法前调用Advice，比如请求接口之前的登录验证。
- Around：在执行方法前后调用Advice，这是最常用的方法。
- After：在执行方法后调用Advice，after return是方法正常返回后调用，after throw是方法抛出异常后调用。
- Finally：方法调用后执行Advice，无论是否抛出异常还是正常返回。
- AOP proxy：AOP proxy也是Java对象，是由AOP框架创建，用来完成上述动作，AOP对象通常可以通过JDK dynamic proxy完成，或者使用CGLIb完成。
- Weaving：实现上述切面编程的代码织入，可以在编译时刻，也可以在运行时刻，Spring和其它大多数Java框架都是在运行时刻生成代理。

## 二、在Spring Boot中使用AOP

> 博主本案例的效果是用AOP在service层做入参和出参的参数打印，同时对异常进行日志打印，避免重复的手写日志，完整案例见文末源码。

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

### 2.2 然后编写切面类：`WebLogAspect`

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

1. `@Aspect`:申明这是一个切面类；
2. `@Component`：声明了这个类将作为Spring的组件，会被加载到Spring容器中;
3. `@Pointcut`：定义请求日志切入点，其切入点表达式有多种匹配方式,这里是指定路径;
4. `@Before`:前置通知,在执行目标方法之前执行，比如请求接口之前的登录验证;
5. `@AfterReturning`:返回通知,在目标方法正常结束之后执行;
6. `@AfterThrowing`:异常通知,在目标方法非正常结束，发生异常或者抛出异常时执行;
7. `@Around`:在执行方法前后调用Advice，这是最常用的方法，相当于@Before和@AfterReturning一起做的事儿；
8. `pjp.proceed()`：当我们执行完切面代码之后，还有继续处理业务相关的代码。p`roceed()`方法会继续执行业务代码，并且其返回值，就是业务处理完成之后的返回值。

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

### 2.4 测试

启动测试类进行测试，本案例提供了正常运行的日志打印和异常的日志打印。

#### 2.4.1 测试正常请求

打开浏览器，访问[http://localhost:8080/log/hello](http://localhost:8080/log/hello)

日志打印如下：

```xml
2019-08-06 18:10:30.140  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 请求开始时间：2019-08-06T18:10:30.140
2019-08-06 18:10:30.140  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 请求Url : http://localhost:8080/log/hello
2019-08-06 18:10:30.140  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 请求方式 : GET
2019-08-06 18:10:30.140  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 请求ip : 0:0:0:0:0:0:0:1
2019-08-06 18:10:30.140  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 请求方法 : 
2019-08-06 18:10:30.140  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 请求参数 : [hello]
2019-08-06 18:10:30.140  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 浏览器：CHROME
2019-08-06 18:10:30.140  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 浏览器版本：76.0.3809.87
2019-08-06 18:10:30.140  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 操作系统: MAC_OS_X
2019-08-06 18:10:30.141  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 请求结束时间：2019-08-06T18:10:30.141
2019-08-06 18:10:30.141  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 请求耗时：2
2019-08-06 18:10:30.141  INFO 15513 --- [nio-8080-exec-3] cn.van.log.aop.aspect.WebLogAspect       : 请求返回 : request msg : hello
```

#### 2.4.2 测试异常情况

访问：[http://localhost:8080/log/exception](http://localhost:8080/log/exception)

```xml
2019-08-06 18:09:30.884  INFO 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 请求开始时间：2019-08-06T18:09:30.884
2019-08-06 18:09:30.885  INFO 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 请求Url : http://localhost:8080/log/exception
2019-08-06 18:09:30.885  INFO 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 请求方式 : GET
2019-08-06 18:09:30.885  INFO 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 请求ip : 0:0:0:0:0:0:0:1
2019-08-06 18:09:30.886  INFO 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 请求方法 : 
2019-08-06 18:09:30.886  INFO 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 请求参数 : []
2019-08-06 18:09:30.886  INFO 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 浏览器：CHROME
2019-08-06 18:09:30.886  INFO 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 浏览器版本：76.0.3809.87
2019-08-06 18:09:30.886  INFO 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 操作系统: MAC_OS_X
2019-08-06 18:09:30.889 ERROR 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 发生异常时间：2019-08-06T18:09:30.889
2019-08-06 18:09:30.890 ERROR 15513 --- [nio-8080-exec-1] cn.van.log.aop.aspect.WebLogAspect       : 抛出异常：For input string: "abc123"
2019-08-06 18:09:30.901 ERROR 15513 --- [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.NumberFormatException: For input string: "abc123"] with root cause

java.lang.NumberFormatException: For input string: "abc123"
```