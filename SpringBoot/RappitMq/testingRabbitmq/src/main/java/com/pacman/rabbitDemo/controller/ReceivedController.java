package com.pacman.rabbitDemo.controller;

import com.pacman.rabbitDemo.config.MemoryStorage;
import com.pacman.rabbitDemo.entity.ReceivedMessage;
import com.pacman.rabbitDemo.repository.ReceivedMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/received")
@RequiredArgsConstructor
public class ReceivedController {
    private final MemoryStorage memoryStorage;
    private final ReceivedMessageRepository receivedMessageRepository;

    @GetMapping
    public List<ReceivedMessage> getAllMessages(){
        return receivedMessageRepository.findAll();
    }
}
