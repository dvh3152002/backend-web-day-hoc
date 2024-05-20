package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    @NotBlank(message = Constants.ErrorMessageQuestionValidation.TITLE_NOT_BLANK)
    private String title;
    @NotBlank(message = Constants.ErrorMessageQuestionValidation.DESCRIPTION_NOT_BLANK)
    private String body;

    @NotEmpty(message = Constants.ErrorMessageQuestionValidation.TAG_NOT_NULL)
    @Size(min = 1, message = Constants.ErrorMessageQuestionValidation.TAG_NOT_NULL)
    private List<String> tags;
    private Integer userId;
}
