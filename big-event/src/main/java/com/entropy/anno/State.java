package com.entropy.anno;

import com.entropy.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// 元注解，帮助文档
@Documented
// 指定校验规则提供类
@Constraint(validatedBy = { StateValidation.class }) // 指定提供校验规则的类
// 元注解，标注可以使用的地方，这里只需要在实体类属性上使用
@Target({ElementType.FIELD})
// 元注解，标注生命周期
@Retention(RetentionPolicy.RUNTIME)
public @interface State {

    // 提供校验失败后的提示信息
    String message() default "state 的值只能为已发布或者草稿";
    // 指定分组
    Class<?>[] groups() default {};
    // 负载，获取到 State 注解的附加信息
    Class<? extends Payload>[] payload() default {};
}
