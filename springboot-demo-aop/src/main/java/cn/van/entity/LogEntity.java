/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: LogEntity
 * Author:   zhangfan
 * Date:     2019-02-27 16:06
 * Description: 日志信息实体
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 〈一句话功能简述〉<br> 
 * 〈日志信息实体〉
 *
 * @author zhangfan
 * @create 2019-02-27
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class LogEntity {

    // 请求ip
    private String ip;
    // 请求地址
    private String url;
    // 请求方法类型（get/post）
    private String httpMethod;
    // 类的路径
    private String classPath;
    // 方法名称
    private String methodName;
    // 入参
    private String params;
    // 出参
    private String args;
    // 方法执行耗时
    private Long costTime;

}