package com.pacman.hospital.domain.patient.service.impl;

import com.pacman.hospital.domain.patient.dto.PatientDto;
import com.pacman.hospital.domain.patient.mapper.PatientMapper;
import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.domain.patient.service.PatientService;
import com.pacman.hospital.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(
        PatientRepository patientRepository,
        PatientMapper patientMapper
    ) {
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
        final Patient existing = patientRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Patient not found with id: " + id
                )
            );

        // delegate field updates to mapper (ignores nulls)
        PatientMapper.updateEntityFromDto(patientDto, existing);

        final Patient updated = patientRepository.save(existing);
        return patientMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientById(Long id) {
        final Patient patient = patientRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Patient not found with id: " + id
                )
            );
        return patientMapper.toDto(patient);
    }

    @Override
    public void deletePatient(Long id) {
        final Patient existing = patientRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Patient not found with id: " + id
                )
            );
        patientRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getAllPatients() {
        return patientRepository
            .findAll()
            .stream()
            .map(patientMapper::toDto)
            .collect(Collectors.toList());
    }

    // same as doctor service impl

    @Override
    @Transactional(readOnly = true)
    public Page<PatientDto> searchPatients(String q, Pageable pageable) {
        // here q can be full name or contact info
        if (q == null || q.isBlank()) {
            return patientRepository
                .findAll(pageable)
                .map(patientMapper::toDto);
        }
        return patientRepository
            .findByFullNameContainingIgnoreCaseOrContactInfoContainingIgnoreCase(
                q,
                q,
                pageable
            )
            .map(patientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientByUserId(Long userId) {
        final Patient patient = patientRepository
            .findByUserId(userId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Patient not found for user id: " + userId
                )
            );
        return patientMapper.toDto(patient);
    }
}
