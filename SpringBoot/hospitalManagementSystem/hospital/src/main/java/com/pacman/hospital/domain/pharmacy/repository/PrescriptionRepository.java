package com.pacman.hospital.domain.pharmacy.repository;

import com.pacman.hospital.domain.pharmacy.model.Prescription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository
    extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientId(Long patientId);
}
