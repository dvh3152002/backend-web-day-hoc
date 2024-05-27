package com.example.backendkhoaluan.payload.response;

import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Data
public class BaseResponse {
    private boolean success=false;
    private ErrorResponse error;

    public static <T> ResponseEntity<?> success(T data){
        BaseItemResponse<T> response=new BaseItemResponse<>();
        response.setSuccess(data);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<?> successListData(List<T> rows,Integer total){
        BaseListResponse<T> response=new BaseListResponse<>();
        response.setResult(rows,total);
        return ResponseEntity.ok(response);
    }

    public static <T> BaseResponse error(int code,String message){
        BaseResponse response=new BaseResponse();
        response.setSuccess(false);

        ErrorResponse err=new ErrorResponse();
        err.setMessage(message);
        err.setCode(code);
        response.setError(err);

        return response;
    }

    public static <T> BaseResponse error(int code,String message,List<ErrorDetail> errors){
        BaseResponse response=new BaseResponse();
        response.setSuccess(false);

        ErrorResponse err=new ErrorResponse();
        err.setMessage(message);
        err.setCode(code);
        err.setErrors(errors);

        response.setError(err);

        return response;
    }
}
