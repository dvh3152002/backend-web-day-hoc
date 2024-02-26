package com.example.backendkhoaluan.security;

import com.example.backendkhoaluan.entity.UserEntity;
import com.example.backendkhoaluan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity=userRepository.findByEmail(email);
        System.out.println("userEntity: "+userEntity);
        if(userEntity==null){
            throw new UsernameNotFoundException("Email không tồn tại");
        }

        return new User(email,userEntity.getPassword(),new ArrayList<>());
    }
}
