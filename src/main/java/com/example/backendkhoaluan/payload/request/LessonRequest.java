package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.domain.validator.numberState.NumberNotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LessonRequest {
    @NotBlank(message = Constants.ErrorMessageLessonValidation.TITLE_NOT_BLANK)
    private String title;

    @NumberNotNull(message = Constants.ErrorMessageLessonValidation.COURSE_ID_NOT_BLANK)
    private int idCourse;
}
