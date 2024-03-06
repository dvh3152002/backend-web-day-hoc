package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse signIn(SignInRequest request);
    AuthResponse refreshToken(AuthResponse authResponse);
}
