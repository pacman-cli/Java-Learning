package com.pacman.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //clients subscribe here (for messages from the server)
        registry.enableSimpleBroker("/topic");
        //server sends messages to @MessageMapping (-->clients here) starts with this prefix
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //this is a websocket connection endpoint
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")// allow frontend (Next.js) to connect
                .withSockJS(); //fallback for browsers that don't support websockets'
    }
}
