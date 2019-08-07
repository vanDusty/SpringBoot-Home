package cn.van.sharding.demo.entity;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Classname User
 * @Description 用户实体类
 * @Author Van
 * @Date 2019-07-26 17:24
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class User{

    private Long id;

    private String userName;

    private Integer userAge;
}
