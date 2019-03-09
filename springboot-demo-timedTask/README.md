# SpringBoot 实现定时任务

> 文末附源码demo 

## 一、定时任务实现的几种方式：

* Timer
	
	这是java自带的java.util.Timer类，这个类允许你调度一个java.util.TimerTask任务。使用这种方式可以让你的程序按照某一个频度执行，但不能在指定时间运行。一般用的较少。

* ScheduledExecutorService

	也jdk自带的一个类；是基于线程池设计的定时任务类,每个调度任务都会分配到线程池中的一个线程去执行,也就是说,任务是并发执行,互不影响。
* Spring Task

	Spring3.0以后自带的task，可以将它看成一个轻量级的Quartz，而且使用起来比Quartz简单许多。
Quartz：这是一个功能比较强大的的调度器，可以让你的程序在指定时间执行，也可以按照某一个频度执行，配置起来稍显复杂。

## 二、基于SpringBoot的定时任务

** SpringBoot默认已经帮我们实行了，只需要添加相应的注解就可以实现 **

### 2.1 导入SpringBoot启动包

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.1.RELEASE</version>
    </dependency>
</dependencies>
```

### 2.2 启动类启用定时

在启动类上面加上`@EnableScheduling`即可开启定时

```java
@SpringBootApplication
@EnableScheduling // 开启定时
public class SpringBootDemoTimeTaskApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootDemoTimeTaskApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoTimeTaskApplication.class);
        logger.info("SpringBootDemoTimeTaskApplication start!");
    }
}
```

### 2.3 创建定时任务实现类`SchedulerTask`

```java 
@Component
public class SchedulerTask {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);

    /**
     * @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行
     * @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行
     * @Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按fixedRate的规则每6秒执行一次
     * @Scheduled(cron=""):详见cron表达式http://www.pppet.net/
     */

    @Scheduled(fixedRate = 5000)
    public void scheduled1() {
        logger.info("=====>>>>>使用fixedRate执行定时任务");
    }
    @Scheduled(fixedDelay = 10000)
    public void scheduled2() {
        logger.info("=====>>>>>使用fixedDelay执行定时任务");
    }

    @Scheduled(cron="*/6 * * * * ?")
    private void scheduled3(){
        logger.info("使用cron执行定时任务");
    }
}
```
运行结果：

```
2019-03-09 17:33:05.681  INFO 7752 --- [           main] c.v.t.SpringBootDemoTimeTaskApplication  : SpringBootDemoTimeTaskApplication start!
2019-03-09 17:33:06.002  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : 使用cron执行定时任务
2019-03-09 17:33:10.680  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : =====>>>>>使用fixedRate执行定时任务
2019-03-09 17:33:12.003  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : 使用cron执行定时任务
2019-03-09 17:33:15.676  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : =====>>>>>使用fixedRate执行定时任务
2019-03-09 17:33:15.676  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : =====>>>>>使用fixedDelay执行定时任务
2019-03-09 17:33:18.002  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : 使用cron执行定时任务
2019-03-09 17:33:20.677  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : =====>>>>>使用fixedRate执行定时任务
2019-03-09 17:33:24.002  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : 使用cron执行定时任务
2019-03-09 17:33:25.680  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : =====>>>>>使用fixedRate执行定时任务
2019-03-09 17:33:25.681  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : =====>>>>>使用fixedDelay执行定时任务
2019-03-09 17:33:30.005  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : 使用cron执行定时任务
2019-03-09 17:33:30.680  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : =====>>>>>使用fixedRate执行定时任务
2019-03-09 17:33:35.680  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : =====>>>>>使用fixedRate执行定时任务
2019-03-09 17:33:35.682  INFO 7752 --- [   scheduling-1] cn.van.task.service.SchedulerTask        : =====>>>>>使用fixedDelay执行定时任务
```

### 2.4 执行时间的配置

在上面的定时任务中，我们在方法上使用`@Scheduled`注解来设置任务的执行时间，并且使用三种属性配置方式：

1. fixedRate：定义一个按一定频率执行的定时任务
1. fixedDelay：定义一个按一定频率执行的定时任务，与上面不同的是，改属性可以配合initialDelay， 定义该任务延迟执行时间。
1. cron：通过表达式来配置任务执行时间


## 三、源码及其延伸

1. 源码地址：[https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-timeTask](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-timeTask)
2. 整理不易，如果帮你解决了问题麻烦点个star，谢谢！