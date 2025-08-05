package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.Sales;
import com.coffee_sales.backend.exception.AppUserServiceException;
import com.coffee_sales.backend.service.AppUserService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appuser")
@CrossOrigin("http://localhost:3000")
public class AppUserController {
    @Autowired
    private AppUserService appUserService;

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

    @GetMapping("/useridlist/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
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

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserCount(){
        try{
            Integer count = appUserService.countUser();
            return ResponseEntity.ok(count);
        }catch(AppUserServiceException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/usersales/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
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


}
