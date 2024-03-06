package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.request.UserRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.payload.response.DataResponse;
import com.example.backendkhoaluan.service.imp.UserService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private Gson gson = new Gson();

    @PostMapping("/signin")
    public BaseResponse signin(@Valid @RequestBody SignInRequest data) {
        UsersDTO userDTO = userService.checkLogin(data.getEmail(), data.getPassword());
        DataResponse baseResponse = new DataResponse();

        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        Authentication authentication = authenticationManager.authenticate(user);
        authentication.getAuthorities();

        String token = jwtUtilsHelper.generateToken(String.valueOf(userDTO.getId()));

        baseResponse.setData(token);

        log.info("Response: " + baseResponse);

        return BaseResponse.success(token);
    }

    @PostMapping("/signup")
    public BaseResponse signup(@Valid @ModelAttribute UserRequest userRequest,
                               @RequestPart(name = "file",required = false) MultipartFile file) {
        return BaseResponse.success(userService.createUser(userRequest,file));
    }
}
