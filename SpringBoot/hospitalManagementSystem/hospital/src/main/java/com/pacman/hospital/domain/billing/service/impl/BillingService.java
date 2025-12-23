package com.pacman.hospital.domain.billing.service.impl;

import com.pacman.hospital.domain.billing.dto.BillingDto;
import java.util.List;

public interface BillingService {
    BillingDto createBilling(BillingDto billingDto);

    BillingDto getBillingById(Long id);

    BillingDto updateBilling(Long id, BillingDto billingDto);

    void deleteBilling(Long id);

    List<BillingDto> getAllBilling();

    List<BillingDto> getBillingsByPatient(Long patientId);

    BillingDto markAsPaid(Long id, String paymentMethod);
}
