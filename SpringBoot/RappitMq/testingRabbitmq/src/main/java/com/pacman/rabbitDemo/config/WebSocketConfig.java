package com.pacman.rabbitDemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry){
        //
        stompEndpointRegistry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000") //-> React dev server
                .withSockJS();
    }

    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry){
        //topic where server will send messages to clients
        messageBrokerRegistry.enableSimpleBroker("/topic");
        //prefix for a client to send messages to server
        messageBrokerRegistry.setApplicationDestinationPrefixes("/app");
    }

}
