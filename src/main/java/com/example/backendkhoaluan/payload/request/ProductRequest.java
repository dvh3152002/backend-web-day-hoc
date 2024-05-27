package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = Constants.ErrorMessageProductValidation.TITLE_NOT_BLANK)
    private String title;

    @NotBlank(message = Constants.ErrorMessageProductValidation.INTRODUCTION_NOT_BLANK)
    private String introduction;

    @NotBlank(message = Constants.ErrorMessageProductValidation.LINK_NOT_BLANK)
    private String link;

    @NotBlank(message = Constants.ErrorMessageProductValidation.DESCRIPTION_NOT_BLANK)
    private String description;
}
