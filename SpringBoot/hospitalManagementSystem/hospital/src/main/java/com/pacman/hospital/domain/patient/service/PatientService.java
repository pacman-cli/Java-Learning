package com.pacman.hospital.domain.patient.service;

import com.pacman.hospital.domain.patient.dto.PatientDto;
import com.pacman.hospital.domain.patient.model.Patient;

import java.util.List;

public interface PatientService {

    PatientDto createPatient(PatientDto patientDto);

    PatientDto updatePatient(Long id, PatientDto patientDto);

    PatientDto getPatientById(Long id);

    void deletePatient(Long id);

    List<PatientDto> getAllPatients();

}
