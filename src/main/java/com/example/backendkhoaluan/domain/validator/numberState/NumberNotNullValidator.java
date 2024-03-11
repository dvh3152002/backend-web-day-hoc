package com.example.backendkhoaluan.domain.validator.numberState;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumberNotNullValidator implements ConstraintValidator<NumberNotNull,Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value !=null && value>0;
    }
}
