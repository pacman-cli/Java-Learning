package com.pacman.hospitalkafkatwo.controller;


import com.pacman.hospitalkafkatwo.model.Patient;
import com.pacman.hospitalkafkatwo.producer.KafkaProducerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    private final KafkaProducerService kafkaProducerService;

    public HospitalController( KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/patient")
    public String sendPatient(@RequestBody Patient patient) {
        kafkaProducerService.sendPatient("patients", patient);
        return "Patient sent!";
    }
}
