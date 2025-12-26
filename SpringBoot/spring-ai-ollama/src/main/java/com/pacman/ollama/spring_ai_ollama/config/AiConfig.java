package com.pacman.ollama.spring_ai_ollama.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
//    @Bean
//    public ChatClient ollamaChatModel(OllamaChatModel ollamaChatModel){
//        return ChatClient.builder(ollamaChatModel).build();
//    }


    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        // Configure the builder with default options for the Ollama AI model
        return builder
                .defaultSystem("You are a helpful coding assistant. You are an expert in coding.")
                .defaultOptions(
                        OllamaOptions.builder()
                                .model("codellama:latest") // Use the latest available model
                                .temperature(0.7) // Set the temperature to 0.7
                                .build()
                ).build();
    }
}
