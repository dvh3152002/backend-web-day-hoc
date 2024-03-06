package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RolesRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

}