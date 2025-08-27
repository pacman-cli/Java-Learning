package com.pacman.uberprojectauthservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService implements CommandLineRunner {
    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;

    // This method will create a brand-new JWT token for the given user based on payload.
    public String createToken(Map<String, Object> payload, String email) {// on the jwt website there is a tool to
        // generate the token, and there we saw
        // information was stored in JSON format
        // for that we'll be using a map
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry * 1000L);

        return Jwts.builder()
                .claims(payload)
                .subject(email) //replaced username with email because email is unique
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .signWith(getSignKey())
                .compact();
    }

    public String createToken(String email) {
        return createToken(new HashMap<>(), email);
    }

    //    This Java method is used to extract and parse JWT (JSON Web Token) claims/payload from a JWT token string
    public Claims extractAllPayloads(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //This Java code defines a **generic utility method** for extracting specific claims from JWT tokens
    //Generics in java: Generics let you write type-safe, reusable code.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllPayloads(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * This method will check if the given token expiry was before the current time stamp of not.
     *
     * @param token JWT token to be checked
     * @return true if the token has expired, false otherwise
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    //secret key
    public Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    //Validate email and expiration
    public Boolean validateToken(String token, String email) {
        final String userEmailFetchedFromToken = extractEmail(token);
        return (userEmailFetchedFromToken.equals(email)) && !isTokenExpired(token);
    }

//    private String extractPhoneNumber(String token) {
//        Claims claims = extractAllPayloads(token);
//        return (String) claims.get("phoneNumber");
//    }

    //this returns the payload value for a given key in String format
//    private String extractPayload(String token, String payloadKey) {
//        Claims claims = extractAllPayloads(token);
//        return (String) claims.get(payloadKey);
//    }

    public Object extractPayload(String token, String payloadKey) {
        Claims claims = extractAllPayloads(token);
        return (Object) claims.get(payloadKey);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> mp = new HashMap<>();
        mp.put("email", "puspo@gmail.com");
        mp.put("phoneNumber", "+919999999999");
        String token = createToken(mp, "puspo@gmail.com");
//        System.out.println("Generated Token:" + token);
//        System.out.println("Token Expiry:" + extractExpiration(token));
//        System.out.println("Token Email:" + extractEmail(token));
//        System.out.println("Token Phone:" + extractPayload(token, "phoneNumber").toString());
//        System.out.println("Token Valid:" + validateToken(token, "puspo"));

    }
}
