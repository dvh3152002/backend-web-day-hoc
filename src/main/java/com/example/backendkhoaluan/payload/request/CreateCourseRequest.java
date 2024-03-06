package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCourseRequest {
    @NotBlank(message = Constants.ErrorMessageCourseValidation.NAME_NOT_BLANK)
    private String name;

    @NotBlank(message = Constants.ErrorMessageCourseValidation.PRICE_NOT_BLANK)
    private double price;

    private int discount=0;

    @NotBlank(message = Constants.ErrorMessageCourseValidation.DESCRIPTION_NOT_BLANK)
    private String description;

    @NotBlank(message = Constants.ErrorMessageCourseValidation.USER_ID_NOT_BLANK)
    private int userId;
}
