package cn.van.exception.global.entity;

import lombok.Data;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: User
 *
 * @author: Van
 * Date:     2019-08-01 17:14
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Data
public class User {
    /** 编号*/
    private Long id;
    /** 姓名*/
    private String name;
    /** 年龄*/
    private Integer age;
}
