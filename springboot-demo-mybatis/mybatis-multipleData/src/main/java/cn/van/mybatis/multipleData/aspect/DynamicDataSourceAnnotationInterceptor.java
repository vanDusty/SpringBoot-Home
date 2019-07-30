package cn.van.mybatis.multipleData.aspect;

import cn.van.mybatis.multipleData.annotation.DataSource;
import cn.van.mybatis.multipleData.config.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: DynamicDataSourceAnnotationInterceptor
 *
 * @author: Van
 * Date:     2019-07-30 15:54
 * Description: 动态数据源拦截器
 * Version： V1.0
 */
@Slf4j
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {



    /**
     * 缓存方法注解值
     */
    private static final Map<Method, String> METHOD_CACHE = new HashMap<Method, String>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String datasource = determineDatasource(invocation);
            if (! DynamicDataSourceContextHolder.containsDataSource(datasource)) {
                log.info("数据源[{}]不存在，使用默认数据源 >", datasource);
            }
            DynamicDataSourceContextHolder.setDataSourceRouterKey(datasource);
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.removeDataSourceRouterKey();
        }
    }

    private String determineDatasource(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (METHOD_CACHE.containsKey(method)) {
            return METHOD_CACHE.get(method);
        } else {
            DataSource ds = method.isAnnotationPresent(DataSource.class) ? method.getAnnotation(DataSource.class)
                    : AnnotationUtils.findAnnotation(method.getDeclaringClass(), DataSource.class);
            METHOD_CACHE.put(method, ds.value());
            return ds.value();
        }
    }

}
