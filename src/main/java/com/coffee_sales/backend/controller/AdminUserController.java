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
    public ResponseEntity<?> getAllUserId() {
        List<Integer> list = appUserService.getUserIdList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/userlist")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUser() {
        List<AppUser> list = appUserService.getUserList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/usercount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserCount() {
        Integer count = appUserService.countUser();
        return ResponseEntity.ok(count);

    }

    @GetMapping("/useridlist/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable @Positive Integer id) {
        AppUser user = appUserService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/usersales/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getUserSalesById(@PathVariable @Positive Integer id) {
        List<Sales> list = appUserService.getSalesByUserId(id);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addcoffee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addNewCoffee(@Valid @RequestBody CoffeeDTO coffeeDTO) {
        Categories categories = coffeeService.getCategory(coffeeDTO.getCategoryname());
        Coffee coffee = coffeeService.createCoffee(coffeeDTO.getItemName(), coffeeDTO.getItemPrice(), categories);
        Coffee saveCoffee = coffeeService.addCoffee(coffee);
        return ResponseEntity.status(201).body(saveCoffee);
    }

    @DeleteMapping("/removecoffee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeCoffee(@Valid @RequestBody Coffee coffee) {
        coffeeService.removeCoffeeByCoffee(coffee);
        return ResponseEntity.ok(Map.of("message", "Coffee deleted successfully."));
    }

    @DeleteMapping("/removecoffee/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeCoffeeById(@PathVariable Integer id) {
        coffeeService.removeCoffeeById(id);
        return ResponseEntity.ok(Map.of("message", "Coffee deleted successfully."));
    }

    @PostMapping("/addcategory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoriesDTO) {
        Categories newCategory = coffeeService.addCategory(categoriesDTO.getCategoryName());
        return ResponseEntity.status(201).body(Map.of("message", newCategory));
    }

    @DeleteMapping("/removecategory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeCategory(@PathVariable @Valid Integer id) {
        coffeeService.removeCategory(id);
        return ResponseEntity.ok().body("Removed category:" + id);
    }

    @PutMapping("/updatecategory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modifyCategory(@Valid @PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        coffeeService.modifyCategory(id, categoryDTO.getCategoryName());
        return ResponseEntity.status(200).body("Modify category: " + id + " to " + categoryDTO.getCategoryName());
    }
}
