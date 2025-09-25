package com.pacman.hospital.domain.medicalrecord.service.impl;

import com.pacman.hospital.domain.medicalrecord.dto.MedicalRecordDto;
import com.pacman.hospital.domain.medicalrecord.mapper.MedicalRecordMapper;
import com.pacman.hospital.domain.medicalrecord.model.MedicalRecord;
import com.pacman.hospital.domain.medicalrecord.respository.MedicalRecordRepository;
import com.pacman.hospital.domain.medicalrecord.service.MedicalRecordService;
import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public MedicalRecordDto createRecord(MedicalRecordDto medicalRecordDto) {
        Patient patient = patientRepository.findById(medicalRecordDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + medicalRecordDto.getPatientId()));

        MedicalRecord entity = MedicalRecordMapper.toEntity(medicalRecordDto);
        entity.setPatient(patient);
        MedicalRecord result = medicalRecordRepository.save(entity);
        return MedicalRecordMapper.toDto(result);
    }

    @Override
    public MedicalRecordDto updateRecord(Long id, MedicalRecordDto dto) {
        MedicalRecord existing = medicalRecordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "MedicalRecord not " +
                        "found with id: " + id));

        MedicalRecordMapper.updateEntityFromDto(dto, existing);

        if (dto.getPatientId() != null) { // Allow updating patient association if patientId is provided
            Patient patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));
            existing.setPatient(patient);
        }
        medicalRecordRepository.save(existing);
        return MedicalRecordMapper.toDto(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordDto getRecordById(Long id) {
        MedicalRecord entity = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id: " + id));
        return MedicalRecordMapper.toDto(entity);
    }

    @Override
    public void deleteRecord(Long id) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id: " + id));
        medicalRecordRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getRecordsForPatient(Long patientId) {
        return medicalRecordRepository.findByPatientIdOrderByCreatedAtDesc(patientId)
                .stream()
                .map(MedicalRecordMapper::toDto)
                .collect(Collectors.toList());
    }
}
