package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//TODO: FINISH USERCONTROLLER UNIT TEST
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    @Test
    void getAllCoffeeTest() throws Exception {
        Coffee coffee = new Coffee(1, "Espresso", new BigDecimal("5.00"));

        Mockito.when(userService.getAllCoffee()).thenReturn(List.of(coffee));

        mockMvc.perform(get("/api/coffeelist")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Espresso")));
    }

    @Test
    void getCoffeeById() throws Exception {
        Coffee coffee = new Coffee(1, "Espresso", new BigDecimal("5.00"));

        Mockito.when(userService.getCoffeeById(1)).thenReturn(coffee);

        mockMvc.perform(get("/api/coffeelist/1")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Espresso")));
    }

    @Test
    void addCoffeeTest() throws Exception {
        Coffee coffee = new Coffee(1, "Latte", new BigDecimal(4.5));

        Mockito.when(userService.addCoffee(coffee)).thenReturn(coffee);
        String coffeeJson = "{\"id\":1,\"name\":\"Latte\",\"price\":6.50}";

        mockMvc.perform(post("/api/coffeelist/addcoffee")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(coffeeJson))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Latte")));
    }
}