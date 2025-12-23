package com.pacman.customersupport.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    //ChatClient is the central bean provided to interact with
    // LLMs (OpenAI, Anthropic, etc.)
    //chatModel is the model used to interact with LLMs
    //lets you call OpenAI, Anthropic, HuggingFace, etc.
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .build();
    }
}