package com.example.backendkhoaluan.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class BaseResponse {
    private boolean success=false;
    private ErrorResponse error;

    public static <T> BaseItemResponse<T> success(T data){
        BaseItemResponse<T> response=new BaseItemResponse<>();
        response.setSuccess(data);
        return response;
    }

    public static <T> BaseListResponse<T> successListData(List<T> rows,Integer total){
        BaseListResponse<T> response=new BaseListResponse<>();
        response.setResult(rows,total);
        return response;
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
