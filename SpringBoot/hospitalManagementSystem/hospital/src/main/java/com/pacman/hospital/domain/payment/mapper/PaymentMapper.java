package com.pacman.hospital.domain.payment.mapper;

import com.pacman.hospital.domain.payment.dto.PaymentDto;
import com.pacman.hospital.domain.payment.model.Payment;

import java.time.LocalDateTime;

public class PaymentMapper {
    public static PaymentDto toDto(Payment e) {
        if (e == null) {
            return null;
        }
        PaymentDto dto = new PaymentDto();
        dto.setId(e.getId());
        dto.setInvoiceId(e.getInvoice().getId() != null ? e.getInvoice().getId() : null);
        dto.setBillingId(e.getBilling().getId() != null ? e.getBilling().getId() : null);
        dto.setAmount(e.getAmount());
        dto.setPaidAt(e.getPaidAt());
        dto.setPaymentMethod(e.getPaymentMethod());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setReferenceNumber(e.getReferenceNumber());
        return dto;
    }

    public static Payment toEntity(PaymentDto d) {
        if (d == null) return null;
        Payment p = new Payment();
        p.setId(d.getId());
        p.setAmount(d.getAmount());
        p.setPaymentMethod(d.getPaymentMethod());
        p.setReferenceNumber(d.getReferenceNumber());
        p.setPaidAt(d.getPaidAt() != null ? d.getPaidAt() : LocalDateTime.now());
        return p;
    }
}
