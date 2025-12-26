package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Kubernetes! Time: " + LocalDateTime.now();
    }

    @GetMapping("/health")
    public String health() {
        return "Application is healthy at " + LocalDateTime.now();
    }
}
