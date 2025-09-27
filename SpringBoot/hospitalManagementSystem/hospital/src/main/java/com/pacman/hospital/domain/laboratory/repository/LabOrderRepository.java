package com.pacman.hospital.domain.laboratory.repository;

import com.pacman.hospital.domain.laboratory.model.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
}
