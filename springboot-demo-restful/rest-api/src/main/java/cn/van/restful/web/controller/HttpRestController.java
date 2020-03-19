package cn.van.restful.web.controller;

import cn.van.restful.enunm.ResultCodeEnum;
import cn.van.restful.util.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @公众号： 风尘博客
 * @Classname HttpRestController
 * @Description 统一结果测试
 * @Date 2019/3/19 9:05 下午
 * @Author by Van
 */
@RestController
@RequestMapping("/httpRest")
@Api(tags = "统一结果测试")
public class HttpRestController {

    @ApiOperation(value = "通用返回成功（没有返回结果）", httpMethod = "GET")
    @GetMapping("/success")
    public HttpResult success(){
        return HttpResult.success();
    }

    @ApiOperation(value = "返回成功（有返回结果）", httpMethod = "GET")
    @GetMapping("/successWithData")
    public HttpResult successWithData(){
        return HttpResult.success("风尘博客");
    }

    @ApiOperation(value = "通用返回失败", httpMethod = "GET")
    @GetMapping("/failure")
    public HttpResult failure(){
        return HttpResult.failure(ResultCodeEnum.NOT_FOUND);
    }

}
