package cn.van.fil.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * @公众号： 风尘博客
 * @Classname MyFilter
 * @Description 拦截器示例
 * @Date 2019/3/31 3:47 下午
 * @Author by Van
 */
@Slf4j
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 启动服务器时加载
        log.info("MyFilter init()");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
        String requestUri = request.getRequestURI();
        log.info("请求地址是："+requestUri);
        if (requestUri.contains("/addSession")
                || requestUri.contains("/removeSession")
                || requestUri.contains("/online")
                || requestUri.contains("/ex")) {
            filterChain.doFilter(servletRequest, response);
        } else {
            // 除了以上三个链接，其他请求转发到 /online
            wrapper.sendRedirect("/online");
        }
    }

    @Override
    public void destroy() {
        // 在服务关闭时销毁
        log.info("MyFilter destroy()");
    }
}

