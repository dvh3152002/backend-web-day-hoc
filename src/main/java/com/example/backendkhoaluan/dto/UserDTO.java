package com.example.backendkhoaluan.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String email;
//    private String password;
    private String fullname;
    private String address;
}
