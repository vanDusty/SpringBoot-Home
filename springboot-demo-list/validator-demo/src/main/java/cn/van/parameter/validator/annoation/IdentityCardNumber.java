package cn.van.parameter.validator.annoation;

import cn.van.parameter.validator.annoation.service.IdentityCardNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @公众号： 风尘博客
 * @Classname IdentityCardNumber
 * @Description 身份证校验注解
 * @Date 2019/9/09 9:32 下午
 * @Author by Van
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