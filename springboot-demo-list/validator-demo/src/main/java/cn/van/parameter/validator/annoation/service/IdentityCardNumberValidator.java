package cn.van.parameter.validator.annoation.service;

import cn.van.parameter.validator.annoation.IdentityCardNumber;
import cn.van.parameter.validator.util.IdCardValidatorUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @公众号： 风尘博客
 * @Classname IdentityCardNumberValidator
 * @Description 身份证校验 Validator
 * @Date 2019/9/09 9:32 下午
 * @Author by Van
 */
public class IdentityCardNumberValidator implements ConstraintValidator<IdentityCardNumber, Object> {

    @Override
    public void initialize(IdentityCardNumber identityCardNumber) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return IdCardValidatorUtils.isValidate18Idcard(o.toString());
    }
}