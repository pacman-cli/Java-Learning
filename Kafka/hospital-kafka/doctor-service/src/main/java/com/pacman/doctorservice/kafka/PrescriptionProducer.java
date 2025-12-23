package com.pacman.doctorservice.kafka;

import com.pacman.doctorservice.model.Prescription;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionProducer {
    public final KafkaTemplate<String, Prescription> kafkaTemplate;

    public PrescriptionProducer(KafkaTemplate<String, Prescription> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPrescriptionEvent(Prescription prescription) {
        kafkaTemplate.send("prescriptions", prescription);
        System.out.println("✅ Prescription Event Sent: " + prescription);
    }
}
