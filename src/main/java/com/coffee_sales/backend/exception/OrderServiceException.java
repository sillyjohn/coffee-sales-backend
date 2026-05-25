package com.coffee_sales.backend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class OrderServiceException extends RuntimeException {
    private final HttpStatus status;
    
    public OrderServiceException(String message) {
        this(message,HttpStatus.BAD_REQUEST);
    }

    public OrderServiceException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public OrderServiceException(String message, HttpStatus status, Throwable cause){
        super(message, cause);
        this.status = status;
    }
}
