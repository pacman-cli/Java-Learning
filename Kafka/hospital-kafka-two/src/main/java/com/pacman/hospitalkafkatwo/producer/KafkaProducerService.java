package com.pacman.hospitalkafkatwo.producer;

import com.pacman.hospitalkafkatwo.model.Patient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPatient(String topic, Patient patient) {
        String json = """
            {
                "patient": "%s",
                "medicine": "%s",
                "doctor": "%s",
                "notes": "%s"
            }
            """.formatted(patient.getName(), patient.getMedicine(), patient.getDoctor(), patient.getNotes());

        kafkaTemplate.send(topic, json);
        System.out.println("Sent: " + json);
    }
}
