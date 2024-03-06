package com.example.backendkhoaluan.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsertException extends RuntimeException{
    private String titleError;
    private String message;
}
