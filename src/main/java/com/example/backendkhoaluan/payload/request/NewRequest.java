package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewRequest {
    @NotBlank(message = Constants.ErrorMessageNewValidation.TITLE_NOT_BLANK)
    private String title;

    @NotBlank(message = Constants.ErrorMessageNewValidation.DESCRIPTION_NOT_BLANK)
    private String description;
}
