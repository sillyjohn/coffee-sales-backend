package com.coffee_sales.backend.security;
import com.coffee_sales.backend.entity.AppUser;
import com.coffee_sales.backend.exception.JwtUtilException;
import com.coffee_sales.backend.repository.AppUserRepo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long EXPIRATION;

    @Value("${jwt.admin_expiration}")
    private long ADMIN_EXPIRATION;

    private SecretKey SECRET_KEY;

    @Autowired
    private AppUserRepo appUserRepo;
    @PostConstruct
    public void init(){
        this.SECRET_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username){
        AppUser user = appUserRepo.findByUsername(username);
        return Jwts.builder()
                    .subject(username)
                    .claims(Map.of(
                            "userid",user.getId(),
                            "role","USER")
                    )
                    .issuer("CoffeeBackend")
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                    .notBefore(new Date())
                    .signWith(SECRET_KEY)
                    .compact();
    }

    public String generateAdminToken(String username) {
        AppUser user = appUserRepo.findByUsername(username);
        return Jwts.builder()
                .subject(username)
                .claims(Map.of(
                        "userid",user.getId(),
                        "role","ADMIN"))
                .issuer("CoffeeBackend-admin")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ADMIN_EXPIRATION))
                .notBefore(new Date())
                .signWith(SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token){
        try{
            Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
            return true;
        }catch(SecurityException e){
            throw new JwtUtilException("Invalid JWT signature:" + e.getMessage());
        }catch(MalformedJwtException e){
            throw new JwtUtilException("Invalid JWT token: " + e.getMessage());
        }catch(ExpiredJwtException e){
            throw new JwtUtilException("JWT token expired: " + e.getMessage());
        }catch(UnsupportedJwtException e){
            throw new JwtUtilException("JWT token unsupported: " + e.getMessage());
        }catch(IllegalArgumentException e){
            throw new JwtUtilException("JWT claim is empty: " + e.getMessage());
        }
    }

    public boolean isTokenExpired(String token){
        try {
            Claims claims= Jwts.parser()
                                  .verifyWith(SECRET_KEY)
                                  .build()
                                  .parseSignedClaims(token)
                                  .getPayload();
            return claims.getExpiration().before(new Date());
        }catch(ExpiredJwtException e) {
            return true;
        }catch(JwtException e){
            throw new JwtUtilException("Invalid token:" + e.getMessage());
        }
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                   .verifyWith(SECRET_KEY)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                ;
    }

    public Integer extractAppUserId(String token){
        return extractAllClaims(token).get("userid",Integer.class);
    }

    //TODO: JWT find username
    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }




}
