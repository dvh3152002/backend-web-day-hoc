package com.example.backendkhoaluan.payload.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
    private String fullname;
    private String address;
    private String avartar;
    private int roleId;
}
