package cn.van.redis.view.aspect;

import cn.van.redis.view.utils.IpUtils;
import cn.van.redis.view.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;


/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: PageViewAspect
 *
 * @author: Van
 * Date:     2019-03-26 17:30
 * Description: 新增浏览量的切面处理
 * Version： V1.0
 */
@Aspect
@Configuration
@Slf4j
public class PageViewAspect {

    @Resource
    private RedisUtils redisUtil;

    /**
     * 切入点
     */
    @Pointcut("@annotation(cn.van.redis.view.annotation.PageView)")
    public void PageViewAspect() {

    }

    /**
     * 切入处理
     * @param joinPoint
     * @return
     */
    @Around("PageViewAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        Object[] object = joinPoint.getArgs();
        Object articleId = object[0];
        log.info("articleId:{}", articleId);
        Object obj = null;
        try {
            String ipAddr = IpUtils.getIpAddr();
            log.info("ipAddr:{}", ipAddr);
            String key = "articleId_" + articleId;
            // 浏览量存入redis中
            Long num = redisUtil.add(key,ipAddr);
            if (num == 0) {
                log.info("该ip:{},访问的浏览量已经新增过了", ipAddr);
            }
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }
}