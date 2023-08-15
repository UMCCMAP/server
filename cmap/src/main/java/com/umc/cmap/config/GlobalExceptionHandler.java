package com.umc.cmap.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<Object>> handleBaseException(BaseException ex) {
        BaseResponseStatus status = ex.getStatus();
        BaseResponse<Object> response = new BaseResponse<>(status);
        return new ResponseEntity<>(response, status.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
