package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.payload.request.ChangePasswordRequest;
import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.response.AuthResponse;
import com.example.backendkhoaluan.repository.CustomCourseDetailQuery;

import java.util.List;
import java.util.Set;

public interface AuthService {
    AuthResponse signIn(SignInRequest request);
    AuthResponse refreshToken(AuthResponse authResponse);

    Set<Integer> getCoursePurchased(CustomCourseDetailQuery.CourseDetailFilterParam param);

    void changePassword(Integer id, ChangePasswordRequest request);
}
