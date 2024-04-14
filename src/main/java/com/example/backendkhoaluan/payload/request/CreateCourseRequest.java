package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.domain.validator.numberState.NumberNotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCourseRequest {
    @NotBlank(message = Constants.ErrorMessageCourseValidation.NAME_NOT_BLANK)
    @NotNull(message = Constants.ErrorMessageCourseValidation.NAME_NOT_BLANK)
    private String name;

    @NumberNotNull(message = Constants.ErrorMessageCourseValidation.PRICE_NOT_BLANK)
    private int price;

    private int discount=0;

    @NotBlank(message = Constants.ErrorMessageCourseValidation.DESCRIPTION_NOT_BLANK)
    private String description;

    @NumberNotNull(message = Constants.ErrorMessageCourseValidation.USER_ID_NOT_BLANK)
    private int teacherId;

    private boolean free;

    private float limitTime;

    @NumberNotNull(message = Constants.ErrorMessageCourseValidation.CATEGORY_ID_NOT_BLANK)
    private int categoryId;
}
