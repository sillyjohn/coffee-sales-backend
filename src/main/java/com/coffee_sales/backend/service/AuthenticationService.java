package com.coffee_sales.backend.service;

import com.coffee_sales.backend.dto.RegisterDTO;
import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.entity.AuthRequest;
import com.coffee_sales.backend.exception.AuthenticationServiceException;
import com.coffee_sales.backend.repository.AppUserRepo;
import com.coffee_sales.backend.security.AuthEntryPointJwt;
import com.coffee_sales.backend.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public AppUser registerAppUser(RegisterDTO newUserDTO) {
        if (appUserRepo.existsByUsername(newUserDTO.getUsername())) {
            throw new AuthenticationServiceException("Username has been taken.");
        }
        if (appUserRepo.existsByEmail(newUserDTO.getEmail())) {
            throw new AuthenticationServiceException("Email has been registered.");
        }

        try {
            // Create AppUser object
            AppUser newUser = new AppUser();
            newUser.setName(newUserDTO.getName());
            newUser.setUsername(newUserDTO.getUsername());
            newUser.setPhoneNumber(newUserDTO.getPhoneNumber());
            newUser.setEmail(newUserDTO.getEmail());
            newUser.setDateOfBirth(newUserDTO.getDateOfBrith());
            // Encrypt password
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.setRole("ROLE_USER");
            return appUserRepo.save(newUser);
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException("Failed to register new user in the database.");
        }
    }

    public void removeAppUserByAppUser(AppUser appUser) {
        if (appUser.getId() == null && !appUserRepo.existsById(appUser.getId())) {
            throw new AuthenticationServiceException("AppUser does not exists.");
        }

        if (appUser.getEmail() == null && !appUserRepo.existsByEmail(appUser.getEmail())) {
            throw new AuthenticationServiceException("AppUser does not exists.");
        }

        try {
            if (appUser.getId() != null) {
                appUserRepo.deleteById(appUser.getId());
            } else if (appUser.getEmail() != null) {
                appUserRepo.deleteByEmail(appUser.getEmail());
            }
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException("Failed to delete appuser from database.", HttpStatus.BAD_REQUEST,
                    e);
        }
    }

    public void removeAppUserById(Integer id) {
        if (!appUserRepo.existsById(id)) {
            throw new AuthenticationServiceException("AppUser does not exists.");
        }

        try {
            appUserRepo.deleteById(id);
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException("Failed to remove delete appuser from the database.");
        }
    }

    public String login(@Valid AuthRequest authRequest) {

        // Create an Authentication Object
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException("Login Failed: Invalid username or password", HttpStatus.UNAUTHORIZED, e);
        }

        // Authentication Successful
        if (authentication.isAuthenticated() && isAdmin(authentication)) {
            return jwtUtil.generateAdminToken(authRequest.getUsername());
        } else if (authentication.isAuthenticated()) {
            return jwtUtil.generateToken(authRequest.getUsername());
        }

        // Default throw
        throw new AuthenticationServiceException("Login Failed");
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
