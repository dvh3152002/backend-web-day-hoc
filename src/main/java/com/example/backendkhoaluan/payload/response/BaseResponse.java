package com.example.backendkhoaluan.payload.response;

import lombok.Data;

@Data
public class BaseResponse {
    private int statusCode=200;
    private Object data;
    private String message="";
}
