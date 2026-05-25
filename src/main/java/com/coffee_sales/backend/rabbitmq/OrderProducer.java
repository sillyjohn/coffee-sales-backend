package com.coffee_sales.backend.rabbitmq;

import com.coffee_sales.backend.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private DirectExchange directExchange;

    public void sendMessage(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

    public void placeOrder(Order order){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try{
            String jsonString = mapper.writeValueAsString(order);
            this.rabbitTemplate.convertAndSend(
                directExchange.getName(),
                "Order.Placed",
                jsonString
            );
            System.out.println(" [x] Sent '" + jsonString + "'");
        }catch(JsonProcessingException e){
            throw new RuntimeException();
        }
    }

    public void updateOrder_InProgress(Order order){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try{
            String jsonString = mapper.writeValueAsString(order);
            this.rabbitTemplate.convertAndSend(
                directExchange.getName(),
                "Order.InProgress",
                jsonString);
            System.out.println(" [x] Sent '" + jsonString + "'");
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    public void updateOrder_Finished(Order order){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try{
            String jsonString = mapper.writeValueAsString(order);
            this.rabbitTemplate.convertAndSend(
                directExchange.getName(),
                "Order.Finished",
                jsonString);
            System.out.println(" [x] Sent '" + jsonString + "'");
        }catch(JsonProcessingException e){
                throw new RuntimeException(e);
        }
    }

    public void updateOrder_Cancelled(Order order){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try{
            String jsonString = mapper.writeValueAsString(order);
            this.rabbitTemplate.convertAndSend(
                directExchange.getName(),
                "Order.Cancelled",
                jsonString);
            System.out.println(" [x] Sent '" + jsonString + "'");
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
