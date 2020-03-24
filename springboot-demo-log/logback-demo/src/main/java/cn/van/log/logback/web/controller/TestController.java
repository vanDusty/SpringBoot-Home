package cn.van.log.logback.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @公众号： 风尘博客
 * @Classname TestController
 * @Description rest
 * @Date 2019/2/14 19:59 下午
 * @Author by Van
 */
@RestController
@RequestMapping("/log")
public class TestController {
    /**
     * 测试正常请求
     * @param msg
     * @return
     */
    @GetMapping("/normal/{msg}")
    public String getMsg(@PathVariable String msg) {
        return msg;
    }

    /**
     * 测试抛异常
     * @return
     */
    @GetMapping("/exception/{msg}")
    public String getException(@PathVariable String msg){
        // 故意造出一个异常
        Integer.parseInt("abc123");
        return msg;
    }
}
