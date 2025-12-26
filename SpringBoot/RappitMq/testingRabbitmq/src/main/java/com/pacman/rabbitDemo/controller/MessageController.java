package com.pacman.rabbitDemo.controller;

import com.pacman.rabbitDemo.entity.ReceivedMessage;
import com.pacman.rabbitDemo.repository.ReceivedMessageRepository;
import com.pacman.rabbitDemo.services.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000") --> if you want to allow cross-origin requests can use this or
// CorsConfig(WebMvcConfigurer)
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageProducer messageProducer;
    private final ReceivedMessageRepository receivedMessageRepository;

    @PostMapping
    public String sendMessage(@RequestParam String msg){
        messageProducer.sendMessage(msg); //must go to rabbit
        return "Sent message: " + msg;
    }

//    @GetMapping
//    public Iterable<?> getAllMessages(){
//        return receivedMessageRepository.findAll();
//    }
    @GetMapping("/all")
    public List<ReceivedMessage> getAllMessages(){
        return receivedMessageRepository.findAll();
    }
}
