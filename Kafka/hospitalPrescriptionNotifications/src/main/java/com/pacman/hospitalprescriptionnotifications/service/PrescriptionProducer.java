package com.pacman.hospitalprescriptionnotifications.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacman.hospitalprescriptionnotifications.model.Prescription;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PrescriptionProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void sendPrescriptionEvent(Prescription prescription) {
        try {
//            We’re serializing Prescription objects to JSON before sending.
            String prescriptionJson = objectMapper.writeValueAsString(prescription);
            kafkaTemplate.send("prescriptions", prescriptionJson);
            System.out.println("✅ Prescription Event Sent: " + prescriptionJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
