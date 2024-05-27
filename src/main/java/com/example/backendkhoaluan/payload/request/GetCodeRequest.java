package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.repository.CustomCodeQuery;
import lombok.Data;

@Data
public class GetCodeRequest extends CustomCodeQuery.CodeFilterParam {
//    @Min(value = 0, message = Constants.ErrorMessageLessonValidation.START_SIZE)
    private int start = 0;
//    @Range(min = 5, max = 50, message = Constants.ErrorMessageLessonValidation.LIMIT_SIZE)
    private int limit = 10;
}
