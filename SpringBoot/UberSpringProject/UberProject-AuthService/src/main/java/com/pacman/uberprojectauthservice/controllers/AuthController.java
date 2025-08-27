package com.pacman.uberprojectauthservice.controllers;

import com.pacman.uberprojectauthservice.dto.AuthRequestDto;
import com.pacman.uberprojectauthservice.dto.AuthResponseDto;
import com.pacman.uberprojectauthservice.dto.PassengerDto;
import com.pacman.uberprojectauthservice.dto.PassengerSignupRequestDto;
import com.pacman.uberprojectauthservice.services.AuthService;
import com.pacman.uberprojectauthservice.services.JwtService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // sign up creation
    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response = authService.toSignedInPassengerDto(passengerSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // sign in creation
    @PostMapping("/signing/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse httpServletResponse) {
        System.out.println("AuthController.signIn: " + authRequestDto.getEmail() + " " + authRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        if (authentication.isAuthenticated()) {
            String jwtToken = jwtService.createToken(authRequestDto.getEmail());

//            Map<String, Object> mp = new HashMap<>();
//            mp.put("email", authRequestDto.getEmail());
            ResponseCookie responseCookie = ResponseCookie.from("JwtToken", jwtToken)
                    .httpOnly(true)
                    .secure(false) //if set true, then only https
                    .maxAge(cookieExpiry)
                    .path("/")
                    .build();
            httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            return new ResponseEntity<>(AuthResponseDto.builder()
                    .success(true)
                    .build(), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
}
