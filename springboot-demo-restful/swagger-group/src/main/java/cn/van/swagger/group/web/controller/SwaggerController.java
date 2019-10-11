/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: SwaggerController
 * Author:   zhangfan
 * Date:     2019-04-08 16:23
 * Description: 控制器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.swagger.group.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: SwaggerController
 *
 * @author: Van
 * Date:     2019-04-10 20:18
 * Description:
 * Version： V1.0
 */
@RestController
@RequestMapping("/swagger")
@Api(tags = "第一个包的Swagger接口")
public class SwaggerController {

    @GetMapping("/sayHello")
    public String sayHello(){
        return "hello SwaggerController!";
    }

}