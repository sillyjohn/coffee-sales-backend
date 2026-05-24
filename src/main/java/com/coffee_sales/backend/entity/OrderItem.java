package com.coffee_sales.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {
    @NotNull(message = "Item id cannot be null")
    private Integer id;
    private String name;
    @Positive(message = "Item price cannot be below 0")
    private double price;
    @Positive(message = "Item quantity cannot be below 0")
    private Integer quantity;
}
