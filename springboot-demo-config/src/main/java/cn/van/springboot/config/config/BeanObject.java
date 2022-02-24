package cn.van.springboot.config.config;

import lombok.Data;

import java.io.Serializable;

/**
 * Copyright (C), 2017-2022, 风尘博客
 * 公众号 : 风尘博客
 * FileName: BeanObject
 *
 * @Author: VanFan
 * Date:     2022/2/24 8:50 PM
 * Description:
 * Version： V1.0
 */
@Data
public class BeanObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appKey;

    private String appSecret;
}
