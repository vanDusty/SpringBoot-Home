/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: CorsTestController
 * Author:   zhangfan
 * Date:     2019-03-14 16:29
 * Description: 测试跨域请求
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.cros.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br> 
 * 〈测试跨域请求〉
 *
 * @author zhangfan
 * @create 2019-03-14
 * @since 1.0.0
 */
@RequestMapping("/demo2")
@RestController
//@CrossOrigin //所有域名均可访问该类下所有接口
//@CrossOrigin("https://blog.csdn.net"var token= "LtSFVqKxvpS1nPARxS2lpUs2Q2IpGstidMrS8zMhNV3rT7RKnhLN6d2FFirkVEzVIeexgEHgI/PtnynGqjZlyGkJa4+zYIXxtDMoK/N+AB6wtsskYXereH3AR8kWErwIRvx+UOFveH3dgmdw1347SYjbL/ilGKX5xkoZCbfb1f0=,LZkg22zbNsUoHAgAUapeBn541X5OHUK7rLVNHsHWDM/BA4DCIP1f/3Bnu4GAElQU6cds/0fg9Li5cSPHe8pyhr1Ii/TNcUYxqHMf9bHyD6ugwOFTfvlmtp6RDopVrpG24RSjJbWy2kUOOjjk5uv6FUTmbrSTVoBEzAXYKZMM2m4=,R4QeD2psvrTr8tkBTjnnfUBw+YR4di+GToGjWYeR7qZk9hldUVLlZUsEEPWjtBpz+UURVmplIn5WM9Ge29ft5aS4oKDdPlIH8kWNIs9Y3r9TgH3MnSUTGrgayaNniY9Ji5wNZiZ9cE2CFzlxoyuZxOcSVfOxUw70ty0ukLVM/78=";
//var xhr = new XMLHttpRequest();
//xhr.open('GET', 'http://127.0.0.1:8080/demo2/sayHello');
//xhr.setRequestHeader("x-access-token",token);
//xhr.send(null);
//xhr.onload = function(e) {
//    var xhr = e.target;
//    console.log(xhr.responseText);
//}) // 只有指定域名可以访问该类下所有接口
public class CorsTest2Controller {

    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello world --- 2";
    }
}