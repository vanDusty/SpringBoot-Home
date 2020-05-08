package cn.van.fil.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @公众号： 风尘博客
 * @Classname MyHttpSessionListener
 * @Description 监听对象属性监听器
 * @Date 2020/3/31 8:50 下午
 * @Author by Van
 */
@Slf4j
public class MyHttpSessionListener implements HttpSessionListener {

    /**
     * 在线人数
     */
    public static AtomicInteger userCount = new AtomicInteger(0);

    @Override
    public synchronized void sessionCreated(HttpSessionEvent se) {
        userCount.getAndIncrement();
        se.getSession().getServletContext().setAttribute("sessionCount", userCount.get());
        log.info("【在线人数】人数增加为:{}",userCount.get());
    }

    @Override
    public synchronized void sessionDestroyed(HttpSessionEvent se) {
        userCount.getAndDecrement();
        se.getSession().getServletContext().setAttribute("sessionCount", userCount.get());
        log.info("【在线人数】人数减少为:{}",userCount.get());
    }
}

