package com.coffee_sales.backend.service;

import com.coffee_sales.backend.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.coffee_sales.backend.config.OrderQueueConfig.orderid;


@Service
public class OrderService {

    public Order createOrder(List<OrderItem> orderItemList, AppUser user){
        Order newOrder = new Order();
        newOrder.setId(orderid);
        orderid++;
        newOrder.setAppUserID(user.getId());
        newOrder.setItems(orderItemList);
        newOrder.setOrderCreateTime(LocalDateTime.now());
        newOrder.setOrderStatus(OrderStatus.Order_Placed);
        return newOrder;
    }

    public Order updateOrderStatus_InProgress(Order order){

        return order;
    }
}
