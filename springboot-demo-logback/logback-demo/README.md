# SpringBoot 异步输出 Logback 日志


> 本文介绍：日志输出到文件并根据LEVEL级别将日志分类保存到不同文件、通过异步输出日志减少磁盘IO提高性能

## 一、介绍

### 1.1 Logback

　　Logback是由log4j创始人设计的另一个开源日志组件，它分为下面下个模块：

1. logback-core：其它两个模块的基础模块
1. logback-classic：它是log4j的一个改良版本，同时它完整实现了slf4j API使你可以很方便地更换成其它日志系统如log4j或JDK14 Logging
1. logback-access：访问模块与Servlet容器集成提供通过Http来访问日志的功能

### 1.2 日志级别

> 包括：`TRACE`、`DEBUG`、`INFO`、`WARN` 和 `ERROR`。

#### 1.2.1 `TRACE`

特别详细的系统运行完成信息，业务代码中，不要使用。(除非有特殊用意，否则请使用`DEBUG`级别替代)

#### 1.2.2 `DEBUG`

1. 可以填写所有的想知道的相关信息(但不代表可以随便写，`debug`信息要有意义,最好有相关参数)；
1. 生产环境需要关闭`DEBUG`信息
1. 如果在生产情况下需要开启DEBUG,需要使用开关进行管理，不能一直开启。

#### 1.2.3 `INFO`

- 系统运行信息
	
	1. Service方法中对于系统/业务状态的变更；
	1. 主要逻辑中的分步骤。

- 外部接口部分
	
	1. 客户端请求参数(REST/WS)；
	1. 调用第三方时的调用参数和调用结果。

- 说明

1. 并不是所有的service都进行出入口打点记录,单一、简单service是没有意义的；
1. 对于复杂的业务逻辑，需要进行日志打点，以及埋点记录，比如电商系统中的下订单逻辑，以及OrderAction操作(业务状态变更)；
1. 对于整个系统的提供出的接口(REST/WS)，使用`INFO`记录入参；
1. 如果所有的service为SOA架构，那么可以看成是一个外部接口提供方，那么必须记录入参；
1. 调用其他第三方服务时，所有的出参和入参是必须要记录的(因为你很难追溯第三方模块发生的问题)。

#### 1.2.4 `WARN`

- 不应该出现但是不影响程序、当前请求正常运行的异常情况:

	1. 有容错机制的时候出现的错误情况；
	1. 找不到配置文件，但是系统能自动创建配置文件；

- 即将接近临界值的时候，例如：缓存池占用达到警告线；
- 业务异常的记录,比如:当接口抛出业务异常时，应该记录此异常。

#### 1.2.5 `ERROR`

影响到程序正常运行、当前请求正常运行的异常情况:

1. 打开配置文件失败；
1. 所有第三方对接的异常(包括第三方返回错误码)；
1. 所有影响功能使用的异常，包括:`SQLException`和除了业务异常之外的所有异常(`RuntimeException`和`Exception`)。

> 不应该出现的情况:
> 如果进行了抛出异常操作，请不要记录`ERROR`日志，由最终处理方进行处理：

反例(不要这么做):

```java
try{
    ....
}catch(Exception ex){
  String errorMessage=String.format("Error while reading information of user [%s]",userName);
  logger.error(errorMessage,ex);
  throw new UserServiceException(errorMessage,ex);
}
```
### 1.3 SpringBoot 中 logback

1. `SpringBoot`工程自带`logback`和`slf4j`的依赖，所以重点放在编写配置文件上，需要引入什么依赖，日志依赖冲突统统都不需要我们管了;
1. `logback`框架会默认加载`classpath`下命名为`logback-spring`或`logback`的配置文件。
1. 将所有日志都存储在一个文件中文件大小也随着应用的运行越来越大并且不好排查问题，正确的做法应该是将`ERROR`日志和其他日志分开，并且不同级别的日志根据时间段进行记录存储。


## 二、logback 配置

### 2.1 配置文件`logback-spring.xml`示例

```xml
<?xml version="1.0" encoding="utf-8"?>
<configuration>
    <!-- 属性文件:在配置文件中找到对应的配置项 -->
    <springProperty scope="context" name="logPath" source="logging.path"/>

    <!-- 输出到控制台 -->
    <appender name="CONSOLE-LOG" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>[%d{yyyy-MM-dd' 'HH:mm:ss.sss}] [%C] [%t] [%L] [%-5p] %m%n</pattern>
        </layout>
    </appender>

    <!-- 获取比info级别高(包括info级别)但除error级别的日志 -->
    <appender name="INFO-LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 指定过滤策略 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>DENY</onMatch>
            <onMismatch>ACCEPT</onMismatch>
        </filter>
        <encoder>
            <!-- 指定日志输出格式 -->
            <pattern>[%d{yyyy-MM-dd' 'HH:mm:ss.sss}] [%C] [%t] [%L] [%-5p] %m%n</pattern>
        </encoder>

        <!-- 指定收集策略：滚动策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 指定生成日志保存地址 -->
            <fileNamePattern>${logPath}/info.%d.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <appender name="ERROR-LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 指定过滤策略 -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <encoder>
            <!-- 指定日志输出格式 -->
            <pattern>[%d{yyyy-MM-dd' 'HH:mm:ss.sss}] [%C] [%t] [%L] [%-5p] %m%n</pattern>
        </encoder>
        <!-- 指定收集策略：滚动策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--指定生成日志保存地址 -->
            <fileNamePattern>${logPath}/error.%d.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>

    <!-- 异步输出 -->
    <appender name="ASYNC-INFO" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>256</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="INFO-LOG"/>
    </appender>

    <appender name="ASYNC-ERROR" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>256</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="ERROR-LOG"/>
    </appender>

    <!-- 指定最基础的日志输出级别 -->
    <root level="info">
        <appender-ref ref="CONSOLE-LOG" />
        <appender-ref ref="INFO-LOG" />
        <appender-ref ref="ERROR-LOG" />
    </root>

</configuration>
```

> 项目配置文件中配置日志输出地址

	
	logging:
	  path: ./logs
	    
### 2.2 标签说明

- `<springProperty>` ：定义日志的根目录

	- `name`: 变量的名称,下文可以使`${xxx}`来使用变量;
	- `source`: 变量定义的值(放在配置文件中)。

- `<root>`标签：指定最基础的日志输出级别；
	- `<appender-ref>`标签，添加`append`


- `<appender>`标签：指定日志的收集策略
	- `name`属性指定`appender`命名
	- `class`属性指定输出策略，通常有两种，控制台输出和文件输出，文件输出就是将日志进行一个持久化。`ConsoleAppender`将日志输出到控制台。


- `<filter>`标签：指定过滤策略
	- `<level>`：指定过滤的类型。


- `<encoder>`标签：使用该标签下的`<pattern>`标签指定日志输出格式。

- `<rollingPolicy>`标签：指定收集策略，比如基于时间进行收集

	- `<fileNamePattern>`标签指定生成日志保存地址，实现了按天分类以及日志的目标了。

	
### 2.3 打印sql语句

其他如上，在我们的`logback-spring.xml`中进行如下配置：

```xml
<!-- 将sql语句输出到具体的日志文件中 -->
<logger name="cn.van.logback" level="debug" additivity="false">
    <appender-ref ref="SQL-APPENDER"/>
</logger>
```	