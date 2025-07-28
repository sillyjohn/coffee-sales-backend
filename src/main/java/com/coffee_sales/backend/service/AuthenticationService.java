package com.coffee_sales.backend.service;

import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.AuthRequest;
import com.coffee_sales.backend.exception.AuthenticationServiceException;
import com.coffee_sales.backend.repository.AppUserRepo;
import com.coffee_sales.backend.security.AuthEntryPointJwt;
import com.coffee_sales.backend.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class AuthenticationService {
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AppUser registerAppUser(AppUser newUser){
        if(appUserRepo.existsByUsername(newUser.getUsername())){
            throw new AuthenticationServiceException("Username has been taken.");
        }
        if(appUserRepo.existsByEmail(newUser.getEmail())){
            throw new AuthenticationServiceException("Email has been registered.");
        }

        try{
            //Encrypt password
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.setRole("ROLE_USER");
            return appUserRepo.save(newUser);
        }catch(DataAccessException e){
            throw new AuthenticationServiceException("Failed to register new user in the database.");
        }
    }

    //TODO: login WIP
    public String login(@Valid AuthRequest authRequest) {

        if(!appUserRepo.existsByUsername(authRequest.getUsername())){
            throw new AuthenticationServiceException("Username not found.");
        }

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            if(authentication.isAuthenticated()){
                return jwtUtil.generateToken(authRequest.getUsername());
            }else{
                throw new AuthenticationServiceException("Authentication Failed");
            }
        }catch(Exception e){
            throw new AuthenticationServiceException("Invalid username or password.");
        }

    }





}
