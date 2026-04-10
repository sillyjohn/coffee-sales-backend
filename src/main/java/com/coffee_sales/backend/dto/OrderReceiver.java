package com.coffee_sales.backend.dto;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

public class OrderReceiver {

    @RabbitListener(queues = "OrderQueue")
    public void receiveMessage(String message) {
        System.out.println("Received: " + message);
    }
}