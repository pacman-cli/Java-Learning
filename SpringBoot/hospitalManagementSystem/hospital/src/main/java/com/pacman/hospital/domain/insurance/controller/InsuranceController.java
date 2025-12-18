package com.pacman.hospital.domain.insurance.controller;


import com.pacman.hospital.domain.insurance.dto.InsuranceDto;
import com.pacman.hospital.domain.insurance.service.InsuranceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {

    private final InsuranceService service;

    public InsuranceController(InsuranceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InsuranceDto> create(
            @Valid @RequestBody InsuranceDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        InsuranceDto created = service.create(dto);
        URI loc = uriBuilder.path("/api/insurance/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(loc).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<InsuranceDto>> byPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.getByPatientId(patientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsuranceDto> update(
            @PathVariable Long id,
            @Valid @RequestBody InsuranceDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

