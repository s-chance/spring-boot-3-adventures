package com.entropy.validation;

import com.entropy.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// 第一个泛型指定需要提供校验规则的注解类，第二个泛型指定需要校验的数据类型
public class StateValidation implements ConstraintValidator<State, String> {
    /**
     *
     * @param value 将来要校验的数据
     * @param context
     * @return 返回 false，校验不通过，返回 true，校验通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 提供校验规则
        if (value == null) {
            return false;
        }
        if (value.equals("已发布") || value.equals("草稿")) {
            return true;
        }
        return false;
    }
}
