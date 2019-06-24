package cn.van.aspect;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Service 请求的AOP日志处理
 *
 * @author Van
 * @since 2019.2.25
 */
// @Aspect 声明这是一个切面类
// @Compoent 此类交由Spring容器管理
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


    //    定义请求日志切入点，其切入点表达式有多种匹配方式,这里是指定路径
    @Pointcut("execution(public * cn.van.redisson..*.*(..))")
    public void serviceLogPointcut(){}

    /**
     * 前置通知：
     * 1. 在执行目标方法之前执行，比如请求接口之前的登录验证;
     * 2. 在前置通知中设置请求日志信息，如开始时间，请求参数，注解内容等
     * @param joinPoint
     * @throws Throwable
     */
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

    /**
     * 返回通知：
     * 1. 在目标方法正常结束之后执行
     * 1. 在返回通知中补充请求日志信息，如返回时间，方法耗时，返回值，并且保存日志信息
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "serviceLogPointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("请求结束时间："+ endTime.now());

        // 处理完请求，返回内容
        logger.info("请求返回 : " + ret);
    }

    /**
     * 异常通知：
     * 1. 在目标方法非正常结束，发生异常或者抛出异常时执行
     * 1. 在异常通知中设置异常信息，并将其保存
     * @param throwable
     */
    @AfterThrowing(value = "serviceLogPointcut()",throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        // 保存异常日志记录
        logger.error("抛出的异常："+throwable.getMessage());
    }

    /**
     * 在执行方法前后调用Advice，这是最常用的方法，相当于@Before和@AfterReturning全部做的事儿
     * @param pjp
     * @return
     * @throws Throwable
     */
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
        // pjp.proceed()：当我们执行完切面代码之后，还有继续处理业务相关的代码。proceed()方法会继续执行业务代码，并且其返回值，就是业务处理完成之后的返回值。
        Object ret = pjp.proceed();
        logger.info("请求结束时间："+ endTime.now());
        // 处理完请求，返回内容
        logger.info("请求返回 : " + ret);
        return ret;
    }

}
