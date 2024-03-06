package com.example.backendkhoaluan.security;

import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity=usersRepository.findByEmail(email);
        if(userEntity==null){
            throw new UsernameNotFoundException("User không tồn tại");
        }
        // Tạo 1 list nhận vào danh sách quyền theo chuẩn của Security
        List<GrantedAuthority> listRoles=new ArrayList<>();
        // Tạo ra 1 quyền và gán tên quyền truy vấn được từ database đế add vào list role ở trên
        SimpleGrantedAuthority role=new SimpleGrantedAuthority(userEntity.getRole().getName());
        listRoles.add(role);
        return new org.springframework.security.core.userdetails.User(email,userEntity.getPassword(),listRoles);
    }

    public UserDetails loadUserById(int id) throws UsernameNotFoundException {
        Optional<User> otpUserEntity=usersRepository.findById(id);
        User userEntity=otpUserEntity.get();
        if(userEntity==null){
            throw new UsernameNotFoundException("User không tồn tại");
        }
        // Tạo 1 list nhận vào danh sách quyền theo chuẩn của Security
        List<GrantedAuthority> listRoles=new ArrayList<>();
        // Tạo ra 1 quyền và gán tên quyền truy vấn được từ database đế add vào list role ở trên
        SimpleGrantedAuthority role=new SimpleGrantedAuthority(userEntity.getRole().getName());
        listRoles.add(role);
        return new org.springframework.security.core.userdetails.User(userEntity.getEmail(),userEntity.getPassword(),listRoles);
    }
}
