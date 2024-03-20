package com.example.backendkhoaluan.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UsersDTO {
    private Integer id;
    private String email;
    private String fullname;
    private String address;
    private String avatar;
    private Set<RolesDTO> roles;
}
