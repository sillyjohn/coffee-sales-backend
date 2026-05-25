package com.coffee_sales.backend.controller;

import com.coffee_sales.backend.dto.RegisterDTO;
import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.AuthRequest;
import com.coffee_sales.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    //Public Endpoint
    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody @Valid RegisterDTO registerDTO){
        try{
            AppUser addedUser = authenticationService.registerAppUser(registerDTO);
            return ResponseEntity.ok(addedUser);
        }catch(Exception e){
            return  ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    //Public Endpoint
    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody @Valid AuthRequest authRequest){
        try{
            HttpHeaders headers = new HttpHeaders();
//            headers.set("Access-Control-Allow-Credentials", "true");
            String token = authenticationService.login(authRequest);
            return ResponseEntity.ok().headers(headers).body(Map.of("token",token));
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>deleteUserRecordByAppUser(@RequestBody @Valid AppUser appUser){
        try{
            authenticationService.removeAppUserByAppUser(appUser);
            return ResponseEntity.ok(Map.of("message","AppUser record removed."));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>deleteUserRecordById(@PathVariable @Positive Integer id){
        try{
            authenticationService.removeAppUserById(id);
            return ResponseEntity.ok(Map.of("message","AppUser record removed."));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }
}
