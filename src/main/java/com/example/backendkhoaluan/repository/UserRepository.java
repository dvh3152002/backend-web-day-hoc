package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.dto.UserDTO;
import com.example.backendkhoaluan.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    UserEntity findByEmail(String email);
}
