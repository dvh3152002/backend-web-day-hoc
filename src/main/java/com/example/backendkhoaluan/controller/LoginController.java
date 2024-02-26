package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.UserDTO;
import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.request.SignUpRequest;
import com.example.backendkhoaluan.payload.response.ResponseData;
import com.example.backendkhoaluan.service.imp.UserService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private Gson gson=new Gson();

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest data){
        ResponseData responseData=new ResponseData();
        UserDTO userDTO= userService.checkLogin(data.getEmail(),data.getPassword());
        if(userDTO!=null){
            String json=gson.toJson(userDTO);
            String token=jwtUtilsHelper.generateToken(json);
            responseData.setData(token);
        }else{
            responseData.setSuccess(false);
            responseData.setMessage("Đăng nhập thất bại");
            responseData.setStatusCode(201);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest){
        ResponseData responseData=new ResponseData();
        responseData.setData(userService.addUser(signUpRequest));

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
