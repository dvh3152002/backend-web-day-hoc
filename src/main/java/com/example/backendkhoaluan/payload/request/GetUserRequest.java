package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.repository.CustomUserQuery;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class GetUserRequest extends CustomUserQuery.UserFilterParam {
    @Min(value = 0, message = Constants.ErrorMessageUserValidation.START_SIZE)
    private int start = 0;
    @Range(min = 5, max = 50, message = Constants.ErrorMessageUserValidation.LIMIT_SIZE)
    private int limit = 10;
}
