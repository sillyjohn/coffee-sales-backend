package com.coffee_sales.backend.dto;

import com.coffee_sales.backend.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDTO {
    @NotNull(message = "Order Status is required")
    @JsonProperty("order_status")
    private OrderStatus orderStatus;
}
