package com.coffee_sales.backend.security;

import com.coffee_sales.backend.service.CustomerUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException
    {
        try{
            String header = request.getHeader("Authorization");
            String token = null;
            String username = null;
            Integer userid = null;

            //Parse token
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
                System.out.println("Token received: " + token);
            }

            //parse username & construct an UserDetails & Authentication object
            if(token!= null && jwtUtil.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null){
                username = jwtUtil.getUsernameFromToken(token);
                userid = jwtUtil.extractAppUserId(token);

                UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
                JwtPrincipal principal = new JwtPrincipal(userid,username);
                //Authentication Object Construction
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch(Exception e){
            System.out.println("Cannot set user authentication: " + e);
        }
        //Move doFilter() outside of the try catch block,
        //this make sure request will always reach the controller.
        filterChain.doFilter(request, response);
    }
}
