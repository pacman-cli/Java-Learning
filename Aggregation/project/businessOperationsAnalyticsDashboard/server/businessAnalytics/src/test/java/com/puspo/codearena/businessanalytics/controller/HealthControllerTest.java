package com.puspo.codearena.businessanalytics.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

class HealthControllerTest {

    private MockMvc mockMvc;
    private HealthController healthController;

    @BeforeEach
    void setUp() {
        healthController = new HealthController();
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    void health_ShouldReturnSuccessMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Backend is running"));
    }

    @Test
    void health_ShouldReturnTextPlainContentType() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void health_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        // This test verifies the endpoint is accessible
        // In a real scenario with security, you'd verify no auth is needed
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
    }

    @Test
    void health_ShouldReturnConsistentResponse() throws Exception {
        // Make multiple calls to verify consistent response
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/health"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Backend is running"));
        }
    }

    @Test
    void health_ShouldRespondQuickly() throws Exception {
        // Verify the health check is fast (no external dependencies)
        long startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Health check should be very fast (< 1000ms even in test environment)
        assert duration < 1000 : "Health check took too long: " + duration + "ms";
    }

    @Test
    void health_ShouldReturnExactMessage() throws Exception {
        // Test exact message match
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("Backend is running")));
    }

    @Test
    void health_ShouldNotReturnNull() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void health_ShouldHaveCorrectEndpointMapping() throws Exception {
        // Verify the endpoint is at /api/health
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
        
        // Verify wrong paths return 404
        mockMvc.perform(get("/health"))
                .andExpect(status().isNotFound());
    }
}