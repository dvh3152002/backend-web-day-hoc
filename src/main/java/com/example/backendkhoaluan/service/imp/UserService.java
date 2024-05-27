package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.payload.request.CreateUserRequest;
import com.example.backendkhoaluan.payload.request.UpdateUserRequest;
import com.example.backendkhoaluan.repository.CustomUserQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Page<User> getAllUser(CustomUserQuery.UserFilterParam param, PageRequest pageRequest);
    UsersDTO checkLogin(String email,String password);
    UsersDTO findById(int id);
    UsersDTO findByEmail(String email);
    void deleteUser(int id);
    String createUser(CreateUserRequest createUserRequest, MultipartFile avatar);
    void updateUser(int id, UpdateUserRequest request, MultipartFile avatar);
}
