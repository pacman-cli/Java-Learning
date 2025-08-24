package com.pacman.uberprojectauthservice.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService implements CommandLineRunner {
    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;

    // This method will create a JWT token for the given user based on payload.
    public String createToken(Map<String, Object> payload, String username) {// on the jwt website there is a tool to
                                                                             // generate the token, and there we saw
                                                                             // information was stored in JSON format
                                                                             // for that we'll be using a map
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry * 1000L);
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
        return Jwts.builder()
                .claims(payload)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String,Object> mp = new HashMap<>();
        mp.put("email","puspo@gmail.com");
        mp.put("phone","+919999999999");
        String token = createToken(mp,"puspo");
        System.out.println("Generated Token:" + token);
    }
}
