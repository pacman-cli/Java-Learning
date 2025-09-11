package com.pacman.rabbitDemo.services;

import com.pacman.rabbitDemo.config.MemoryStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
    private final MemoryStorage memoryStorage;
    @Value("${app.rabbitmq.queue}")
    private String queue;

    @RabbitListener(queues = "#{queue.name}")
    public void receiveMessage(String message){
        System.out.println("Received message: " + message);
        memoryStorage.addMessage(message);
    }
}
