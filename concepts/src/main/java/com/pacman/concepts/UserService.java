package com.pacman.concepts;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

// Lifecycle callbacks
@Component
public class UserService implements InitializingBean, DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("UserService destroyed");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("UserService initialized");
    }

    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("@PreDestroy");
    }
}
