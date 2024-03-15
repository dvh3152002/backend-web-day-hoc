package com.example.backendkhoaluan.security;

import com.example.backendkhoaluan.entities.Role;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity=usersRepository.findByEmail(email).orElseThrow();
        if(userEntity==null){
            throw new UsernameNotFoundException("User không tồn tại");
        }
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for (Role role : userEntity.getRoles()) {
            setAuths.add(new SimpleGrantedAuthority(role.getName()));
        }
        // Tạo 1 list nhận vào danh sách quyền theo chuẩn của Security
        List<GrantedAuthority> listRoles= new ArrayList<>(setAuths);
        // Tạo ra 1 quyền và gán tên quyền truy vấn được từ database đế add vào list role ở trên
        return new org.springframework.security.core.userdetails.User(email,userEntity.getPassword(), listRoles);
    }
}
