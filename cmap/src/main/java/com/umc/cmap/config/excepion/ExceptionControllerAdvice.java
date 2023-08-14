package com.umc.cmap.config.excepion;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    protected ErrorResponse entityNotFound(EntityNotFoundException e) {
        return new ErrorResponse(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UnAuthorizedException.class)
    protected ErrorResponse unAuthorized(UnAuthorizedException e) {
        return new ErrorResponse(e);
    }
    
}
