package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.dto.OrderProducer;

import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.Order;
import com.coffee_sales.backend.entity.OrderItem;
import com.coffee_sales.backend.entity.OrderStatus;
import com.coffee_sales.backend.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class UserOrderController {
    @Autowired
    AppUserService appUserService;

    @Autowired
    OrderProducer orderProducer;

    @GetMapping("/send")
    public String sendMsg() {
        orderProducer.sendMessage("OrderQueue", "msg to rabbitmq");
        return "Message sent!";
    }

    @GetMapping("/placeordertest")
    public String placeordertest(){
        OrderItem orderItem = new OrderItem(1,"Espresso",4.5,3);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        AppUser appUser =appUserService.findUserById(1);
        Order order = new Order(9,items,appUser.getId(), OrderStatus.Order_Placed, LocalDateTime.now());
//        System.out.println("In controller:"+order.getId());
        return orderProducer.placeOrder(order);
    }

    @GetMapping("/placeorder")
    public String placeorder(@Valid @RequestBody Order order){

//        System.out.println("In controller:"+order.getId());
        return orderProducer.placeOrder(order);
    }



}
