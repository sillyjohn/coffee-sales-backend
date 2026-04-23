package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.dto.OrderDTO;
import com.coffee_sales.backend.dto.OrderProducer;

import com.coffee_sales.backend.dto.OrderStatusDTO;
import com.coffee_sales.backend.entity.Order;
import com.coffee_sales.backend.entity.OrderItem;
import com.coffee_sales.backend.entity.OrderStatus;
import com.coffee_sales.backend.exception.OrderServiceException;
import com.coffee_sales.backend.security.JwtPrincipal;
import com.coffee_sales.backend.service.OrderService;
import com.coffee_sales.backend.service.ServerSideEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class UserOrderController {
    @Autowired
    OrderProducer orderProducer;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServerSideEventService sseService;

    @GetMapping("/send")
    public String sendMsg() {
        orderProducer.sendMessage("OrderQueue", "msg to rabbitmq");
        return "Message sent!";
    }

    @PostMapping("/placeorder")
    public ResponseEntity<?> placeordertest(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal JwtPrincipal jwtPrincipal){
        try{
            Integer customerId = jwtPrincipal.userid();
            List<OrderItem> items = orderDTO.getItems();
            Order order =  orderService.createOrder(items,customerId);
            return ResponseEntity.ok().body(Map.of("order",order));
        }catch(OrderServiceException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/listenOrderUpdate")
    public SseEmitter listenOrderUpdate(@AuthenticationPrincipal JwtPrincipal jwtPrincipal){
        return sseService.establishConnection(jwtPrincipal.userid());
    }
}
