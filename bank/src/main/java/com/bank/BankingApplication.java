package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot Application Class
 * This is the entry point for the Enhanced Terminal Banking System
 * 
 * Spring Boot will automatically:
 * - Configure the application context
 * - Set up the database connection (H2 in-memory)
 * - Initialize all repositories, services, and components
 * - Start the embedded server
 * - Enable scheduled tasks (e.g., monthly interest calculation)
 */
@SpringBootApplication
@EnableScheduling // Enable scheduled tasks like monthly interest calculation
public class BankingApplication {

    /**
     * Main method - Application entry point
     * Starts the Spring Boot application
     * 
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Launch Spring Boot application
        // This will initialize the Spring context and start the application
        SpringApplication.run(BankingApplication.class, args);
    }
}

