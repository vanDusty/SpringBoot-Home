/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: WebLog
 * Author:   zhangfan
 * Date:     2019-06-11 17:38
 * Description: controller 层自定义注解
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.annotation.annotation;

import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈controller 层自定义注解〉
 *
 * @author zhangfan
 * @create 2019-06-11
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME) // 什么时候使用该注解，我们定义为运行时；
@Target({ElementType.METHOD}) //用于什么地方，我们定义为作用于方法上；
@Documented //注解是否将包含在 JavaDoc 中
public @interface WebLog {
    String description() default "";
}