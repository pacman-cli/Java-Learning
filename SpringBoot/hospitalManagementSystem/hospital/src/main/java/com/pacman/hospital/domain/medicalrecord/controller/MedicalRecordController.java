package com.pacman.hospital.domain.medicalrecord.controller;

import com.pacman.hospital.domain.medicalrecord.dto.MedicalRecordDto;
import com.pacman.hospital.domain.medicalrecord.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecordDto> createRecord(
            @Valid @RequestBody MedicalRecordDto dto,
            UriComponentsBuilder uriBuilder) {
        MedicalRecordDto created = medicalRecordService.createRecord(dto);
        URI location = uriBuilder.path("/api/medical-records/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // Optional: upload file for a record (controller should delegate to a StorageService you implement)
    @PostMapping("/{id}/upload")
    public ResponseEntity<MedicalRecordDto> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        // NOTE: For now, placeholder — implement StorageService to store file and update filePath on record
//         String path = storageService.store(file);
//         MedicalRecordDto dto = new MedicalRecordDto();
//         dto.setFilePath(path);
//         MedicalRecordDto updated = medicalRecordService.updateRecord(id, dto);
        return ResponseEntity.status(501).build(); // Not implemented
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getRecordById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordDto>> getRecordsForPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getRecordsForPatient(patientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordDto dto
    ) {
        return ResponseEntity.ok(medicalRecordService.updateRecord(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        medicalRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
