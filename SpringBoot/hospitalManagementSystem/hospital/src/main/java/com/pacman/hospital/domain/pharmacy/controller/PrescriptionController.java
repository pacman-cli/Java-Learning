package com.pacman.hospital.domain.pharmacy.controller;

import com.pacman.hospital.domain.pharmacy.dto.PrescriptionDto;
import com.pacman.hospital.domain.pharmacy.service.PrescriptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionDto> create(
        @RequestBody PrescriptionDto prescriptionDto
    ) {
        return ResponseEntity.ok(
            prescriptionService.createPrescription(prescriptionDto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionDto> update(
        @PathVariable Long id,
        @RequestBody @Valid PrescriptionDto prescriptionDto
    ) {
        return ResponseEntity.ok(
            prescriptionService.updatePrescription(id, prescriptionDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByPatient(
        @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(
            prescriptionService.getPrescriptionsByPatient(patientId)
        );
    }
}
