package cn.van.restful.web.controller;

import cn.van.restful.handler.TokenVerificationException;
import cn.van.restful.util.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @公众号： 风尘博客
 * @Classname ExceptionRestController
 * @Description 异常测试接口
 * @Date 2019/3/19 11:05 下午
 * @Author by Van
 */
@RestController
@RequestMapping("/exception")
@Api(tags = "异常测试接口")
public class ExceptionRestController {

    @ApiOperation(value = "业务异常(token 异常)", httpMethod = "GET")
    @GetMapping("/token")
    public HttpResult token() {
        // 模拟业务层抛出 token 异常
        throw new TokenVerificationException("token 已经过期");
    }


    @ApiOperation(value = "其他异常", httpMethod = "GET")
    @GetMapping("/errorException")
    public HttpResult errorException() {
        //这里故意造成一个其他异常，并且不进行处理
        Integer.parseInt("abc123");
        return HttpResult.success();
    }
}
