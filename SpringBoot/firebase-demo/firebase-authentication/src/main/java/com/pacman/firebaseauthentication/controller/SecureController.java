package com.pacman.firebaseauthentication.controller;

import com.google.firebase.auth.FirebaseAuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class SecureController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is public endpoint , no user authentication required";
    }

    @GetMapping("/user")
    public String userEndpoint() throws FirebaseAuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();

        return "Hello, authenticated user with UID: " + uid;
    }
}
