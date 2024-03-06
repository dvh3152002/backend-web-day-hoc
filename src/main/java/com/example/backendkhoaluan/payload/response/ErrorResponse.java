package com.example.backendkhoaluan.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private int code;
    private String message;
    private List<ErrorDetail> errors;
}
