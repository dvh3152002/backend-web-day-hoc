package com.example.backendkhoaluan.payload.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String role;
}
