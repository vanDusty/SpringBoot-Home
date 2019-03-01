# SpringBoot 配置 AOP 记录日志

> 在项目开发中，日志系统是必不可少的，然而在项目中处理的一个Web的请求及其业务的处理，这些操作不需要我们在相应的方法中一个一个的去实现，这样也不利于维护。我们可以借助Spring的AOP来更加优雅的实现。接下来就介绍一下在Spring Boot中的实现。

## 一、Spring AOP的介绍

> AOP(Aspect-Oriented Programming，面向切面编程)，它利用一种"横切"的技术，将那些多个类的共同行为封装到一个可重用的模块。便于减少系统的重复代码，降低模块之间的耦合度，并有利于未来的可操作性和可维护性。

AOP中有以下概念：

- Aspect（切面）：Aspect声明类似于Java中的类声明，在Aspect中会包含一些Pointcut及相应的Advice。
- Joint point（连接点）：表示在程序中明确定义的点。包括方法的调用、对类成员的访问等。
- Pointcut（切入点）：表示一个组Joint point，如方法名、参数类型、返回类型等等。
- Advice（通知）：Advice定义了在Pointcut里面定义的程序点具体要做的操作，它通过(before、around、after(return、throw)、finally来区别实在每个Joint point之前、之后还是执行 前后要调用的代码。
- before：在执行方法前调用Advice，比如请求接口之前的登录验证。
- around：在执行方法前后调用Advice，这是最常用的方法。
- after：在执行方法后调用Advice，after return是方法正常返回后调用，after throw是方法抛出异常后调用。
- finally：方法调用后执行Advice，无论是否抛出异常还是正常返回。
- AOP proxy：AOP proxy也是Java对象，是由AOP框架创建，用来完成上述动作，AOP对象通常可以通过JDK dynamic proxy完成，或者使用CGLIb完成。
- Weaving：实现上述切面编程的代码织入，可以在编译时刻，也可以在运行时刻，Spring和其它大多数Java框架都是在运行时刻生成代理。

## 二、在Spring Boot中使用AOP

> 博主本案例的效果是用AOP在service层做入参和出参的参数打印，同时对异常进行日志打印，避免重复的手写日志，完整案例见文末源码。

### 2.1 在pom引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
```

### 2.2 然后编写切面类：`ServiceLogAspect`

```java 
@Aspect
@Component
public class ServiceLogAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    // 进入方法
    private LocalDateTime startTime ;
    // 方法结束(用户计时)
    private LocalDateTime endTime ;
    // 异常发生时间
    private LocalDateTime happenTime ;

    @Pointcut("execution(public * cn.van.service..*.*(..))")
    public void serviceLogPointcut(){}

    @Before("serviceLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("请求开始时间："+ startTime.now());
        // 记录下请求内容
        logger.info("请求Url : " + request.getRequestURL().toString());
        logger.info("请求方式 : " + request.getMethod());
        logger.info("请求ip : " + request.getRemoteAddr());
        logger.info("请求方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("请求参数 : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "serviceLogPointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("请求结束时间："+ endTime.now());

        // 处理完请求，返回内容
        logger.info("请求返回 : " + ret);
    }

    @AfterThrowing(value = "serviceLogPointcut()",throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        // 保存异常日志记录
        logger.error("抛出的异常："+throwable.getMessage());
    }
    
    @Around("serviceLogPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        logger.info("请求开始时间："+ startTime.now());
        // 记录下请求内容
        logger.info("请求Url : " + request.getRequestURL().toString());
        logger.info("请求方式 : " + request.getMethod());
        logger.info("请求ip : " + request.getRemoteAddr());
        logger.info("请求方法 : " + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        logger.info("请求参数 : " + Arrays.toString(pjp.getArgs()));
        Object ret = pjp.proceed();
        logger.info("请求结束时间："+ endTime.now());
        // 处理完请求，返回内容
        logger.info("请求返回 : " + ret);
        return ret;
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
8. `pjp.proceed()`：当我们执行完切面代码之后，还有继续处理业务相关的代码。proceed()方法会继续执行业务代码，并且其返回值，就是业务处理完成之后的返回值。

### 2.3 编写测试类`AopServiceTest`

```java 
@RunWith(SpringRunner.class)
@SpringBootTest
public class AopServiceTest {

    @Resource
    AopService aopService;

    @Test
    public void insertLog() {
        aopService.insertLog(1l,"van");
    }

    @Test
    public void throwException() {
        aopService.throwException("模拟抛出异常");
    }
}
```

### 2.4 测试

启动测试类进行测试，本案例提供了正常运行的日志打印和异常的日志打印。博主控制台的日志如下：

#### 2.4.1 运行`insertLog`方法日志

```java
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][103]  请求开始时间：2019-03-01T16:48:39.675
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][105]  请求Url : http://localhost
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][106]  请求方式 : 
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][107]  请求ip : 127.0.0.1
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][108]  请求方法 : cn.van.service.impl.AopServiceImpl.insertLog
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][109]  请求参数 : [1, van]
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][54]  请求开始时间：2019-03-01T16:48:39.680
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][56]  请求Url : http://localhost
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][57]  请求方式 : 
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][58]  请求ip : 127.0.0.1
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][59]  请求方法 : cn.van.service.impl.AopServiceImpl.insertLog
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][60]  请求参数 : [1, van]
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][112]  请求结束时间：2019-03-01T16:48:39.692
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][114]  请求返回 : success
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][73]  请求结束时间：2019-03-01T16:48:39.693
2019-03-01 16:48:39 [INFO] [main] [c.v.a.ServiceLogAspect][76]  请求返回 : success
```

#### 2.4.2 运行`throwException`方法日志

```java
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][103]  请求开始时间：2019-03-01T16:47:19.987
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][105]  请求Url : http://localhost
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][106]  请求方式 : 
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][107]  请求ip : 127.0.0.1
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][108]  请求方法 : cn.van.service.impl.AopServiceImpl.throwException
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][109]  请求参数 : [模拟抛出异常]
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][54]  请求开始时间：2019-03-01T16:47:19.993
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][56]  请求Url : http://localhost
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][57]  请求方式 : 
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][58]  请求ip : 127.0.0.1
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][59]  请求方法 : cn.van.service.impl.AopServiceImpl.throwException
2019-03-01 16:47:19 [INFO] [main] [c.v.a.ServiceLogAspect][60]  请求参数 : [模拟抛出异常]
2019-03-01 16:47:20 [ERROR] [main] [c.v.a.ServiceLogAspect][88]  抛出的异常：模拟抛出异常
```

## 三、源码及其延伸

1. 源码地址：[https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-aop](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-aop)
2. 关于日志配置的问题，详见博主上一篇文章[SpringBoot (三)日志配置之logback](https://blog.csdn.net/weixin_42036952/article/details/88033678);
3. 源码中包含了controller的请求的aop方式打印日志，原理跟service一样，详见源码，下载并启动，通过[http://localhost:8080/aop/hello](http://localhost:8080/aop/hello)/[http://localhost:8080/aop/ex](http://localhost:8080/aop/ex)查看日志;
4. 整理不易，如果帮你解决了问题麻烦点个star，谢谢！