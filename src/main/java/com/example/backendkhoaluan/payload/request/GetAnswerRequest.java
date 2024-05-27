package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.repository.CustomAnswerQuery;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class GetAnswerRequest extends CustomAnswerQuery.AnswerFilterParam {
    @Min(value = 0, message = Constants.ErrorMessageAnswerValidation.START_SIZE)
    private int start = 0;
    @Range(min = 2, max = 10, message = Constants.ErrorMessageAnswerValidation.LIMIT_SIZE)
    private int limit = 2;
}
