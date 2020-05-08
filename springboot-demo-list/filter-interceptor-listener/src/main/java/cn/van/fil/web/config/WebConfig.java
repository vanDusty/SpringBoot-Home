package cn.van.fil.web.config;

import cn.van.fil.filter.MyFilter;
import cn.van.fil.interceptor.MyInterceptor;
import cn.van.fil.listener.MyHttpSessionListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @公众号： 风尘博客
 * @Classname WebConfig
 * @Description web 配置
 * @Date 2020/3/31 8:51 下午
 * @Author by Van
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    MyInterceptor myInterceptor;

    /**
     * 注册过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new MyFilter());
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
    }

    /**
     * 注册监听器
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean registrationBean(){
        ServletListenerRegistrationBean registrationBean = new ServletListenerRegistrationBean();
        registrationBean.setListener(new MyHttpSessionListener());
        return registrationBean;
    }
}


