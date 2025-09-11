package com.pacman.rabbitDemo.controller;

import com.pacman.rabbitDemo.services.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:3000") --> if you want to allow cross-origin requests can use this or
// CorsConfig(WebMvcConfigurer)
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageProducer messageProducer;

    @PostMapping
    public String sendMessage(@RequestParam String msg){
        messageProducer.sendMessage(msg);
        return "Sent message: " + msg;
    }
}
