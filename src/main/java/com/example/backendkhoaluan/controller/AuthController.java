package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.constant.ErrorCodeDefs;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.request.CreateUserRequest;
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
import org.springframework.util.StringUtils;
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
    public BaseResponse signup(@Valid @RequestBody CreateUserRequest createUserRequest,
                               @RequestPart(name = "file",required = false) MultipartFile file) {
        return BaseResponse.success(userService.createUser(createUserRequest,file));
    }

    @GetMapping("/profile")
    public BaseResponse getProfile(@RequestHeader("Authorization") String header) {
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if(token!=null){
                String email=jwtUtilsHelper.verifyToken(token);

                if(email!=null){
                    UsersDTO usersDTO=userService.findByEmail(email);
                    return BaseResponse.success(usersDTO);
                }
            }
        }
        return BaseResponse.error(ErrorCodeDefs.ERR_HEADER_TOKEN_REQUIRED,ErrorCodeDefs.getMessage(ErrorCodeDefs.ERR_HEADER_TOKEN_REQUIRED));
    }
}
