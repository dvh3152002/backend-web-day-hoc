package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.dto.RoleDTO;
import com.example.backendkhoaluan.dto.UserDTO;
import com.example.backendkhoaluan.entity.CourseEntity;
import com.example.backendkhoaluan.entity.RoleEntity;
import com.example.backendkhoaluan.entity.UserEntity;
import com.example.backendkhoaluan.payload.request.SignUpRequest;
import com.example.backendkhoaluan.repository.UserRepository;
import com.example.backendkhoaluan.service.imp.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUser(int page, int size) {
        PageRequest pageRequest=PageRequest.of(page, size);
        Page<UserEntity> listData= userRepository.findAll(pageRequest);

        List<UserDTO> userDTOList = new ArrayList<>();

        for (UserEntity user : listData) {
            UserDTO userDTO=new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setAddress(user.getAddress());
            userDTO.setFullname(user.getFullname());
            userDTO.setEmail(user.getEmail());

            RoleDTO roleDTO=new RoleDTO();
            roleDTO.setId(user.getRole().getId());
            roleDTO.setName(user.getRole().getName());

            userDTO.setRole(roleDTO);

            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    @Override
    public UserDTO findById(int id) {
        Optional<UserEntity> userOptional= userRepository.findById(id);
        UserDTO userDTO=new UserDTO();
        if(userOptional.isPresent()){
            userOptional.ifPresent(user->{
                userDTO.setEmail(user.getEmail());
                userDTO.setAddress(user.getAddress());
                userDTO.setFullname(user.getFullname());
                userDTO.setId(user.getId());

                RoleDTO roleDTO=new RoleDTO();
                roleDTO.setId(user.getRole().getId());
                roleDTO.setName(user.getRole().getName());

                userDTO.setRole(roleDTO);
            });
            return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO checkLogin(String email, String password) {
        UserEntity user= userRepository.findByEmail(email);
        if(user!=null){
            if(passwordEncoder.matches(password,user.getPassword())) {
                UserDTO userDTO=new UserDTO();

                userDTO.setEmail(user.getEmail());
                userDTO.setAddress(user.getAddress());
                userDTO.setFullname(user.getFullname());
                userDTO.setId(user.getId());

                RoleDTO roleDTO=new RoleDTO();
                roleDTO.setId(user.getRole().getId());
                roleDTO.setName(user.getRole().getName());

                userDTO.setRole(roleDTO);

                return userDTO;
            }
        }
        return null;
    }

    @Override
    public boolean addUser(SignUpRequest signUpRequest) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(signUpRequest.getRoleId());

        UserEntity userEntity = new UserEntity();
        userEntity.setFullname(signUpRequest.getFullname());
        userEntity.setPassword(signUpRequest.getPassword());
        userEntity.setAddress(signUpRequest.getAddress());
        userEntity.setEmail(signUpRequest.getEmail());
        userEntity.setAvatar(signUpRequest.getAvartar());
        userEntity.setRole(roleEntity);

        try {
            userRepository.save(userEntity);

            return true;
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getLocalizedMessage());
            return false;
        }
    }
}
