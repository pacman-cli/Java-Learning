package com.pacman.chatapp.chatapplication.controller;

import com.pacman.chatapp.chatapplication.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChatController {
    // (we are not using the default prefix "app" here) because we have defined a custom prefix for this mapping in the
    // WebsocketConfig class
    @MessageMapping("/sendMessage") //this mapping is used to receive messages from the client
    @SendTo("/topic/messages") //this mapping is used to send messages to the client -> this maps
    public ChatMessage sendMessage(@RequestBody ChatMessage chatMessage) {
        return chatMessage;
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat"; //this returns the name of the view (HTML file) beacuse we are using Thymeleaf that's why we need to return the name of the view
    }
}
