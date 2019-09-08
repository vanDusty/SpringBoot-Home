package cn.van.redis.idempotency.aspect;

import cn.van.redis.idempotency.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: PageViewAspect
 *
 * @author: Van
 * Date:     2019-09-03 11:19
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Aspect
@Configuration
@Slf4j
public class PageViewAspect {

    @Resource
    private TokenService tokenService;

    /**
     * 切入点
     */
    @Pointcut("@annotation(cn.van.redis.idempotency.annotation.ApiIdempotent)")
    public void PageViewAspect() {

    }

    /**
     * 切入处理
     * @param joinPoint
     * @return
     */
    @Around("PageViewAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        tokenService.checkToken(request);
        return "success";
    }
}
