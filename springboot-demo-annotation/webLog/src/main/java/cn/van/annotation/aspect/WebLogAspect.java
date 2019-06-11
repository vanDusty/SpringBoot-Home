/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: WebLogAspect
 * Author:   zhangfan
 * Date:     2019-06-11 16:16
 * Description: controller 层WebLog切入的行为
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.annotation.aspect;


import cn.van.annotation.annotation.WebLog;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 〈〉<br>
 * 〈controller 层WebLog切入的行为〉
 *
 * @author zhangfan
 * @create 2019-06-11
 * @since 1.0.0
 */
@Aspect // 申明这是一个切面
@Component
//@Profile("dev") //指定dev环境该注解有效，其他环境无效
//@Order(10)
// 在切点之前， @Order 从小到大被执行，也就是说越小的优先级越高；
// 在切点之后， @Order 从大到小被执行，也就是说越大的优先级越高；
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