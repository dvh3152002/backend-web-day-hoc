package com.example.backendkhoaluan.payload.response;

import lombok.Data;

@Data
public class DataResponse {
    private int statusCode=200;
    private Object data;
    private String message="";
    private boolean isSuccess=true;
}
