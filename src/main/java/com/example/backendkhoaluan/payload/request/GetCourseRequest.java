package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.repository.CustomCourseQuery;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class GetCourseRequest extends CustomCourseQuery.CourseFilterParam {
    @Min(value = 0, message = Constants.ErrorMessageCourseValidation.START_SIZE)
    private Integer start = 0;
    @Range(min = 5, max = 50, message = Constants.ErrorMessageCourseValidation.LIMIT_SIZE)
    private Integer limit = 10;
}
