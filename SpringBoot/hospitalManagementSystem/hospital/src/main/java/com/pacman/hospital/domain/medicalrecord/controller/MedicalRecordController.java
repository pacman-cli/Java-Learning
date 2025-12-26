package com.pacman.hospital.domain.medicalrecord.controller;

import com.pacman.hospital.common.storage.StorageService;
import com.pacman.hospital.domain.medicalrecord.dto.MedicalRecordDto;
import com.pacman.hospital.domain.medicalrecord.service.MedicalRecordService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final StorageService storageService;

    public MedicalRecordController(
        MedicalRecordService medicalRecordService,
        StorageService storageService
    ) {
        this.medicalRecordService = medicalRecordService;
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecordDto> createRecord(
        @Valid @RequestBody MedicalRecordDto dto,
        UriComponentsBuilder uriBuilder
    ) {
        MedicalRecordDto created = medicalRecordService.createRecord(dto);
        URI location = uriBuilder
            .path("/api/medical-records/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadFile(
        @PathVariable Long id,
        @RequestParam("file") MultipartFile file
    ) {
        // Basic validation
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                "File is required and must not be empty."
            );
        }

        String storedPath; // Path where the file is stored
        try {
            // StorageService.store expects a (MultipartFile, String subfolder)
            storedPath = storageService.store(file, id.toString()); // Store in a subfolder named after the record ID
        } catch (IOException ex) {
            // log the error in real code
            return ResponseEntity.status(500).body(
                "Failed to store file: " + ex.getMessage()
            );
        }

        // Update the medical record with the stored file path
        MedicalRecordDto dto = new MedicalRecordDto();
        dto.setFilePath(storedPath); // Assuming MedicalRecordDto has a field 'filePath' to store the path

        MedicalRecordDto updated; // The updated record to return
        try {
            updated = medicalRecordService.updateRecord(id, dto); // Update the record with the new file path
        } catch (Exception ex) {
            // If updating the DB fails, consider removing the stored file or otherwise handling cleanup.
            return ResponseEntity.status(500).body(
                "File stored but failed to update record: " + ex.getMessage()
            );
        }

        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecordDto>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getRecordById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordDto>> getRecordsForPatient(
        @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(
            medicalRecordService.getRecordsForPatient(patientId)
        );
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

    /**
     * Single upload handler for attaching a file to an existing medical record. Note: consumes multipart/form-data so
     * Swagger will render the file picker.
     */
    @PostMapping(
        path = "/{id}/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE //this will make swagger show the file picker
    )
    public ResponseEntity<?> uploadFile(
        @Valid @PathVariable Long id,
        @RequestParam("file") MultipartFile file,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                "File is required and must not be empty."
            );
        }
        try {
            MedicalRecordDto medicalRecordDto =
                medicalRecordService.attachFileToRecord(id, file);
            URI location = uriComponentsBuilder
                .path("/api/medical-records/{id}")
                .buildAndExpand(medicalRecordDto.getId())
                .toUri(); // Location of the updated record
            //            return ResponseEntity.created(location).body(medicalRecordDto);
            return ResponseEntity.ok()
                .location(location)
                .body(medicalRecordDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                "File could not be attached to record: " + e.getMessage()
            );
        }
    }
}
