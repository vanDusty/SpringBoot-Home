package cn.van.parameter.validator.web.controller;

import cn.van.parameter.validator.DTO.UserDTO;
import cn.van.parameter.validator.domain.assist.Create;
import cn.van.parameter.validator.domain.HttpResult;
import cn.van.parameter.validator.domain.assist.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @公众号： 风尘博客
 * @Classname ValidatorGroupsController
 * @Description 分组校验
 * @Date 2019/9/09 9:32 下午
 * @Author by Van
 */
@RestController
@RequestMapping("/groups")
public class ValidatorGroupsController {

    /**
     * 更新数据，需要传入userID
     * @param userDTO
     * @return
     */
    @PostMapping("/update")
    public HttpResult updateData(@Validated(Update.class)UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }
    /**
     * 新增数据，不需要传入userID
     * @param userDTO
     * @return
     */
    @PostMapping("/create")
    public HttpResult createData(@Validated(Create.class)UserDTO userDTO) {
        return HttpResult.success(userDTO);
    }

}
