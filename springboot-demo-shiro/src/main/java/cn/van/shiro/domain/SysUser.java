package cn.van.shiro.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
@Data
@Accessors(chain = true)
public class SysUser{
    private Long id;

    private String userName;

    private String password;

    private String salt;

    private String realName;

    private String avatar;

    private String phone;

    private String email;

    private Byte sex;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer source;
}