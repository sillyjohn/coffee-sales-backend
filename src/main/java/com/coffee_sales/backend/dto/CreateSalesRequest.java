package com.coffee_sales.backend.dto;

import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.Coffee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSalesRequest {
    @Valid
    @NotNull
    private Coffee coffee;
    @Valid
    @NotNull
    private AppUser appUser;
}
