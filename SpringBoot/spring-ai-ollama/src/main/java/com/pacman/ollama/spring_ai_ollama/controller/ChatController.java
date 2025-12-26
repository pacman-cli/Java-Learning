package com.pacman.ollama.spring_ai_ollama.controller;

import com.pacman.ollama.spring_ai_ollama.entity.Tut;
import com.pacman.ollama.spring_ai_ollama.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ChatController {

//    private ChatClient ollamaChatModel;

    private ChatService chatService;
    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @RequestMapping("/chat")
    public ResponseEntity<List<Tut>> chat(@RequestParam(value = "message") String message) {
//        String responseContent = this.ollamaChatModel
//                .prompt(message)
//                .call()
//                .content();

        return ResponseEntity.ok(chatService.chat(message));
    }
}
