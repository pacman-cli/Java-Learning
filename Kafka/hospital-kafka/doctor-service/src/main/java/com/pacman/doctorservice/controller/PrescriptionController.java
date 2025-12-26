package com.pacman.doctorservice.controller;

import com.pacman.doctorservice.kafka.PrescriptionProducer;
import com.pacman.doctorservice.model.Prescription;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    private final PrescriptionProducer prescriptionProducer;

    public PrescriptionController(PrescriptionProducer prescriptionProducer) {
        this.prescriptionProducer = prescriptionProducer;
    }

    @PostMapping("/create")
    public String createPrescription(@RequestBody Prescription prescription) {
        prescriptionProducer.sendPrescriptionEvent(prescription);
        return "Prescription sent for " + prescription.getPatient();
    }
}
