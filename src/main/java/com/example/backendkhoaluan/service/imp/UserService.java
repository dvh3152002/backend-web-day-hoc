package com.example.backendkhoaluan.service.imp;

import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.payload.request.UserRequest;
import com.example.backendkhoaluan.repository.CustomeUserQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Page<User> getAllUser(CustomeUserQuery.UserFilterParam param, PageRequest pageRequest);
    UsersDTO checkLogin(String email,String password);
    UsersDTO findById(int id);
    UsersDTO findByEmail(String email);
    void deleteUser(int id);
    String createUser(UserRequest userRequest, MultipartFile avatar);
    void updateUser(int id, UserRequest userRequest, MultipartFile avatar);
}
