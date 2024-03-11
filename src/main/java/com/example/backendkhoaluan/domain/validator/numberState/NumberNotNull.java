package com.example.backendkhoaluan.domain.validator.numberState;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumberNotNullValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberNotNull {
    String message() default "Số phải khác null và lớn hơn 0";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
