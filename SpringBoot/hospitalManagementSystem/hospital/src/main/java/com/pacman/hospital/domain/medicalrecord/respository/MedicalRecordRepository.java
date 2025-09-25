package com.pacman.hospital.domain.medicalrecord.respository;

import com.pacman.hospital.domain.medicalrecord.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientIdOrderByCreatedAtDesc(Long patientId); // Custom query to find records by patient ID ordered by creation date
}
