package com.coffee_sales.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeDTO {
    @JsonProperty("name")
    private String itemName;
    @JsonProperty("price")
    private BigDecimal itemPrice;
    @JsonProperty("category")
    private String categoryname;

}
