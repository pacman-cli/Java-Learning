package com.pacman.hospital.domain.laboratory.controller;

import com.pacman.hospital.domain.laboratory.dto.LabTestDto;
import com.pacman.hospital.domain.laboratory.model.LabTest;
import com.pacman.hospital.domain.laboratory.service.LabTestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lab-tests")
public class LabTestController {
    private final LabTestService labTestService;

    public LabTestController(LabTestService labTestService) {
        this.labTestService = labTestService;
    }

    @GetMapping
    public ResponseEntity<List<LabTestDto>> getAll() {
        return ResponseEntity.ok(labTestService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabTestDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(labTestService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LabTestDto> create(
            @RequestBody @Valid LabTestDto labTestDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        LabTestDto created = labTestService.create(labTestDto);
        URI location = uriComponentsBuilder.path("/api/lab-tests/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabTestDto> update(
            @PathVariable Long id,
            @RequestBody @Valid LabTestDto labTestDto
    ) {
        return ResponseEntity.ok(labTestService.update(id, labTestDto));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        labTestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
