package com.coffee_sales.backend.exception;

public class AppUserServiceException extends RuntimeException{
    public AppUserServiceException(String message){
        super(message);
    }
    public AppUserServiceException(String message, Throwable cause){
        super(message,cause);
    }
}
