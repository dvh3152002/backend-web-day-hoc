package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyAccountRequest {
    @Size(min = 6,max=15, message = Constants.ErrorMessageUserValidation.PASSWORD_SIZE)
    private String email;

    @Size(min = 6,max=15, message = Constants.ErrorMessageUserValidation.PASSWORD_SIZE)
    private String otp;
}
