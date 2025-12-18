package com.pacman.hospital.domain.patient.repository;

import com.pacman.hospital.domain.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    boolean existsByContactInfo(String contactInfo);
}
