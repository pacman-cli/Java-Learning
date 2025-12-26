package com.pacman.analyticsservice.kafka;

import com.pacman.analyticsservice.model.Prescription;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsConsumer {
    @KafkaListener(topics = "prescriptions", groupId = "analytics-group")
    public void listen(Prescription prescription){
        System.out.println("📊 Analytics Service: Logging prescription -> " + prescription.getPatient()
                + ", Medicine: " + prescription.getMedicine());
    }
}
