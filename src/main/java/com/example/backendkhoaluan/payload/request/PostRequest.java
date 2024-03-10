package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message = Constants.ErrorMessagePostValidation.TITLE_NOT_BLANK)
    private String title;

    @NotBlank(message = Constants.ErrorMessagePostValidation.DESCRIPTION_NOT_BLANK)
    private String description;
}
