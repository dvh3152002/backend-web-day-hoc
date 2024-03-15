package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.repository.CustomeRatingCourseQuery;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class GetRatingCourseRequest extends CustomeRatingCourseQuery.RatingCourseFilterParam {
    @Min(value = 0, message = Constants.ErrorMessageRatingValidation.START_SIZE)
    private int start = 0;
    @Range(min = 5, max = 50, message = Constants.ErrorMessageRatingValidation.LIMIT_SIZE)
    private int limit = 5;
}
