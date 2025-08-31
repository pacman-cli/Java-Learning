package com.pacman.todo.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Generate token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            System.out.println("Validating token: " + token.substring(0, Math.min(50, token.length())) + "...");
            System.out.println("Using secret key (first 20 chars): "
                    + secretKey.substring(0, Math.min(20, secretKey.length())) + "...");

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            System.out.println("Token validation successful");
            return true;
        } catch (JwtException e) {
            System.out.println("Token validation failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Unexpected error during token validation: " + e.getClass().getSimpleName() + " - "
                    + e.getMessage());
            return false;
        }
    }

    // Get username from token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
