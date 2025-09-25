package com.pacman.hospital.domain.billing.repository;

import com.pacman.hospital.domain.billing.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    Long id(Long id);
}
