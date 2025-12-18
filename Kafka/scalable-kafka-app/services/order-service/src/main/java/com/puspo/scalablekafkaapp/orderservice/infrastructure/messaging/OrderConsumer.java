package com.puspo.scalablekafkaapp.orderservice.infrastructure.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    @KafkaListener(topics = "user.created", groupId = "order-service-group")
    public void handleNewUser(ConsumerRecord<String, String> record) {
        System.out.println("Received message from user.created(Order Service): " + record.value());
    }
}
