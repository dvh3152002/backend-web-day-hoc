package com.example.backendkhoaluan.payload.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequest {
    @Email(regexp = ".+[@].+[\\.].+",message = "Email không hợp lệ")
    @NotBlank(message = "Thiếu email")
    private String email;

    @NotBlank(message = "Thiếu password")
    @Size(min = 6, message = "Password phải từ 6 kí tự trở lên")
    private String password;

    @NotBlank(message = "Thiếu họ tên")
    private String fullname;

    @NotBlank(message = "Thiếu địa chỉ")
    private String address;

    private String avatar;
    private int roleId=2;
}
