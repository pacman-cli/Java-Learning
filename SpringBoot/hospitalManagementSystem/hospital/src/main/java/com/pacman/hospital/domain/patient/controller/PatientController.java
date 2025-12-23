package com.pacman.hospital.domain.patient.controller;

import com.pacman.hospital.domain.patient.dto.PatientDto;
import com.pacman.hospital.domain.patient.service.PatientService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientDto> createPatient(
        @Valid @RequestBody PatientDto patientDto,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        PatientDto newPatient = patientService.createPatient(patientDto);
        URI location = uriComponentsBuilder
            .path("/api/patients/{id}")
            .buildAndExpand(newPatient.getId())
            .toUri();
        return ResponseEntity.created(location).body(newPatient);
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<PatientDto>> searchPatients(
        @RequestParam(value = "q", required = false) String q,
        Pageable pageable
    ) {
        return ResponseEntity.ok(patientService.searchPatients(q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        PatientDto patientDto = patientService.getPatientById(id);
        return ResponseEntity.ok(patientDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(
        @PathVariable Long id,
        @Valid @RequestBody PatientDto dto
    ) {
        PatientDto updated = patientService.updatePatient(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get patient by user ID (for logged-in patient users)
     */
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<PatientDto> getPatientByUserId(
        @PathVariable Long userId
    ) {
        PatientDto patientDto = patientService.getPatientByUserId(userId);
        return ResponseEntity.ok(patientDto);
    }
}
