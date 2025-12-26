package com.example.demo.controller;

import com.example.demo.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins ="*")
public class ChatController {
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessage chatMessage(ChatMessage chatMessage){
        return chatMessage; //broadcast to all
    }
}
