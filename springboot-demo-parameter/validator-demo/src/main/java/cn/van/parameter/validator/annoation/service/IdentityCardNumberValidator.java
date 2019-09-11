package cn.van.parameter.validator.annoation.service;

import cn.van.parameter.validator.annoation.IdentityCardNumber;
import cn.van.parameter.validator.util.IdCardValidatorUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: IdentityCardNumberValidator
 *
 * @author: Van
 * Date:     2019-09-11 21:08
 * Description: 身份证校验 Validator
 * Version： V1.0
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