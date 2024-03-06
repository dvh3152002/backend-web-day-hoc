package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.payload.request.UserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<UsersDTO> getAllUser(int page, int size);
    UsersDTO checkLogin(String email,String password);
    UsersDTO findById(int id);
    void deleteUser(int id);
    String createUser(UserRequest userRequest, MultipartFile avatar);
    void updateUser(int id, UserRequest userRequest, MultipartFile avatar);
}
