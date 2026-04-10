package com.coffee_sales.backend.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesRequest {
    @NotNull
    private Integer coffeeId;

    private Integer appUserId;

    //Walk-in customer info
    private String customerName;
    private Long customerPhone;
    private String customerEmail;

    public boolean isAppUser(){
        return appUserId != null;
    }

    public boolean hasWalkInCustomerInfo(){
        return customerName != null && customerPhone != null;
    }
}
