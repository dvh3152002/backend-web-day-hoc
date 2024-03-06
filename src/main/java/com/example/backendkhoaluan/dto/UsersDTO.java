package com.example.backendkhoaluan.dto;

import lombok.Data;

@Data
public class UsersDTO {
    private Integer id;
    private String email;
    private String password;
    private String fullname;
    private String address;
    private String avatar;
    private Integer idRole;
}
