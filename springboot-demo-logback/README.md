# SpringBoot 配置 logback

[toc]

## 一、logback 介绍
　　Logback是由log4j创始人设计的另一个开源日志组件，进入[官方网站](http://logback.qos.ch)。它分为下面下个模块：

1. logback-core：其它两个模块的基础模块
1. logback-classic：它是log4j的一个改良版本，同时它完整实现了slf4j API使你可以很方便地更换成其它日志系统如log4j或JDK14 Logging
1. logback-access：访问模块与Servlet容器集成提供通过Http来访问日志的功能

## 二、logback 配置介绍

### 2.1 Logger、appender及layout
1. Logger作为日志的记录器，把它关联到应用的对应的context上后，主要用于存放日志对象，也可以定义日志类型、级别。
1. Appender主要用于指定日志输出的目的地，目的地可以是控制台、文件、远程套接字服务器、 MySQL、PostreSQL、 Oracle和其他数据库、 JMS和远程UNIX Syslog守护进程等。 
1. Layout 负责把事件转换成字符串，格式化的日志信息的输出。

### 2.2 有效级别及级别的继承
　　Logger 可以被分配级别。级别包括：`TRACE、DEBUG、INFO、WARN` 和 `ERROR`，定义于`ch.qos.logback.classic.Level`类。如果 logger没有被分配级别，那么它将从有被分配级别的最近的祖先那里继承级别。root logger 默认级别是 DEBUG。

### 2.3 打印方法与基本的选择规则
　　打印方法决定记录请求的级别。例如，如果 L 是一个 logger 实例，那么，语句 `L.info("..")` 是一条级别为 `INFO` 的记录语句。记录请求的级别在高于或等于其 logger 的有效级别时被称为被启用，否则，称为被禁用。记录请求级别为 p，其 logger的有效级别为 q，只有则当 p>=q 时，该请求才会被执行。
该规则是 logback 的核心。级别排序为： `TRACE < DEBUG < INFO < WARN < ERROR`


## 三、logback 常用配置

### 3.1 根节点`<configuration>`,包含三个属性：

1. scan：当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
1. scanPeriod：设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒当scan为true时，此属性生效。默认的时间间隔为1分钟。
1. debug：当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。

```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false"> 
　　  <!--其他配置省略--> 
</configuration>　
```

### 3.2 子节点`<contextName>`
　　用来设置上下文名称，每个logger都关联到logger上下文，默认上下文名称为default。但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。

### 3.3 子节点`<springProperty>` ：定义日志的根目录

1. name: 变量的名称,下文可以使“${}”来使用变量;
1. source: 变量定义的值(放在配置文件中)。

`<springProperty scope="context" name="LOG_PATH" source="logging.path"/>`

### 3.4 子节点`<property>` ：用来定义变量值

> 它有两个属性name和value，通过<property>定义的值会被插入到logger上下文中，可以使“${}”来使用变量。

1. name: 变量的名称
1. value: 的值时变量定义的值

`<property name="fileName" value="spring-boot-logback"></property>`

### 3.5 子节点`<appender>`：负责写日志的组件

> 有两个必要属性name和class。name指定appender名称，class指定appender的全限定名

#### 3.5.1 把日志输出到控制台，有以下子节点：

1. <encoder>：对日志进行格式化；
1. <target>：字符串System.out(默认)或者System.err（区别不多说了）。

// `ch.qos.logback.core.ConsoleAppender`表示控制台输出

```xml
<appender name="consoleLogAppender" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss} [%p] [%.10t] [%c{1}][%L] %X{sbtTraceId} %m%n</pattern>
        <charset>utf-8</charset>
    </encoder>
</appender>
```

### 3.5.2 把日志添加到文件，有以下子节点：
1. <file>：被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。
1. <append>：如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。
1. <encoder>：对记录事件进行格式化。
1. <prudent>：如果是 true，日志会被安全的写入文件，即使其他的FileAppender也在向此文件做写入操作，效率低，默认是 false。
1. <rollingPolicy>：滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件。

```xml
<appender name="fileLogAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <!-- 被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。 -->
    <file>${LOG_PATH}/${fileName}.log</file>
    <!--<append>：如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。-->
    <encoder>
        <!--
        日志输出格式：
            %d表示日期时间，
            %thread表示线程名，
            %-5level：级别从左显示5个字符宽度
            %logger{50} 表示logger名字最长50个字符，否则按照句点分割。
            %msg：日志消息，
            %n是换行符
        -->
        <pattern>%d{yyyy-MM-dd HH:mm:ss} [%p] [%.10t] [%c{1}][%L] %X{sbtTraceId} %m%n</pattern>
        <charset>utf-8</charset>
    </encoder>
    <!-- 滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件 -->
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <!--
        滚动时产生的文件的存放位置及文件名称 %d{yyyy-MM-dd}：按天进行日志滚动
        %i：按天进行打包
        -->
        <fileNamePattern>${LOG_PATH}/${fileName}.log.%d{yyyy-MM-dd}.gz</fileNamePattern>
        <!--
        可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件。假设设置每天滚动，
        且maxHistory是365，则只保存最近365天的文件，删除之前的旧文件。注意，删除旧文件是，
        那些为了归档而创建的目录也会被删除。
        -->
        <maxHistory>365</maxHistory>
        <!-- 活动文件的大小，默认值是10MB -->
    </rollingPolicy>
</appender>
```

### 3.6 子节`<loger>`：用来设置某一个包或具体的某一个类的日志打印级别、以及指定`<appender>`

`<loger>`仅有一个name属性，一个可选的level和一个可选的addtivity属性；也可以包含零个或多个`<appender-ref>`元素，标识这个appender将会添加到这个loger

1. name: 用来指定受此loger约束的某一个包或者具体的某一个类。
1. level: 用来设置打印级别，大小写无关：`TRACE, DEBUG, INFO, WARN, ERROR, ALL和OFF`，还有一个特俗值INHERITED或者同义词NULL，代表强制执行上级的级别。 如果未设置此属性，那么当前loger将会继承上级的级别。
1. addtivity: 是否向上级loger传递打印信息。默认是true。同`<loger>`一样，可以包含零个或多个`<appender-ref>`元素，标识这个appender将会添加到这个loger。

```xml
<!--myibatis log configure-->
<logger name="com.apache.ibatis" level="TRACE"/>
<logger name="java.sql.Connection" level="DEBUG"/>
<logger name="java.sql.Statement" level="DEBUG"/>
<logger name="java.sql.PreparedStatement" level="DEBUG"/>
```
### 3.7 子节点`<root>`
> 它也是<loger>元素，但是它是根loger,是所有<loger>的上级。只有一个level属性，因为name已经被命名为"root",且已经是最上级了。

```xml
<root level="INFO">
    <appender-ref ref="consoleLogAppender"/>
    <appender-ref ref="fileLogAppender"/>
</root>
```
## 四、Demo

### 4.1 添加包依赖
> 因为我用controler测试请求的，所以加入了web包

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
```

### 4.2 logback-spring.xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- configuration:
scan：当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
scanPeriod：设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒当scan为true时，此属性生效。默认的时间间隔为1分钟。
debug：当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。
-->
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <!-- 定义日志的根目录
    name: 变量的名称,下文可以使“${}”来使用变量
    source: 变量定义的值(放在配置文件中)-->
    <springProperty scope="context" name="LOG_PATH" source="logging.path"/>

    <!-- 定义日志文件名称 -->
    <property name="fileName" value="spring-boot-logback"></property>
    <!-- <appender>是负责写日志的组件，它有两个必要属性name和class。name指定appender名称，class指定appender的全限定名 -->
    <appender name="consoleLogAppender" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%p] [%.10t] [%c{1}][%L] %X{sbtTraceId} %m%n</pattern>
            <charset>utf-8</charset>
        </encoder>
    </appender>
    <!-- 把日志添加到文件
    ch.qos.logback.core.ConsoleAppender 表示控制台输出 -->
    <appender name="fileLogAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。 -->
        <file>${LOG_PATH}/${fileName}.log</file>
        <!--<append>：如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。-->
        <encoder>
            <!--
            日志输出格式：
                %d表示日期时间，
                %thread表示线程名，
                %-5level：级别从左显示5个字符宽度
                %logger{50} 表示logger名字最长50个字符，否则按照句点分割。
                %msg：日志消息，
                %n是换行符
            -->
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%p] [%.10t] [%c{1}][%L] %X{sbtTraceId} %m%n</pattern>
            <charset>utf-8</charset>
        </encoder>
        <!-- 滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--
            滚动时产生的文件的存放位置及文件名称 %d{yyyy-MM-dd}：按天进行日志滚动
            %i：按天进行打包
            -->
            <fileNamePattern>${LOG_PATH}/${fileName}.log.%d{yyyy-MM-dd}.gz</fileNamePattern>
            <!--
            可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件。假设设置每天滚动，
            且maxHistory是365，则只保存最近365天的文件，删除之前的旧文件。注意，删除旧文件是，
            那些为了归档而创建的目录也会被删除。
            -->
            <maxHistory>365</maxHistory>
            <!-- 活动文件的大小，默认值是10MB -->
        </rollingPolicy>
    </appender>

    <!--
		logger主要用于存放日志对象，也可以定义日志类型、级别
		name：表示匹配的logger类型前缀，也就是包的前半部分
		level：要记录的日志级别，包括 TRACE < DEBUG < INFO < WARN < ERROR
		additivity：作用在于children-logger是否使用 rootLogger配置的appender进行输出，
		false：表示只用当前logger的appender-ref，true：表示当前logger的appender-ref和rootLogger的appender-ref都有效
    -->
    <root level="INFO">
        <appender-ref ref="consoleLogAppender"/>
        <appender-ref ref="fileLogAppender"/>
    </root>
</configuration>
```

### 4.3 配置application.yml
```yml
# 配置的是日志的存放路径
logging:
  path: '/Users/zhangfan/Documents/Git-public/Springboot-Home/springboot-demo-logback/log'
```
### 4.4 Java代码
#### 4.4.1 aop日志
```java
@Aspect
@Component
public class WebLogAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(public * cn.van.logback.controller..*.*(..))")
    public void webLogPointcut(){}

    @Before("webLogPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }
}
```
#### 4.4.2 Controller 代码
```java
@RestController
@RequestMapping("/logback")
public class LogbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/{msg}")
    public String getMsg(@PathVariable String msg) {
        return "request msg : " + msg;
    }
    @GetMapping("/ex")
    public String getExcation() throws Exception {

        throw new Exception("sd");

    }
}
```

### 4.5 启动项目，测试

1. 请求[http://localhost:8080/logback/hello](http://localhost:8080/logback/hello),查看工作台输出和log文件输出
2. 请求[http://localhost:8080/logback/ex](http://localhost:8080/logback/ex)，查看异常的日志

### 4.6 项目源码

[https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-logback](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-logback)
如果帮你解决了问题麻烦点个star