package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.UserDTO;
import com.example.backendkhoaluan.payload.response.ResponseData;
import com.example.backendkhoaluan.service.imp.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(){
        List<UserDTO> list= userService.getAllUser(0,10);
        ResponseData responseData=new ResponseData();
        responseData.setData(list);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/getDetailUser/{id}")
    public String getDetailUser(@PathVariable("id") int id){
        return "getDetailUser "+id;
    }
}
