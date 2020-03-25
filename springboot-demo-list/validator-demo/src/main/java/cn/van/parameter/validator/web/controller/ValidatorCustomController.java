package cn.van.parameter.validator.web.controller;

import cn.van.parameter.validator.DTO.UserDTO;
import cn.van.parameter.validator.domain.HttpResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @公众号： 风尘博客
 * @Classname ValidatorCustomController
 * @Description 自定义注解参数校验
 * @Date 2019/9/09 9:32 下午
 * @Author by Van
 */
@RestController
@RequestMapping("/custom")
public class ValidatorCustomController {

    /**
     * 自定义注解参数校验案例
     * @param userDTO
     * @return
     */
    @PostMapping("/test")
    public HttpResult test(@Validated UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }

}
