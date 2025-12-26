package com.pacman.ollama.spring_ai_ollama.service.impl;

import com.pacman.ollama.spring_ai_ollama.entity.Tut;
import com.pacman.ollama.spring_ai_ollama.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    private ChatClient chatClient;

    public ChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public List<Tut> chat(String message) {
        //this is the system instruction for strictly following the JSON format
//        String systemInstruction = """
//                You are a JSON generator.
//                IMPORTANT RULES:
//                - Output ONLY a valid JSON array.
//                - Do NOT include any text before or after.
//                - Do NOT include markdown fences like ```json.
//                - Do NOT explain anything.
//
//                JSON format:
//                [
//                  {
//                    "title": "string",
//                    "content": "string",
//                    "createdYear": "string"
//                  }
//                ]
//                """;

        String prompt = message;
        Prompt prompt1 = new Prompt(message);

        List<Tut> tutorial = chatClient
                .prompt(prompt1)
                .call()
                .entity(new ParameterizedTypeReference<List<Tut>>() {
                });

        return tutorial;
    }
}
