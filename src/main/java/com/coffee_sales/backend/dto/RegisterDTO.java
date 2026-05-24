package com.coffee_sales.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class RegisterDTO {
    public String name;
    @NotEmpty(message = "Username cannot be empty")
    public String username;
    @NotEmpty(message = "Username cannot be empty")
    public String password;
    public long phoneNumber;
    public String email;
    public LocalDate dateOfBrith;
}
