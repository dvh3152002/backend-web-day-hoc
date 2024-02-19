package com.example.backendkhoaluan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
@Entity(name = "roles")
public class RoleEntity {
    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<UserEntity> listUser;
}
