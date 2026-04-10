package com.coffee_sales.backend.security;

import org.springframework.stereotype.Component;

public record JwtPrincipal(Integer userid, String username) {
}
