package com.pacman.rabbitDemo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;
    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public void sendMessage(String message){
//        System.out.println("Sending message: " + message);
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
        System.out.println("[x] Sent:  "+ message);
    }
}
