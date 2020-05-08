package cn.van.fil.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @公众号： 风尘博客
 * @Classname MyInterceptor
 * @Description 拦截器示例
 * @Date 2020/3/31 8:49 下午
 * @Author by Van
 */
@Component
@Slf4j
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("【MyInterceptor】调用了:{}", request.getRequestURI());
        request.setAttribute("requestTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception{
        if (!request.getRequestURI().contains("/getUserCount")) {
            HttpSession session = request.getSession();
            String sessionName = (String) session.getAttribute("name");
            if ("Van".equals(sessionName)) {
                log.info("【MyInterceptor】当前浏览器存在 session:{}",sessionName);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        long duration = (System.currentTimeMillis() - (Long)request.getAttribute("requestTime"));
        log.info("【MyInterceptor】[{}]调用耗时:{}ms",request.getRequestURI(), duration);
    }
}

