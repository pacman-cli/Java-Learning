package com.pacman.hospitalprescriptionnotifications.controller;

import com.pacman.hospitalprescriptionnotifications.model.Prescription;
import com.pacman.hospitalprescriptionnotifications.service.PrescriptionProducer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prescriptions")
public class prescriptionController {
    private final PrescriptionProducer prescriptionProducer;

    public prescriptionController(PrescriptionProducer prescriptionProducer) {
        this.prescriptionProducer = prescriptionProducer;
    }

    @RequestMapping("/create")
    public String createPrescription(
            @RequestBody Prescription prescription
            ){
//        String event = String.format("{\"patient\":\"%s\", \"medicine\":\"%s\"}", patient, medicine);
        prescriptionProducer.sendPrescriptionEvent(prescription);
        return "Prescription created for : " + prescription.getPatient() + "with medicine: " + prescription.getMedicine();
    }
}
