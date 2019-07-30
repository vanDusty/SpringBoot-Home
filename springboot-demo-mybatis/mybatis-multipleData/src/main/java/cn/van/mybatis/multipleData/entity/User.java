package cn.van.mybatis.multipleData.entity;

import lombok.Data;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: User
 *
 * @author: Van
 * Date:     2019-07-30 15:49
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Data
public class User {
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别 1=男 2=女 其他=保密
     */
    private Integer sex;
}
