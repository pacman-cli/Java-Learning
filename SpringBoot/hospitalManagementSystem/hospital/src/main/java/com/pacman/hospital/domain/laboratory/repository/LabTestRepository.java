package com.pacman.hospital.domain.laboratory.repository;

import com.pacman.hospital.domain.laboratory.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {
}
