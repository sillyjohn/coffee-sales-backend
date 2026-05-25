package com.coffee_sales.backend.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffee_sales.backend.service.ServerSideEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coffee_sales.backend.entity.Order;

@Component
public class OrderReceiver {

    @Autowired 
    private ServerSideEventService sseService;

    // @RabbitListener(queues = "OrderQueue")
    // public void receiveMessage(String message) {
    //     System.out.println("Received: " + message);
    // }

    @RabbitListener(queues = "OrderQueue")
    public void receiveMessage(String message){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try{
            Order order = objectMapper.readValue(message,Order.class);
            sseService.sendOrderUpdate(order);
        }catch(Exception e){
            throw new RuntimeException();
        }
    }


}