package cn.van.parameter.validator.annoation;

import cn.van.parameter.validator.annoation.service.IdentityCardNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: IdentityCardNumber
 *
 * @author: Van
 * Date:     2019-09-11 21:06
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentityCardNumberValidator.class)
public @interface IdentityCardNumber {

    String message() default "身份证号码格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}