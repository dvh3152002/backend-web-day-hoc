package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/getAllUser")
    public ResponseEntity<UserDTO> getAllUser(){
        UserDTO user=new UserDTO();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getDetailUser/{id}")
    public String getDetailUser(@PathVariable("id") int id){
        return "getDetailUser "+id;
    }
}
