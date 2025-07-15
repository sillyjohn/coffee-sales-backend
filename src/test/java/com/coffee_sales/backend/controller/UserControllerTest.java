package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//TODO: FINISH USERCONTROLLER UNIT TEST
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(username = "coffee", password = "coffee123")
    void getAllCoffeeTest() throws Exception{
        Coffee coffee = new Coffee(1, "Espresso", new BigDecimal("5.00"));

        Mockito.when(userService.getAllCoffee()).thenReturn(List.of(coffee));

        mockMvc.perform(get("/api/coffeelist")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Espresso")));
    }

    @Test
    @WithMockUser(username = "coffee", password = "coffee123")
    void getCoffeeById() throws Exception{
        Coffee coffee = new Coffee(1, "Espresso", new BigDecimal("5.00"));

        Mockito.when(userService.getCoffeeById(1)).thenReturn(coffee);

        mockMvc.perform(get("/api/coffeelist/1")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Espresso")));
    }

    @Test
    @WithMockUser(username = "coffee", password = "coffee123")
    void addCoffeeTest() throws Exception{
        Coffee coffee = new Coffee(1, "Latte", new BigDecimal(4.5));
        String coffeeJson = "{\"id\":1,\"name\":\"Latte\",\"price\":4.50}";
        Mockito.when(userService.addCoffee(Mockito.any(Coffee.class))).thenReturn(coffee);
        mockMvc.perform(post("/api/coffeelist/addcoffee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(coffeeJson)
                        .with(csrf()))
                .andExpect(status().isCreated())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("Latte","Latte"));
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Latte")));
    }

    @Test
    @WithMockUser(username = "coffee", password = "coffee123")
    void removeCoffeeTest() throws Exception{
        Integer id = 1;
        mockMvc.perform(delete("/api/coffeelist/removecoffee/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Coffee deleted successfully")));
    }

}