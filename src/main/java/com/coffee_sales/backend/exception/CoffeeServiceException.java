package com.coffee_sales.backend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CoffeeServiceException extends RuntimeException{
    private final HttpStatus status;

    public CoffeeServiceException(String message){
        this(message,HttpStatus.BAD_REQUEST);
    }

    public CoffeeServiceException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public CoffeeServiceException(String message, HttpStatus status, Throwable cause){
        super(message, cause);
        this.status = status;
    }
}
