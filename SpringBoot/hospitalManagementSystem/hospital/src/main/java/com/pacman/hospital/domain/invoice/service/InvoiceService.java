package com.pacman.hospital.domain.invoice.service;

import com.pacman.hospital.domain.invoice.dto.InvoiceDto;
import com.pacman.hospital.domain.invoice.model.InvoiceStatus;

import java.util.List;

public interface InvoiceService {
    InvoiceDto createInvoice(InvoiceDto invoiceDto);

    InvoiceDto updateInvoice(Long id, InvoiceDto invoiceDto);

    void deleteInvoice(Long id);

    InvoiceDto getInvoiceById(Long id);

    InvoiceDto getInvoiceByBillingId(Long billingId);

    InvoiceDto markInvoiceAsPaid(Long id);

    List<InvoiceDto> getInvoicesByStatus(InvoiceStatus status);

    List<InvoiceDto> getInvoicesByPatient(Long patientId);

    List<InvoiceDto> getAllInvoices();
}
