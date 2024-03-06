package com.example.backendkhoaluan.domain.validator.specialCharacters;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SpecialCharatersValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecialCharaters {
    String message() default Constants.ErrorMessageUserValidation.FULLNAME_NOT_SPECIAL_CHARATERS;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
