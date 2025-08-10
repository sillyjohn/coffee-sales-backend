package com.coffee_sales.backend.config;
import com.coffee_sales.backend.security.AuthTokenFilter;
import com.coffee_sales.backend.security.JwtAccessDeniedHandler;
import com.coffee_sales.backend.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//TODO: Add JWT...WIP
//TODO: Add 2FA
//TODO: Modify securityfilterchain and redirect user to login page in both mobile and web when not logged in / authenticated
//TODO: Allow user to user basic service as a guest...Maybe?
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    private AuthTokenFilter jwtFilter;
    @Autowired
    AuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    JwtAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth ->
                        auth
                            //Public Endpoint
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/api/sales/**").permitAll()
                            //Private Endpoint
                            .anyRequest().authenticated()
                )
                //Stateless Session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //Handle unauthorized access
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                                         .accessDeniedHandler(accessDeniedHandler)
                )
                .authenticationProvider(authenticationProvider())
                //Disables CSRF protection
                .csrf(csrf->csrf.disable())
                //Add before Spring's default security provider
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //AuthenticationProvider - Handles the "how" of authentication
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider(customerUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    //AuthenticationManager - Attempts to authenticate the passed Authentication object
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }



}
