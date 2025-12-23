package com.pacman.hospital.domain.insurance.repository;

import com.pacman.hospital.domain.insurance.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    List<Insurance> findAllByPatientId(Long id);

    List<Insurance> findByPatientIdAndValidFromLessThanEqualAndValidToGreaterThanEqual(Long patient_id,
            LocalDate validFrom, LocalDate validTo);

    // fallback: get all by patient
    List<Insurance> findByPatientId(Long patientId);
}
