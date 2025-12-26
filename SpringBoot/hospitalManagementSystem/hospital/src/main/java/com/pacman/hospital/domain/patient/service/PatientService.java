package com.pacman.hospital.domain.patient.service;

import com.pacman.hospital.domain.patient.dto.PatientDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    PatientDto createPatient(PatientDto patientDto);

    PatientDto updatePatient(Long id, PatientDto patientDto);

    PatientDto getPatientById(Long id);

    void deletePatient(Long id);

    List<PatientDto> getAllPatients();

    // this is used to search for patients by name or contact info
    Page<PatientDto> searchPatients(String q, Pageable pageable);

    // Get patient by userId (link to User account)
    PatientDto getPatientByUserId(Long userId);
}
