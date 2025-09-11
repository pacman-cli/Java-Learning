package com.pacman.rabbitDemo.services;

import com.pacman.rabbitDemo.config.MemoryStorage;
import com.pacman.rabbitDemo.entity.ReceivedMessage;
import com.pacman.rabbitDemo.repository.ReceivedMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
    private final MemoryStorage memoryStorage;
    private final ReceivedMessageRepository receivedMessageRepository;
    private final SimpMessagingTemplate brokerMessagingTemplate;
    @Value("${app.rabbitmq.queue}")
    private String queue;

    @RabbitListener(queues = "#{queue.name}")
    public void receiveMessage(String message){
        System.out.println("Received message: " + message);


        //saving to DB with a builder pattern
        ReceivedMessage saved = ReceivedMessage.builder()
                .content(message)
                .createdAt(LocalDateTime.now())
                .build();
        receivedMessageRepository.save(saved);

        //Store in memory
        memoryStorage.addMessage(message);

        //this will send a message to a client via websocket
        brokerMessagingTemplate.convertAndSend("/topic/messages",message);
    }
}
