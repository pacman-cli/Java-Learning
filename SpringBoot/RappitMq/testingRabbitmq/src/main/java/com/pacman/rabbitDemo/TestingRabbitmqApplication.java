package com.pacman.rabbitDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pacman.rabbitDemo")
public class TestingRabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestingRabbitmqApplication.class, args);
    }

}
