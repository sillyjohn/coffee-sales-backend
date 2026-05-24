package com.coffee_sales.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeDTO {
    @NotEmpty(message = "Coffee name cannot be empty")
    @JsonProperty("name")
    private String itemName;
    @Positive(message = "Coffee price cannot be below 0")
    @JsonProperty("price")
    private BigDecimal itemPrice;
    @NotEmpty(message = "Coffee category cannot be empty")
    @JsonProperty("category")
    private String categoryname;
}
