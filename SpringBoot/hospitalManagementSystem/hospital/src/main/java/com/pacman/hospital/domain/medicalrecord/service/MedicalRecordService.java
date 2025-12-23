package com.pacman.hospital.domain.medicalrecord.service;

import com.pacman.hospital.domain.medicalrecord.dto.MedicalRecordDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MedicalRecordService {
    MedicalRecordDto createRecord(MedicalRecordDto dto);

    MedicalRecordDto updateRecord(Long id, MedicalRecordDto dto);

    MedicalRecordDto getRecordById(Long id);

    void deleteRecord(Long id);

    List<MedicalRecordDto> getAllRecords();

    List<MedicalRecordDto> getRecordsForPatient(Long patientId);

    MedicalRecordDto attachFileToRecord(Long recordId, MultipartFile file);
}
