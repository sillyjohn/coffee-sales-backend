package com.coffee_sales.backend.controller;
import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.exception.CoffeeServiceException;
import com.coffee_sales.backend.service.CoffeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/coffeelist")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping
    //@PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getCoffeeList(){
        List<Coffee> coffees = coffeeService.getAllCoffee();
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Access-Control-Allow-Origin", "http://localhost:5173");
        //headers.set("Access-Control-Allow-Credentials", "true");
        return ResponseEntity.ok().headers(headers).body(coffees);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getCoffeeById(@PathVariable Integer id){
        Coffee coffee = coffeeService.getCoffeeById(id);
        return ResponseEntity.ok(coffee);
    }
}
