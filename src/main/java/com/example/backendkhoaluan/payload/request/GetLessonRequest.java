package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.repository.CustomeLessonQuery;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class GetLessonRequest extends CustomeLessonQuery.LessonFilterParam {
    @Min(value = 0, message = Constants.ErrorMessageLessonValidation.START_SIZE)
    private int start = 0;
    @Range(min = 5, max = 50, message = Constants.ErrorMessageLessonValidation.LIMIT_SIZE)
    private int limit = 10;
}
