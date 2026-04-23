package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.dto.OrderDTO;
import com.coffee_sales.backend.dto.OrderStatusDTO;
import com.coffee_sales.backend.entity.Order;
import com.coffee_sales.backend.entity.OrderItem;
import com.coffee_sales.backend.entity.OrderStatus;
import com.coffee_sales.backend.exception.OrderServiceException;
import com.coffee_sales.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/order")
@CrossOrigin(origins = "*")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/placeorder/{customerId}")
    public ResponseEntity<?> placeOrder(@PathVariable Integer customerId, @RequestBody OrderDTO orderDTO){
        try{
            List<OrderItem> items = orderDTO.getItems();
            Order order =  orderService.createOrder(items,customerId);
            return ResponseEntity.ok().body(Map.of("order",order));
        }catch(OrderServiceException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/updateorder/{orderid}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer orderid, @RequestBody OrderStatusDTO newStatus){
        try {
            if(newStatus == null){
                throw new OrderServiceException("Invalid order status");
            }
            if(orderid == null){
                throw new OrderServiceException("Invalid order id");
            }

            OrderStatus orderStatus = newStatus.getOrderStatus();
            if( orderStatus == null){
                throw new OrderServiceException("Invalid order status");
            }

            return switch (orderStatus) {
                case In_Progress -> {
                    Order updatedOrder = orderService.updateOrderStatus_InProgress(orderid);
                    yield ResponseEntity.ok().body(Map.of("Updated Order", updatedOrder));
                }
                case Cancelled ->{
                    Order updatedOrder = orderService.updateOrderStatus_Cancelled(orderid);
                    yield ResponseEntity.ok().body(Map.of("Updated Order", updatedOrder));
                }
                case Finished -> {
                    Order updatedOrder = orderService.updateOrderStatus_Finished(orderid);
                    yield ResponseEntity.ok().body(Map.of("Updated Order", updatedOrder));
                }
                default -> throw new OrderServiceException("Unable to update the order");
            };
        } catch (OrderServiceException e) {
            return ResponseEntity.status(400).body(Map.of("error",e.getMessage()));
        } catch( NoSuchElementException e){
            return ResponseEntity.status(404).body(Map.of("error","User not found"));
        }
    }
}
