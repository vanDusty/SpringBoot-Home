package cn.van.swagger.group.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MultipleController
 *
 * @author: Van
 * Date:     2019-04-10 20:18
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@RestController
@RequestMapping("/swagger2")
@Api(tags = "第二个包的Swagger接口")
public class MultipleController {
    /**
     *  无参方法
     * @return
     */
    @GetMapping("/sayHello")
    public String sayHello(){
        return "hello MultipleController!";
    }
}
