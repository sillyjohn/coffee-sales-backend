package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.AuthRequest;
import com.coffee_sales.backend.repository.AppUserRepo;
import com.coffee_sales.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody @Valid AppUser appUser){
        try{
            AppUser addedUser = authenticationService.registerAppUser(appUser);
            return ResponseEntity.ok(addedUser);
        }catch(Exception e){
            return  ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody @Valid AuthRequest authRequest){
        try{
            String token = authenticationService.login(authRequest);
            return ResponseEntity.ok().body(Map.of("token",token));
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }
}
