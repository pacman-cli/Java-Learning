package com.pacman.hospital.domain.insurance.service.impl;

import com.pacman.hospital.domain.insurance.dto.InsuranceDto;
import com.pacman.hospital.domain.insurance.mapper.InsuranceMapper;
import com.pacman.hospital.domain.insurance.model.Insurance;
import com.pacman.hospital.domain.insurance.repository.InsuranceRepository;
import com.pacman.hospital.domain.insurance.service.InsuranceService;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InsuranceServiceImpl implements InsuranceService {
    private final InsuranceRepository insuranceRepository;
    private final InsuranceMapper insuranceMapper;
    private final PatientRepository patientRepository;

    public InsuranceServiceImpl(InsuranceRepository insuranceRepository, InsuranceMapper insuranceMapper, PatientRepository patientRepository) {
        this.insuranceRepository = insuranceRepository;
        this.insuranceMapper = insuranceMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    public InsuranceDto create(InsuranceDto insuranceDto) {
        var patient = patientRepository.findById(insuranceDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not foound with id: " + insuranceDto.getPatientId()));

        Insurance entity = insuranceMapper.toEntity(insuranceDto);
        entity.setPatient(patient);
        return insuranceMapper.toDto(insuranceRepository.save(entity));
    }

    @Override
    public InsuranceDto update(Long id, InsuranceDto insuranceDto) {
        Insurance existing = insuranceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found with id: " + id));


        if (insuranceDto.getProviderName() != null) {
            existing.setProviderName(insuranceDto.getProviderName());
        }
        if (insuranceDto.getPolicyNumber() != null) {
            existing.setPolicyNumber(insuranceDto.getPolicyNumber());
        }
        if (insuranceDto.getCoverageDetails() != null) {
            existing.setCoverageDetails(insuranceDto.getCoverageDetails());
        }
        if (insuranceDto.getValidFrom() != null) {
            existing.setValidFrom(insuranceDto.getValidFrom());
        }
        if (insuranceDto.getValidTo() != null) {
            existing.setValidTo(insuranceDto.getValidTo());
        }
        if (insuranceDto.getCoveragePercent() != null) {
            existing.setCoveragePercent(insuranceDto.getCoveragePercent());
        }
        if (insuranceDto.getPatientId() != null) {
            var patient = patientRepository.findById(insuranceDto.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + insuranceDto.getPatientId()));
            existing.setPatient(patient);
        }
        return insuranceMapper.toDto(insuranceRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public InsuranceDto getById(Long id) {
        return insuranceRepository.findById(id)
                .map(insuranceMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found " +
                        "with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceDto> getByPatientId(Long patientId) {
        return insuranceRepository.findAllByPatientId(patientId)
                .stream()
                .map(insuranceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        insuranceRepository.deleteById(id);
    }
}
