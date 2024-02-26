package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.UserDTO;
import com.example.backendkhoaluan.entity.UserEntity;
import com.example.backendkhoaluan.payload.request.SignUpRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUser(int page, int size);
    UserDTO checkLogin(String email,String password);
    UserDTO findById(int id);
    boolean addUser(SignUpRequest signUpRequest);
}
