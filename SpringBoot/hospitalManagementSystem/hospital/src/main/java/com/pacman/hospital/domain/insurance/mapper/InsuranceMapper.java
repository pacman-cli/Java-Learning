package com.pacman.hospital.domain.insurance.mapper;

import com.pacman.hospital.domain.insurance.dto.InsuranceDto;
import com.pacman.hospital.domain.insurance.model.Insurance;
import com.pacman.hospital.domain.patient.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class InsuranceMapper {

    public InsuranceDto toDto(Insurance e) {
        InsuranceDto d = new InsuranceDto();
        d.setId(e.getId());
        d.setProviderName(e.getProviderName());
        d.setPolicyNumber(e.getPolicyNumber());
        d.setCoverageDetails(e.getCoverageDetails());
        d.setValidFrom(e.getValidFrom());
        d.setValidTo(e.getValidTo());
        d.setPatientId(e.getPatient() != null ? e.getPatient().getId() : null);
        d.setCoveragePercent(e.getCoveragePercent());
        return d;
    }

    public Insurance toEntity(InsuranceDto d) {
        Insurance e = new Insurance();
        e.setId(d.getId());
        e.setProviderName(d.getProviderName());
        e.setPolicyNumber(d.getPolicyNumber());
        e.setCoverageDetails(d.getCoverageDetails());
        e.setValidFrom(d.getValidFrom());
        e.setValidTo(d.getValidTo());
        e.setCoveragePercent(d.getCoveragePercent());
        return e;
    }
}

