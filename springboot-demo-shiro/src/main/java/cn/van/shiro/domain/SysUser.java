package cn.van.shiro.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
@Data
@Accessors(chain = true)
@ApiModel("用户信息")
public class SysUser{

    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("性别")
    private Byte sex;
    @ApiModelProperty("来源")
    private Integer source;
}