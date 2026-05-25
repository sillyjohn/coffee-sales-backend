package com.coffee_sales.backend.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // ---- this is the "combine" point ----
    private ResponseEntity<ErrorResponse> buildMsg(HttpStatus status, String message,
                                                HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message != null ? message : "Unexpected error",  // guard null
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }

    // Class-wise Exception Handler
    @ExceptionHandler(SalesServiceException.class)
    public ResponseEntity<ErrorResponse> SalesServiceExceptionHandler(SalesServiceException exception, HttpServletRequest request){
        return buildMsg(HttpStatus.BAD_REQUEST,exception.getMessage(), request);
    }

    @ExceptionHandler(AppUserServiceException.class)
    public ResponseEntity<ErrorResponse> AppUserServiceExceptionHandler(AppUserServiceException exception, HttpServletRequest request){
                return buildMsg(exception.getStatus(),exception.getMessage(), request);
    }

    @ExceptionHandler(CoffeeServiceException.class)
    public ResponseEntity<ErrorResponse> CoffeeServiceExceptionHandler(CoffeeServiceException exception, HttpServletRequest request){
        return buildMsg(exception.getStatus(), exception.getMessage(), request);
    }

    @ExceptionHandler(OrderServiceException.class)
    public ResponseEntity<ErrorResponse> OrderServiceExceptionHandler(OrderServiceException exception, HttpServletRequest request){
        return buildMsg(exception.getStatus(), exception.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> AuthenticationServiceException(AuthenticationServiceException exception, HttpServletRequest request){
        return buildMsg(exception.getStatus(), exception.getMessage(), request);
    }

    //General Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> ExceptionHandler(Exception exception, HttpServletRequest request){
        return buildMsg(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalArgumentExceptionHandler(IllegalArgumentException exception, HttpServletRequest request){
        return buildMsg(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> NotFoundExceptionHandler(NotFoundException exception, HttpServletRequest request){
        return buildMsg(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, HttpServletRequest request){
        return buildMsg( HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    
    
}
