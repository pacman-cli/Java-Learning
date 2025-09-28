package com.pacman.hospital.domain.laboratory.repository;

import com.pacman.hospital.domain.laboratory.model.LabOrder;
import com.pacman.hospital.domain.laboratory.model.LabOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
    List<LabOrder> findByPatientId(Long patientId);

    List<LabOrder> findByStatus(LabOrderStatus status);
}
