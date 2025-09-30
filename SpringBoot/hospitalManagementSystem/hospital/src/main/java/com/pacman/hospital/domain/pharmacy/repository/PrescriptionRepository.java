package com.pacman.hospital.domain.pharmacy.repository;

import com.pacman.hospital.domain.pharmacy.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
