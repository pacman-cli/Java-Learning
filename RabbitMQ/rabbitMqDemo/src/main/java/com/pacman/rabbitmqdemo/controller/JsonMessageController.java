package com.pacman.rabbitmqdemo.controller;

import com.pacman.rabbitmqdemo.dto.User;
import com.pacman.rabbitmqdemo.producer.RabbitMQJsonProducer;
import com.pacman.rabbitmqdemo.producer.RabbitMQProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class JsonMessageController {
    private final RabbitMQJsonProducer rabbitMQJsonProducer;

    public JsonMessageController(RabbitMQJsonProducer rabbitMQJsonProducer) {
        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestBody User user){
        rabbitMQJsonProducer.sendMessage(user);
        return ResponseEntity.ok("JSON Message sent successfully to RabbitMQ.....");
    }
}
