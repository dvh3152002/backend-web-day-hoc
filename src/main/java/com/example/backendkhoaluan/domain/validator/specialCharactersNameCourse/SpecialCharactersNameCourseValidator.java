package com.example.backendkhoaluan.domain.validator.specialCharactersNameCourse;

import com.example.backendkhoaluan.domain.validator.specialCharacters.SpecialCharaters;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialCharactersNameCourseValidator implements ConstraintValidator<SpecialCharactersNameCourse,String> {

    @Override
    public void initialize(SpecialCharactersNameCourse constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null || s.isEmpty()) return true;
        Pattern p = Pattern.compile("[^aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ0-9\\s]");
        Matcher m = p.matcher(s);
        boolean b = m.find();
        if(b) {
            return false;
        } else {
            return true;
        }
    }
}
