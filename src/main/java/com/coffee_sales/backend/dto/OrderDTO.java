package com.coffee_sales.backend.dto;

import com.coffee_sales.backend.entity.Order;
import com.coffee_sales.backend.entity.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @Valid
    @NotEmpty(message = "Items cannot be empty")
    @JsonProperty("items")
    private List<OrderItem> items;
//    @JsonProperty("customer_id")
//    private Integer customerId;
}
