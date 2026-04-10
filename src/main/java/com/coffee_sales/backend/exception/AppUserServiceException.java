package com.coffee_sales.backend.exception;

public class AppUserServiceException extends RuntimeException{
    Boolean boo;

    public AppUserServiceException(String message){
        super(message);
    }
    public AppUserServiceException(String message, Throwable cause){
        super(message,cause);
    }
    public AppUserServiceException(String message, Boolean boo){
        super(message);
        this.boo = boo;
    }

    public boolean getValue(){
        return boo;
    }
}
