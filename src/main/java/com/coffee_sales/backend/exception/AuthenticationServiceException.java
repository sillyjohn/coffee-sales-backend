package com.coffee_sales.backend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AuthenticationServiceException extends RuntimeException {
    private final HttpStatus status;

    public AuthenticationServiceException(String message) {
        this(message, HttpStatus.UNAUTHORIZED);
    }

    public AuthenticationServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public AuthenticationServiceException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

}
