package com.puspo.scalablekafkaapp.userservice.infrastructure.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {
    @KafkaListener(topics = "user.updated", groupId = "user-service-group")
    public void userUpdatedListener(ConsumerRecord<String, String> record) {
        System.out.println("Received message from user.updated (User consumer): " + record.value());
    }
}
