package com.coffee_sales.backend.controller;
import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.exception.CoffeeServiceException;
import com.coffee_sales.backend.service.CoffeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/coffeelist")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getCoffeeList(){
        try{
            List<Coffee> coffees = coffeeService.getAllCoffee();
            return ResponseEntity.ok(coffees);
        }catch(CoffeeServiceException e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getCoffeeById(@PathVariable Integer id){
        try{
            Coffee coffee = coffeeService.getCoffeeById(id);
            return ResponseEntity.ok(coffee);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }catch(CoffeeServiceException e){
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }

    }

    @PostMapping("/addcoffee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewCoffee(@Valid @RequestBody Coffee coffee){
        try{
            Coffee saveCoffee = coffeeService.addCoffee(coffee);
            return ResponseEntity.status(201).body(saveCoffee);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }catch(CoffeeServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @DeleteMapping("/removecoffee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeCoffee(@Valid @RequestBody Coffee coffee){
        try{
            coffeeService.removeCoffeeByCoffee(coffee);
            return ResponseEntity.ok(Map.of("message","Coffee deleted successfully."));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
        catch(CoffeeServiceException e){
            return ResponseEntity.status(400).body(Map.of("error:", e.getMessage()));
        }
    }

    @DeleteMapping("/removecoffee/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeCoffeeById(@PathVariable Integer id){
        try{
            coffeeService.removeCoffeeById(id);
            return ResponseEntity.ok(Map.of("message","Coffee deleted successfully."));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(CoffeeServiceException e){
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }


}
