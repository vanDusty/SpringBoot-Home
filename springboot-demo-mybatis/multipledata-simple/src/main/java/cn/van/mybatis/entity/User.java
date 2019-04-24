/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: User
 * Author:   zhangfan
 * Date:     2019-03-29 15:08
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.mybatis.entity;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-29
 * @since 1.0.0
 */
@Data
public class User {
    private Long id;
    private String userName;
    private String passWord;
    private String userSex;
    private String nickName;
}