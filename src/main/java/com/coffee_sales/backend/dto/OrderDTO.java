package com.coffee_sales.backend.dto;

import com.coffee_sales.backend.entity.Order;
import com.coffee_sales.backend.entity.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @JsonProperty("items")
    private List<OrderItem> items;
//    @JsonProperty("customer_id")
//    private Integer customerId;
}
