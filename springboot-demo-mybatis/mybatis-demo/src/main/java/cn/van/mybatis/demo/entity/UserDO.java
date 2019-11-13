package cn.van.mybatis.demo.entity;

import lombok.Data;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserDO
 *
 * @author: Van
 * Date:     2019-07-24 15:49
 * Description: tb_user 表对应的实体类
 * Version： V1.0
 */
@Data
public class UserDO {
    private Long id;
    private String userName;
    private Integer userAge;
}
