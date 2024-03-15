package com.example.backendkhoaluan.payload.response;

import com.example.backendkhoaluan.dto.RolesDTO;
import lombok.Data;

import java.util.List;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private List<RolesDTO> roles;
}
