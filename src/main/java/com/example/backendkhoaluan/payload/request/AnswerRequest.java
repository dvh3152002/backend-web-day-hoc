package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.domain.validator.numberState.NumberNotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AnswerRequest {
    @NotBlank(message = Constants.ErrorMessageAnswerValidation.DESCRIPTION_NOT_BLANK)
    private String body;
    private Integer idUser;
    private Integer idQuestion;
}
