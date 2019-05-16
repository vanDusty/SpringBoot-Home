/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: ApiResult
 * Author:   zhangfan
 * Date:     2019-05-15 18:17
 * Description: 结果集
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.shiro.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈结果集〉
 *
 * @author zhangfan
 * @create 2019-05-15
 * @since 1.0.0
 */
@Data
public class ApiResult<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private boolean success;
    // 无参构造器
    public ApiResult() {
        this.code = 200;
        this.success = true;
    }
    // 返回成功的实体
    public ApiResult(T obj) {
        this.code = 200;
        this.data = obj;
        this.success = true;
    }
    // 返回错误信息
    public ApiResult(int code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }
}