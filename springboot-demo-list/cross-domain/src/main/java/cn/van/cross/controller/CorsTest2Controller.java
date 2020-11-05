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
package cn.van.cross.controller;

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
//不指定域名时，所有域名均可访问该类下所有接口
//@CrossOrigin
// 只有指定域名可以访问该类下所有接口
@CrossOrigin("https://blog.csdn.net")
public class CorsTest2Controller {

    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello world --- 2";
    }
}