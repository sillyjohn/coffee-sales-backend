package com.coffee_sales.backend.config;

import com.coffee_sales.backend.dto.OrderProducer;
import com.coffee_sales.backend.dto.OrderReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class OrderQueueConfig {
    public static Integer orderid = 0;

    @Bean
    public Queue Queue(){
        return new Queue("OrderQueue");
    }

    @Bean
    public OrderProducer orderSender(){
        return new OrderProducer();
    }

    @Bean
    public OrderReceiver orderReceiver(){
        return new OrderReceiver();
    }
}
