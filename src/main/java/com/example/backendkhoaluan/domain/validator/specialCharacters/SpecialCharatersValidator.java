package com.example.backendkhoaluan.domain.validator.specialCharacters;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SpecialCharatersValidator implements ConstraintValidator<SpecialCharaters,String> {

    @Override
    public void initialize(SpecialCharaters constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // Kiểm tra xem họ tên có rỗng không
        if (s == null || s.isEmpty()) {
            return false;
        }

        // Kiểm tra xem họ tên có chứa ký tự đặc biệt hoặc số không
        if (!s.matches("[a-zA-Z\\s]+")) {
            return false;
        }

        // Kiểm tra xem họ tên có ít nhất một ký tự cho tên và một ký tự cho họ
        if (s.trim().split("\\s+").length < 2) {
            return false;
        }

        return true;
    }
}
