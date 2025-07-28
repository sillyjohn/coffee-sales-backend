package com.coffee_sales.backend.service;

import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByUsername(username);

        if(appUser == null){
            throw new UsernameNotFoundException("User not found with username: "+ username);
        }

        //return an Userdetails object for authentication
        return org.springframework
                .security
                .core
                .userdetails
                .User
                .withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .roles("USER")
                .build();
    }
}
