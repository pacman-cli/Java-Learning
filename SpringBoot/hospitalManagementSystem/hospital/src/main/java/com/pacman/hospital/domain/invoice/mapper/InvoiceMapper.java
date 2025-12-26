package com.pacman.hospital.domain.invoice.mapper;

import com.pacman.hospital.domain.invoice.dto.InvoiceDto;
import com.pacman.hospital.domain.invoice.model.Invoice;
import com.pacman.hospital.domain.invoice.model.InvoiceStatus;

public class InvoiceMapper {

    public static InvoiceDto toDto(Invoice e) {
        if (e == null) return null;
        InvoiceDto d = new InvoiceDto();
        d.setId(e.getId());
        d.setTotalAmount(e.getTotalAmount());
        d.setAmountDue(e.getAmountDue());
        d.setInvoiceDate(e.getInvoiceDate());
        d.setStatus(e.getStatus() != null ? e.getStatus().name() : null);
        d.setPatientId(e.getPatient() != null ? e.getPatient().getId() : null);
        d.setAppointmentId(e.getAppointment() != null ? e.getAppointment().getId() : null);
        d.setBillingId(e.getBilling() != null ? e.getBilling().getId() : null);
        return d;
    }

    public static Invoice toEntity(InvoiceDto d) {
        if (d == null) return null;
        Invoice e = new Invoice();
        e.setId(d.getId());
        e.setTotalAmount(d.getTotalAmount());
        e.setAmountDue(d.getAmountDue());
        e.setInvoiceDate(d.getInvoiceDate());
        if (d.getStatus() != null) {
            try {
                e.setStatus(InvoiceStatus.valueOf(d.getStatus()));
            } catch (IllegalArgumentException ignored) {
                e.setStatus(null);
            }
        }
        // Note: patient/appointment/billing associations should be set in service if needed.
        return e;
    }
}
