package com.coffee_sales.backend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AppUserServiceException extends RuntimeException{
    Boolean boo;
    private final HttpStatus status;

    public AppUserServiceException(String message){
        this(message, HttpStatus.BAD_REQUEST);
    }
    public AppUserServiceException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
    public AppUserServiceException(String message, HttpStatus status, Throwable cause){
        super(message, cause);
        this.status = status;
    }

}
