package com.coffee_sales.backend.service;

import com.coffee_sales.backend.dto.OrderProducer;
import com.coffee_sales.backend.entity.*;
import com.coffee_sales.backend.repository.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepo  orderRepo;
    @Autowired
    private OrderProducer orderProducer;

    @Transactional
    public Order createOrder(List<OrderItem> orderItemList, Integer customerid){
        Order newOrder = new Order();
        newOrder.setAppUserID(customerid);
        newOrder.setItems(orderItemList);
        newOrder.setOrderCreateTime(LocalDateTime.now());
        newOrder.setOrderStatus(OrderStatus.Placed);
        orderProducer.placeOrder(newOrder);
        orderRepo.save(newOrder);
        return newOrder;
    }

    @Transactional
    public Order updateOrderStatus_InProgress(Integer orderid){
        Order order = orderRepo.findById(orderid).orElseThrow();
        order.setOrderStatus(OrderStatus.In_Progress);
        orderProducer.updateOrder_InProgress(order);
        return order;
    }

    @Transactional
    public Order updateOrderStatus_Finished(Integer orderid){
        Order order = orderRepo.findById(orderid).orElseThrow();
        order.setOrderStatus(OrderStatus.Finished);
        orderProducer.updateOrder_Finished(order);
        return order;
    }

    @Transactional
    public Order updateOrderStatus_Cancelled(Integer orderid){
        Order order = orderRepo.findById(orderid).orElseThrow();
        order.setOrderStatus(OrderStatus.Cancelled);
        orderProducer.updateOrder_Cancelled(order);
        return order;
    }
}