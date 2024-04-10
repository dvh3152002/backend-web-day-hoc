package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.dto.CoursesDTO;
import com.example.backendkhoaluan.dto.RolesDTO;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.CourseDetail;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.response.AuthResponse;
import com.example.backendkhoaluan.repository.CourseDetailRepository;
import com.example.backendkhoaluan.repository.UsersRepository;
import com.example.backendkhoaluan.service.imp.AuthService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AuthServiceImp implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    @Autowired
    private UsersRepository usersRepository;

    private ModelMapper modelMapper=new ModelMapper();

    private Gson gson = new Gson();

    @Override
    public AuthResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
            User user=usersRepository.findByEmail(request.getEmail()).orElseThrow();
            UsersDTO usersDTO=new UsersDTO();
            usersDTO.setEmail(user.getEmail());
            usersDTO.setId(user.getId());

            String jwt=jwtUtilsHelper.generateToken(gson.toJson(usersDTO));
            String refreshToken=jwtUtilsHelper.generateRefreshToken(new HashMap<>(),user.getEmail());

            AuthResponse authResponse=new AuthResponse();
            authResponse.setRoles(modelMapper.map(user.getRoles(), List.class));
            authResponse.setAccessToken(jwt);
            authResponse.setRefreshToken(refreshToken);

            return authResponse;
        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public AuthResponse refreshToken(AuthResponse authResponse) {
        AuthResponse response=new AuthResponse();
        String email= jwtUtilsHelper.extractEmail(authResponse.getAccessToken());
        User user=usersRepository.findByEmail(email).orElseThrow();
        if (jwtUtilsHelper.isTokenValid(authResponse.getRefreshToken(),user.getEmail())){
            String jwt=jwtUtilsHelper.generateToken(user.getEmail());
            authResponse.setRoles(modelMapper.map(user.getRoles(), List.class));
            response.setAccessToken(jwt);
            response.setRefreshToken(authResponse.getAccessToken());
        }
        return response;
    }
}
