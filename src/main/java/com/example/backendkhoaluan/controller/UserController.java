package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.payload.request.UserRequest;
import com.example.backendkhoaluan.payload.response.DataResponse;
import com.example.backendkhoaluan.service.imp.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(){
        List<UsersDTO> list= userService.getAllUser(0,10);
        DataResponse dataResponse =new DataResponse();
        dataResponse.setData(list);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailUser(@PathVariable("id") int id){
        UsersDTO userDTO=userService.findById(id);
        DataResponse dataResponse =new DataResponse();
        dataResponse.setData(userDTO);
        return new ResponseEntity<>(dataResponse,HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id,@Valid @RequestBody UserRequest userRequest,
                                        @RequestPart(name = "file",required = false) MultipartFile file){
//        log.info("user: {}",id,fullname,address,file);
        log.info("user :{}",userRequest);
        log.info("file :{}",file);
        userService.updateUser(id,userRequest,file);
        DataResponse dataResponse =new DataResponse();
        dataResponse.setMessage("Cập nhật người dùng thành công");
        return new ResponseEntity<>(dataResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
        log.info("id: ",id);
        userService.deleteUser(id);
        DataResponse dataResponse =new DataResponse();
        dataResponse.setMessage("Xóa thành công");
        return new ResponseEntity<>(dataResponse,HttpStatus.OK);
    }
}
