package com.pacman.hospital.domain.payment.service;

import com.pacman.hospital.domain.payment.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    List<PaymentDto> createPayment(PaymentDto paymentDto);

    List<PaymentDto> getAllPayments();

    PaymentDto getPaymentById(Long id);

    PaymentDto getPaymentByInvoiceId(Long invoiceId); // returns most recent one

    PaymentDto getPaymentByBillingId(Long billingId); // returns most recent one

    void deletePayment(Long id);
}
