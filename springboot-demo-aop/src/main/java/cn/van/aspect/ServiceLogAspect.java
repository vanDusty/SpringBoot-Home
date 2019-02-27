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
    private Long startTime ;
    // 方法结束(用户计时)
    private Long endTime ;
    // 异常发生时间
    private Long happenTime ;

//    定义请求日志切入点，其切入点表达式有多种匹配方式,这里是指定路径
    @Pointcut("execution(public * cn.van.service..*.*(..))")
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
        startTime = System.currentTimeMillis();
        logger.info("开始时间："+ startTime);
        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
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
        // 记录方法耗时
        endTime = System.currentTimeMillis();
        logger.info("方法耗时："+ (endTime - startTime));

        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
    }

    /**
     * 异常通知：
     * 1. 在目标方法非正常结束，发生异常或者抛出异常时执行
     * 1. 在异常通知中设置异常信息，并将其保存
     * @param throwable
     */
    @AfterThrowing(value = "serviceLogPointcut()",throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        happenTime = System.currentTimeMillis();
        // 保存异常日志记录
        logger.error(throwable.getMessage());
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

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(pjp.getArgs()));
        startTime = System.currentTimeMillis();
        Object ret = pjp.proceed();
        endTime = System.currentTimeMillis();
        logger.info("方法耗时："+ (endTime - startTime));
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
        return ret;
    }

}
