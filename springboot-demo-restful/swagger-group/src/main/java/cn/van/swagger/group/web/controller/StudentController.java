package cn.van.swagger.group.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: StudentController
 *
 * @author: Van
 * Date:     2019-04-10 20:47
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
// @Api(tags = "学生任务")
@Api(tags = {"学生任务", "教学管理"})
@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @ApiOperation("学习")
    @GetMapping("/study")
    public String study() {
        return "study....";
    }
}
