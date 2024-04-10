package com.example.backendkhoaluan.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

public class PaymentException extends RuntimeException{
    public PaymentException(String message){super(message);}
}
