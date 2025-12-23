package com.pacman.notificationservice.kafka;

import com.pacman.notificationservice.model.Prescription;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    @KafkaListener(topics = "prescriptions", groupId = "notification-group")
    public void consume(Prescription prescription ) {
        System.out.println("📢 Notification Service: Notify patient -> " + prescription.getPatient()
                + ", Medicine: " + prescription.getMedicine());
    }
}
