package com.pacman.hospital.domain.patient.service.impl;

import com.pacman.hospital.domain.patient.dto.PatientDto;
import com.pacman.hospital.domain.patient.mapper.PatientMapper;
import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.domain.patient.service.PatientService;
import com.pacman.hospital.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public PatientDto createPatient(PatientDto patientDto) {
        final Patient patient = patientMapper.toEntity(patientDto);
        final Patient saved = patientRepository.save(patient);
        return patientMapper.toDto(saved);
    }

    @Override
    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        final Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        // delegate field updates to mapper (ignores nulls)
        patientMapper.updateEntityFromDto(patientDto, existing);

        final Patient updated = patientRepository.save(existing);
        return patientMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientById(Long id) {
        final Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return patientMapper.toDto(patient);
    }

    @Override
    public void deletePatient(Long id) {
        final Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }
}
