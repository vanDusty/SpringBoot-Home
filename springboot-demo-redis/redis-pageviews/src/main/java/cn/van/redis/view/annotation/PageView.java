package cn.van.redis.view.annotation;

import java.lang.annotation.*;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: PageView
 *
 * @author: Van
 * Date:     2019-03-26 18:30
 * Description:
 * Version： V1.0
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageView {
    /**
     * 描述
     */
    String description()  default "";
}
