package com.pacman.hospital.domain.insurance.service;

import com.pacman.hospital.domain.insurance.dto.InsuranceDto;
import com.pacman.hospital.domain.insurance.model.Insurance;

import java.util.List;

public interface InsuranceService {
    InsuranceDto create(InsuranceDto insuranceDto);

    InsuranceDto update(Long id, InsuranceDto insuranceDto);

    InsuranceDto getById(Long id);

    List<InsuranceDto> getByPatientId(Long patientId);

    void delete(Long id);

}
