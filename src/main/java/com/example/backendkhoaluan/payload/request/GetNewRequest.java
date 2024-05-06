package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.repository.CustomeNewQuery;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class GetNewRequest extends CustomeNewQuery.NewFilterParam {
    @Min(value = 0, message = Constants.ErrorMessageNewValidation.START_SIZE)
    private int start = 0;
    @Range(min = 5, max = 50, message = Constants.ErrorMessageNewValidation.LIMIT_SIZE)
    private int limit = 10;
}
