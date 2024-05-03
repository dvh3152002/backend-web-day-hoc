package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.payload.request.*;
import com.example.backendkhoaluan.payload.response.AuthResponse;
import com.example.backendkhoaluan.payload.response.DashBoardResponse;
import com.example.backendkhoaluan.repository.CustomCourseDetailQuery;

import java.util.List;
import java.util.Set;

public interface AuthService {
    AuthResponse signIn(SignInRequest request);

    String register(CreateUserRequest request);

    String verifyAccount(VerifyAccountRequest request);

    String regenerateOTP(String email);

    String forgotPassword(ForgotPasswordRequest request);


    AuthResponse refreshToken(AuthResponse authResponse);

    Set<Integer> getCoursePurchased(CustomCourseDetailQuery.CourseDetailFilterParam param);

    void changePassword(Integer id, ChangePasswordRequest request);

    DashBoardResponse getDashBoard();
}
