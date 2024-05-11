package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;

import com.example.backendkhoaluan.repository.CustomeQuestionQuery;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class GetQuestionRequest extends CustomeQuestionQuery.QuestionFilterParam {
    @Min(value = 0, message = Constants.ErrorMessageQuestionValidation.START_SIZE)
    private int start = 0;
    @Range(min = 10, max = 50, message = Constants.ErrorMessageQuestionValidation.LIMIT_SIZE)
    private int limit = 10;
}
