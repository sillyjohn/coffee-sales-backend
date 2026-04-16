package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.dto.CategoryDTO;
import com.coffee_sales.backend.dto.CoffeeDTO;
import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.Categories;
import com.coffee_sales.backend.entity.Coffee;
import com.coffee_sales.backend.entity.Sales;
import com.coffee_sales.backend.exception.AppUserServiceException;
import com.coffee_sales.backend.exception.CoffeeServiceException;
import com.coffee_sales.backend.service.AppUserService;
import com.coffee_sales.backend.service.CoffeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminUserController {
    @Autowired
    AppUserService appUserService;
    @Autowired
    CoffeeService coffeeService;

    @GetMapping("/useridlist")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUserId(){
        try{
            List<Integer> list = appUserService.getUserIdList();
            return ResponseEntity.ok(list);
        }catch(AppUserServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/userlist")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUser(){
        try{
            List<AppUser> list = appUserService.getUserList();
            return ResponseEntity.ok(list);
        }catch(AppUserServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/usercount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserCount(){
        try{
            Integer count = appUserService.countUser();
            return ResponseEntity.ok(count);
        }catch(AppUserServiceException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/useridlist/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable @Positive Integer id){
        try{
            AppUser user = appUserService.findUserById(id);
            return ResponseEntity.ok(user);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(AppUserServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/usersales/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getUserSalesById(@PathVariable @Positive Integer id){
        try {
            List<Sales> list = appUserService.getSalesByUserId(id);
            return ResponseEntity.ok(list);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(AppUserServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @PostMapping("/addcoffee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewCoffee(@Valid @RequestBody CoffeeDTO coffeeDTO){
        try{
            Categories categories = coffeeService.getCategory(coffeeDTO.getCategoryname());
            Coffee coffee = coffeeService.createCoffee(coffeeDTO.getItemName(),coffeeDTO.getItemPrice(),categories);
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

    @PostMapping("/addcategory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoriesDTO){
        try{
            Categories newCategory = coffeeService.addCategory(categoriesDTO.getCategoryName());
            return ResponseEntity.status(201).body(Map.of("message",newCategory));
        }catch(CoffeeServiceException e){
            return ResponseEntity.status(400).body(Map.of("error",e.getMessage()));
        }
    }

    @DeleteMapping("/removecategory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeCategory(@PathVariable @Valid Integer id){
        try{
            coffeeService.removeCategory(id);
            return ResponseEntity.ok().body("Removed category:"+id);
        }catch(CoffeeServiceException e){
            return ResponseEntity.status(400).body(Map.of("error",e.getMessage()));
        }
    }

    @PutMapping("/updatecategory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modifyCategory(@Valid @PathVariable Integer id, @RequestBody CategoryDTO categoryDTO){
        try{

            coffeeService.modifyCategory(id,categoryDTO.getCategoryName());
            return ResponseEntity.status(200).body("Modify category: "+id+" to "+categoryDTO.getCategoryName());
        }catch(CoffeeServiceException e){
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }
}
