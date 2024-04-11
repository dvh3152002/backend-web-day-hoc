package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.domain.validator.numberState.NumberNotNull;
import com.example.backendkhoaluan.repository.CustomOrderQuery;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class OrderRequest extends CustomOrderQuery.OrderFilterParam {
    @Min(value = 0, message = Constants.ErrorMessageOrderValidation.START_SIZE)
    private int start = 0;
    @Range(min = 5, message = Constants.ErrorMessageOrderValidation.LIMIT_SIZE)
    private int limit = 10;
}
