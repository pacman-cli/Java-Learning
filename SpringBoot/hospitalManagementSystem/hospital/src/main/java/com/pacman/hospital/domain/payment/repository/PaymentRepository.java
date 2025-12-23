package com.pacman.hospital.domain.payment.repository;

import com.pacman.hospital.domain.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoiceId(Long invoiceId);

    List<Payment> findByBillingId(Long billingId);
}
