package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.Sales;
import com.coffee_sales.backend.exception.AppUserServiceException;
import com.coffee_sales.backend.security.JwtPrincipal;
import com.coffee_sales.backend.service.AppUserService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appuser")
@CrossOrigin("*")
public class AppUserController {
    @Autowired
    private AppUserService appUserService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal JwtPrincipal principal){
        try{
            AppUser user = appUserService.findUserById(principal.userid());
            return ResponseEntity.ok(user);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch(AppUserServiceException e){
            return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/salesrecord/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getUserSales(@PathVariable @Positive Integer id){
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
