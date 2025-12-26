package com.pacman.rabbitDemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketMessageController {
    private final SimpMessagingTemplate brokerMessagingTemplate;

    @PostMapping("/api/send")
    public void sendMessage(@RequestParam String msg){
        brokerMessagingTemplate.convertAndSend("/topic/messages",msg);
    }
}
