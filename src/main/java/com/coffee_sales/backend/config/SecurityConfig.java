package com.coffee_sales.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/coffeelist/**")
                        .authenticated()
                        .anyRequest().permitAll()
                )
                //Enables HTTP Basic authentication
                .httpBasic(Customizer.withDefaults())
                //Disables CSRF protection
                .csrf(crsf->crsf.disable());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        var user = User.withUsername("coffee")
                .password("{noop}coffee123")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
