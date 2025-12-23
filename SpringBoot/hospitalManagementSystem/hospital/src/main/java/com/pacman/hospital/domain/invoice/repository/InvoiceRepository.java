package com.pacman.hospital.domain.invoice.repository;

import com.pacman.hospital.domain.invoice.model.Invoice;
import com.pacman.hospital.domain.invoice.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPatientId(Long patientId);

    List<Invoice> findByStatus(InvoiceStatus status);

    Optional<Invoice> findByBillingId(Long billingId);
}
