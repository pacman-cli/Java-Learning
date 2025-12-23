package com.pacman.hospitalprescriptionnotifications.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacman.hospitalprescriptionnotifications.model.Prescription;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsConsumer {
    private final ObjectMapper objectMapper;

    public AnalyticsConsumer(ObjectMapper objectMapper) {
        this.objectMapper = new ObjectMapper();
    }

    //Different consumer group → hospital-analytics-group.
    //Kafka sends all events to each consumer group independently.
    @KafkaListener(topics = "prescriptions", groupId = "hospital-analytics-group")
    public void handleAnalyticsEvent(String message) {
        try {
            Prescription prescription = objectMapper.readValue(message, Prescription.class);
            // Simulate analytics processing
            System.out.println("📊 Analytics: Processing prescription -> Patient: "
                    + prescription.getPatient() + ", Medicine: " + prescription.getMedicine());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
