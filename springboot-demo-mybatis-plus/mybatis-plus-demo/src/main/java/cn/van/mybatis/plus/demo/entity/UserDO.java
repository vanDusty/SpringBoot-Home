package cn.van.mybatis.plus.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserDO
 *
 * @author: Van
 * Date:     2019-10-08 20:39
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Data
@TableName("user")
public class UserDO {

    private String id;

    private String userName;

    private Integer age;

    private LocalDateTime createTime;
}
