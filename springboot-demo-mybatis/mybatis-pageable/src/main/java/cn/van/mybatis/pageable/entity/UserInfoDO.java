package cn.van.mybatis.pageable.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserInfoDO
 *
 * @author: Van
 * Date:     2019-12-28 19:18
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Data
public class UserInfoDO {

    private Long id;

    private String userName;

    private Integer age;

    private LocalDateTime createTime;
}
