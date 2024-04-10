package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.response.AuthResponse;

import java.util.List;

public interface AuthService {
    AuthResponse signIn(SignInRequest request);
    AuthResponse refreshToken(AuthResponse authResponse);
}
