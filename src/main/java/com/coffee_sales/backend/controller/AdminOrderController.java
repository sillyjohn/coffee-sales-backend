package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.dto.OrderDTO;
import com.coffee_sales.backend.dto.OrderStatusDTO;
import com.coffee_sales.backend.entity.Order;
import com.coffee_sales.backend.entity.OrderItem;
import com.coffee_sales.backend.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/order")
@CrossOrigin(origins = "*")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/placeorder/{customerId}")
    public ResponseEntity<?> placeOrder(@PathVariable Integer customerId, @RequestBody OrderDTO orderDTO){
        List<OrderItem> items = orderDTO.getItems();
        Order order =  orderService.createOrder(items,customerId);
        return ResponseEntity.ok().body(Map.of("order",order));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/updateorder/{orderid}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer orderid,
                                                @Valid @RequestBody OrderStatusDTO newStatus){
        Order updatedOrder = switch (newStatus.getOrderStatus()) {
            case In_Progress -> orderService.updateOrderStatus_InProgress(orderid);
            case Cancelled   -> orderService.updateOrderStatus_Cancelled(orderid);
            case Finished    -> orderService.updateOrderStatus_Finished(orderid);
            default -> throw new IllegalArgumentException("Unexpected value: " + newStatus.getOrderStatus());
        };
        return ResponseEntity.ok(Map.of("Updated Order", updatedOrder));
    }
}
