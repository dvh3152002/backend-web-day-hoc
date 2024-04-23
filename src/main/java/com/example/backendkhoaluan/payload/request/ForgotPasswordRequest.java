package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @Email(regexp = ".+[@].+[\\.].+",message = Constants.ErrorMessageUserValidation.INVALID_EMAIL)
    @NotBlank(message = Constants.ErrorMessageUserValidation.EMAIL_NOT_BLANK)
    private String email;

    @Size(min = 6,max=15, message = Constants.ErrorMessageUserValidation.PASSWORD_SIZE)
    private String password;

    @Size(min = 6,max=15, message = Constants.ErrorMessageUserValidation.OTP_NOT_BLANK)
    private String otp;
}
