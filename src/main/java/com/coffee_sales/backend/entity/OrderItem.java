package com.coffee_sales.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Integer itemID;
    private String name;
    private double price;
    private Integer quantity;
}
