package com.example.backendkhoaluan.exception;

import com.example.backendkhoaluan.payload.response.ErrorDetail;
import lombok.Data;

import java.util.List;

@Data
public class ErrorDetailException extends RuntimeException{
    private final List<ErrorDetail> errorDetails;
    public ErrorDetailException(List<ErrorDetail> errorDetails) {
        this.errorDetails = errorDetails;
    }
}
