package com.pacman.surokkha.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple health endpoint for quick checks.
 */
@RestController
public class HealthController {
    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }
}
