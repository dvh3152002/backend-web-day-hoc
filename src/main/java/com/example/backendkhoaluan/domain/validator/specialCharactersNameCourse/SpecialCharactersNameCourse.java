package com.example.backendkhoaluan.domain.validator.specialCharactersNameCourse;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SpecialCharactersNameCourseValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecialCharactersNameCourse {
        String message() default Constants.ErrorMessageCourseValidation.NAME_NOT_SPECIAL_CHARATERS;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}
