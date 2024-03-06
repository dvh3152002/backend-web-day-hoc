package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.request.UserRequest;
import com.example.backendkhoaluan.payload.response.AuthResponse;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.AuthService;
import com.example.backendkhoaluan.service.imp.UserService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private Gson gson = new Gson();

    @PostMapping("/signin")
    public BaseResponse signin(@Valid @RequestBody SignInRequest data) {
        AuthResponse authResponse=authService.signIn(data);

        return BaseResponse.success(authResponse);
    }

    @PostMapping("/refreshToken")
    public BaseResponse refreshToken(@RequestBody AuthResponse data) {
        AuthResponse authResponse=authService.refreshToken(data);

        return BaseResponse.success(authResponse);
    }

    @PostMapping("/signup")
    public BaseResponse signup(@Valid @ModelAttribute UserRequest userRequest,
                               @RequestPart(name = "file",required = false) MultipartFile file) {
        return BaseResponse.success(userService.createUser(userRequest,file));
    }
}
