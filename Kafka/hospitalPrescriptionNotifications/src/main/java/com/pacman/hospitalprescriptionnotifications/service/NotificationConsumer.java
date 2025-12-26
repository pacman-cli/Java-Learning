package com.pacman.hospitalprescriptionnotifications.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacman.hospitalprescriptionnotifications.model.Prescription;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    private final ObjectMapper objectMapper;

    public NotificationConsumer(ObjectMapper objectMapper) {
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "prescriptions", groupId = "hospital-consumer-group")
    public void handlePrescriptionEvent(String message)  {
        try {
            Prescription prescription = objectMapper.readValue(message, Prescription.class); //this will throw an exception if the message is not a valid Prescription object
            System.out.println("Notification Service: Prescription received -> " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
