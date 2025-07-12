package com.coffee_sales.backend.exception;

public class CoffeeServiceException extends RuntimeException{
    public CoffeeServiceException(String message){
        super(message);
    }
    public CoffeeServiceException(String message, Throwable cause){
        super(message, cause);
    }
}
