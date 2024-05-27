package com.example.backendkhoaluan.exception;

import com.example.backendkhoaluan.constant.ErrorCodeDefs;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.payload.response.DataResponse;
import com.example.backendkhoaluan.payload.response.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Order(1)
    public BaseResponse handleException(Exception ex) {
        log.error("Exception: {}", ex);
        return BaseResponse.error(ErrorCodeDefs.ERR_OTHER, ex.getMessage());
    }

    @ExceptionHandler({InsertException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Order(-1)
    public BaseResponse handlerInsertException(InsertException e){
        log.error("InsertException: {}", e);
        DataResponse dataResponse=new DataResponse();
        dataResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        dataResponse.setMessage(e.getTitleError());
        dataResponse.setSuccess(false);

        return BaseResponse.error(ErrorCodeDefs.ERR_BAD_REQUEST, e.getTitleError());
    }

    @ExceptionHandler({DeleteException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Order(-1)
    public BaseResponse handlerDeleteException(DeleteException e){
        log.error("DeleteException: {}", e);
        DataResponse dataResponse=new DataResponse();
        dataResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        dataResponse.setMessage(e.getTitleError());
        dataResponse.setSuccess(false);

        return BaseResponse.error(ErrorCodeDefs.ERR_BAD_REQUEST, e.getTitleError());
    }

    @ExceptionHandler({UpdateException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Order(-1)
    public BaseResponse handlerUpdateException(UpdateException e){
        log.error("UpdateException: {}", e);
        DataResponse dataResponse=new DataResponse();
        dataResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        dataResponse.setMessage(e.getTitleError());
        dataResponse.setSuccess(false);

        return BaseResponse.error(ErrorCodeDefs.ERR_BAD_REQUEST, e.getTitleError());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Order(-1)
    public BaseResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException: {}", e);
        List<FieldError> errors=e.getBindingResult().getFieldErrors();
        List<ErrorDetail> errorDetails=new ArrayList<>();
        for (FieldError fieldError:errors) {
            ErrorDetail errorDetail=new ErrorDetail();
            errorDetail.setMessage(fieldError.getDefaultMessage());
            errorDetail.setId(fieldError.getField());

            errorDetails.add(errorDetail);
        }

        return BaseResponse.error(ErrorCodeDefs.ERR_VALIDATION,
                ErrorCodeDefs.getMessage(ErrorCodeDefs.ERR_VALIDATION),errorDetails);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Order(-1)
    public BaseResponse handleNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException: {}", ex);
        return BaseResponse.error(ErrorCodeDefs.ERR_VALIDATION, ex.getMessage());
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {BaseValidateException.class})
    @Order(1)
    public BaseResponse handleValidateException(BaseValidateException ex) {
        log.error("BaseValidateException: {}", ex);
        return BaseResponse.error(ErrorCodeDefs.ERR_VALIDATION, ex.getMessage());
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {DataNotFoundException.class})
    @Order(1)
    public BaseResponse handleValidateException(DataNotFoundException ex) {
        log.error("DataNotFoundException: {}", ex);
        return BaseResponse.error(ErrorCodeDefs.ERR_OBJECT_NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {FileException.class})
    @Order(1)
    public BaseResponse handleFileException(FileException ex) {
        log.error("FileException: {}", ex);
        return BaseResponse.error(ErrorCodeDefs.ERR_FILE, ex.getMessage());
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {RunCodeException.class})
    @Order(1)
    public BaseResponse handleRunCodeException(RunCodeException ex) {
        log.error("FileException: {}", ex);
        return BaseResponse.error(ErrorCodeDefs.ERR_FILE, ex.getMessage());
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {PaymentException.class})
    @Order(1)
    public BaseResponse handlePaymentException(PaymentException ex) {
        log.error("PaymentException: {}", ex);
        return BaseResponse.error(ErrorCodeDefs.ERR_FILE, ex.getMessage());
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {EmailException.class})
    @Order(1)
    public BaseResponse handlePaymentException(EmailException ex) {
        log.error("EmailException: {}", ex);
        return BaseResponse.error(ErrorCodeDefs.ERR_EMAIL_FAILED, ex.getMessage());
    }
}
