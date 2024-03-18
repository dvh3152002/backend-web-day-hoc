package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.domain.validator.specialCharacters.SpecialCharaters;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UpdateUserRequest {
    @Email(regexp = ".+[@].+[\\.].+",message = Constants.ErrorMessageUserValidation.INVALID_EMAIL)
    @NotBlank(message = Constants.ErrorMessageUserValidation.EMAIL_NOT_BLANK)
    private String email;

    @NotBlank(message = Constants.ErrorMessageUserValidation.FULLNAME_NOT_BLANK)
    @SpecialCharaters
    private String fullname;

    @NotBlank(message = Constants.ErrorMessageUserValidation.ADDRESS_NOT_BLANK)
    private String address;

    private Set<Integer> roles;
}
