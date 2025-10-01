package com.pacman.hospitalkafkatwo.consumer;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "patients", groupId = "hospital-group")
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}
