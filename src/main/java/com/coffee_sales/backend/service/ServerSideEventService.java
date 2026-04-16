package com.coffee_sales.backend.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.coffee_sales.backend.repository.OrderRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.coffee_sales.backend.entity.Order;
import com.coffee_sales.backend.entity.OrderStatus;


@Service
public class ServerSideEventService {

    @Autowired
    private OrderRepo orderRepo;

    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();
    
    // Establish Long last connections
    public SseEmitter establishConnection(Integer customerId){
        // Create a 15 mins connection
        SseEmitter emitter = new SseEmitter(15 * 60 * 1000L); // 30 min timeout
        
        // Store it in a concurrenthashmap so that we can look up later
        emitters.put(customerId, emitter);

        // Cleanup callbacks
        emitter.onCompletion(()-> emitters.remove(customerId));
        emitter.onTimeout(()-> emitters.remove(customerId));
        emitter.onError((e)-> emitters.remove(customerId));
        System.out.println("Connection established for user"+ customerId);
        return emitter;
    }

    // Update the client's about the order status
    public void sendOrderUpdate(Order order){
        SseEmitter emitter = emitters.get(order.getAppUserID());

        if(emitter == null) return;

        try{
            emitter.send(
                SseEmitter
                .event()
                .name("order-update")
                .data(order)
            );
            System.out.println("Updated pushed to user"+ order.getAppUserID());
            boolean isActive = hasActiveOrder(order.getAppUserID());
            if(order.getOrderStatus() == OrderStatus.Finished && !isActive || order.getOrderStatus() == OrderStatus.Cancelled && !isActive){
                emitter.complete();
                emitters.remove(order.getAppUserID());
            }
        }catch(IOException e){
            emitters.remove(order.getAppUserID());
        }
    }

    boolean hasActiveOrder(Integer customerId){
        List<OrderStatus> activeStatuses = List.of(OrderStatus.Placed, OrderStatus.In_Progress);
        return orderRepo.existsByAppUserIDAndOrderStatusIn(customerId, activeStatuses);
    }

}
