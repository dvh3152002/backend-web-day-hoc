package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    @NotBlank(message = Constants.ErrorMessageQuestionValidation.TITLE_NOT_BLANK)
    private String title;
    @NotBlank(message = Constants.ErrorMessageQuestionValidation.DESCRIPTION_NOT_BLANK)
    private String body;
    private List<String> tags;
    private Integer userId;
}
