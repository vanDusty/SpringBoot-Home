package cn.van.mybatis.multipleData.aspect;

import cn.van.mybatis.multipleData.annotation.DataSource;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: DynamicDataSourceAnnotationAdvisor
 *
 * @author: Van
 * Date:     2019-07-30 15:53
 * Description: AOP 切面处理
 * Version： V1.0
 *
 * 继承AbstractPointcutAdvisor 类似于使用@Aspect注解
 * 实现BeanFactoryAware 接口的bean其实是希望知道自己属于哪一个BeanFactory
 */

public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private Advice advice;

    private Pointcut pointcut;

    public DynamicDataSourceAnnotationAdvisor(DynamicDataSourceAnnotationInterceptor dynamicDataSourceAnnotationInterceptor) {
        this.advice = dynamicDataSourceAnnotationInterceptor;
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        Pointcut cpc = (Pointcut) new AnnotationMatchingPointcut(DataSource.class, true);
        // 类注解
        Pointcut clpc = (Pointcut) AnnotationMatchingPointcut.forClassAnnotation(DataSource.class);
        // 方法注解
        Pointcut mpc = (Pointcut) AnnotationMatchingPointcut.forMethodAnnotation(DataSource.class);
        return new ComposablePointcut(cpc).union(clpc).union(mpc);
    }
}
