package com.coffee_sales.backend.exception;

import com.coffee_sales.backend.service.OrderService;

public class OrderServiceException extends RuntimeException {
    public OrderServiceException(String message) {
        super(message);
    }

    public OrderServiceException(String message, Throwable cause){
        super(message,cause);
    }
}
