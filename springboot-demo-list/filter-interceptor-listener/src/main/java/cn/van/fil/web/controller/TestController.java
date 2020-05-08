package cn.van.fil.web.controller;

import cn.van.fil.listener.MyHttpSessionListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @公众号： 风尘博客
 * @Classname TestController
 * @Description TODO
 * @Date 2020/3/31 3:53 下午
 * @Author by Van
 */
@RestController
@RequestMapping("")
public class TestController {

    /**
     * 增加在线人数
     * @param request
     */
    @GetMapping("/addSession")
    public void addSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("name", "Van");
    }

    /**
     * 减少在线人数
     * @param request
     */
    @GetMapping("/removeSession")
    public void removeSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
    }

    /**
     * 统计在线人数
     * @return
     */
    @GetMapping("/getUserCount")
    public String getUserCount() {
        return "当前在线人数" + MyHttpSessionListener.userCount.get() + "人";
    }

    /**
     * 重定向的地址
     * @return
     */
    @GetMapping("/redirectUrl")
    public String redirectUrl() {
        return "欢迎关注：风尘博客！";
    }

}

