package com.pacman.uberprojectauthservice.controllers;

import com.pacman.uberprojectauthservice.dto.AuthRequestDto;
import com.pacman.uberprojectauthservice.dto.PassengerDto;
import com.pacman.uberprojectauthservice.dto.PassengerSignupRequestDto;
import com.pacman.uberprojectauthservice.services.AuthService;
import com.pacman.uberprojectauthservice.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Value("${cookie.expiry}")
  private int cookieExpiry;

  public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authService = authService;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @PostMapping("/signup/passenger")
  public ResponseEntity<PassengerDto> signUp(@RequestBody PassengerSignupRequestDto dto) {
    return ResponseEntity.status(201).body(authService.toSignedInPassengerDto(dto));
    // return
    // ResponseEntity.status(201).body(authService.toSignedInPassengerDto(dto));
  }

  @PostMapping("/signing/passenger")
  public ResponseEntity<?> signIn(@RequestBody AuthRequestDto dto, HttpServletResponse response) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

    if (!auth.isAuthenticated())
      throw new UsernameNotFoundException("Invalid credentials");

    String token = jwtService.createToken(dto.getEmail());

    ResponseCookie cookie = ResponseCookie.from("JwtToken", token)
        .httpOnly(true) // ✅ secure for production
        .secure(false)
        .maxAge(cookieExpiry)
        .path("/")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    Map<String, Object> body = new HashMap<>();
    body.put("success", true);
    body.put("token", token);
    body.put("email", dto.getEmail());

    return ResponseEntity.ok(body);
  }

  @GetMapping("/ping")
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok("pong");
  }

  @GetMapping("/validate")
  public ResponseEntity<?> validateToken(HttpServletRequest request) {
    for (Cookie cookie : request.getCookies()) {
      System.out.println(cookie.getName() + " " + cookie.getValue());
    }
    return new ResponseEntity<>("Success", HttpStatus.OK);
  }

  // @GetMapping("/validate")
  // public ResponseEntity<?> validateToken(HttpServletRequest request) {
  // String token = null;
  //
  // if (request.getHeader("Authorization") != null &&
  // request.getHeader("Authorization").startsWith("Bearer ")) {
  // token = request.getHeader("Authorization").substring(7);
  // } else if (request.getCookies() != null) {
  // token = java.util.Arrays.stream(request.getCookies())
  // .filter(c -> c.getName().equals("JwtToken"))
  // .map(c -> c.getValue())
  // .findFirst()
  // .orElse(null);
  // }
  //
  // if (token == null || !jwtService.validateToken(token,
  // jwtService.extractEmail(token))) {
  // return ResponseEntity.status(401).body("Invalid or missing token");
  // }
  //
  // return ResponseEntity.ok(new AuthResponseDto(jwtService.extractEmail(token),
  // true));
  // }
}
