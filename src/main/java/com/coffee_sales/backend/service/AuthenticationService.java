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

    public void removeAppUserByAppUser(AppUser appUser){
        if(appUser.getId() == null &&!appUserRepo.existsById(appUser.getId())){
            throw new AuthenticationServiceException("AppUser does not exists.");
        }

        if(appUser.getEmail() == null && !appUserRepo.existsByEmail(appUser.getEmail())){
            throw new AuthenticationServiceException("AppUser does not exists.");
        }

        try{
            if(appUser.getId() != null){
                appUserRepo.deleteById(appUser.getId());
            }else if(appUser.getEmail() != null){
                appUserRepo.deleteByEmail(appUser.getEmail());
            }
        }catch(Exception e){
            throw new AuthenticationServiceException("Failed to delete appuser from database.");
        }
    }

    public void removeAppUserById(Integer id) {
        if(!appUserRepo.existsById(id)){
            throw new AuthenticationServiceException("AppUser does not exists.");
        }

        try{
            appUserRepo.deleteById(id);
        }catch(Exception e){
            throw new AuthenticationServiceException("Failed to remove delete appuser from the database.");
        }
    }

    public String login(@Valid AuthRequest authRequest) {
        if(!appUserRepo.existsByUsername(authRequest.getUsername())){
            throw new AuthenticationServiceException("Username not found.");
        }

        try{
            //Create an Authentication Object
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            if(authentication.isAuthenticated() && isAdmin(authentication) ){
                return jwtUtil.generateAdminToken(authRequest.getUsername());
            }else if(authentication.isAuthenticated()){
                return jwtUtil.generateToken(authRequest.getUsername());
            }else{
                throw new AuthenticationServiceException("Authentication Failed");
            }
        }catch(Exception e){
            throw new AuthenticationServiceException("Invalid username or password.");
        }

    }

    private boolean isAdmin(Authentication authentication){
        return authentication.getAuthorities()
                             .stream()
                             .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
