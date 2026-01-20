package com.coffee_sales.backend.dto;

import com.coffee_sales.backend.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void sendMessage(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

    public String placeOrder(Order order){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try{
            String jsonString = mapper.writeValueAsString(order);
            this.rabbitTemplate.convertAndSend(queue.getName(),jsonString);
            System.out.println(" [x] Sent '" + jsonString + "'");
            return jsonString;
        }catch(JsonProcessingException e){
            throw new RuntimeException();
        }
    }
}
