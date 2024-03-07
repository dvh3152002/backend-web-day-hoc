package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.payload.request.SignInRequest;
import com.example.backendkhoaluan.payload.response.AuthResponse;
import com.example.backendkhoaluan.repository.UsersRepository;
import com.example.backendkhoaluan.service.imp.AuthService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthServiceImp implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public AuthResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
            User user=usersRepository.findByEmail(request.getEmail()).orElseThrow();
            String jwt=jwtUtilsHelper.generateToken(user.getEmail());
            String refreshToken=jwtUtilsHelper.generateRefreshToken(new HashMap<>(),user.getEmail());

            AuthResponse authResponse=new AuthResponse();
            authResponse.setRole(user.getRole().getName());
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
            authResponse.setRole(user.getRole().getName());
            response.setAccessToken(jwt);
            response.setRefreshToken(authResponse.getAccessToken());
        }
        return response;
    }
}
