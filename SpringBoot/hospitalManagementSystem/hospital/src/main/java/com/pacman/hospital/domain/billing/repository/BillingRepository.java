package com.pacman.hospital.domain.billing.repository;

import com.pacman.hospital.domain.billing.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    List<Billing> findByPatientId(Long patientId);

    Billing findByAppointmentId(Long appointmentId);
}
