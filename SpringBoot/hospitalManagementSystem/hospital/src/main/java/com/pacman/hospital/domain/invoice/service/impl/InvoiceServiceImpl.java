package com.pacman.hospital.domain.invoice.service.impl;

import com.pacman.hospital.domain.invoice.dto.InvoiceDto;
import com.pacman.hospital.domain.invoice.mapper.InvoiceMapper;
import com.pacman.hospital.domain.invoice.model.Invoice;
import com.pacman.hospital.domain.invoice.model.InvoiceStatus;
import com.pacman.hospital.domain.invoice.repository.InvoiceRepository;
import com.pacman.hospital.domain.invoice.service.InvoiceService;
import com.pacman.hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Override
    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        Invoice entity = InvoiceMapper.toEntity(invoiceDto);
        Invoice saved = invoiceRepository.save(entity);
        return InvoiceMapper.toDto(saved);
    }

    @Override
    public InvoiceDto updateInvoice(Long id, InvoiceDto invoiceDto) {
        Invoice existing = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        // update allowed fields
        if (invoiceDto.getInvoiceDate() != null) existing.setInvoiceDate(invoiceDto.getInvoiceDate());
        if (invoiceDto.getTotalAmount() != null) existing.setTotalAmount(invoiceDto.getTotalAmount());
        if (invoiceDto.getAmountDue() != null) existing.setAmountDue(invoiceDto.getAmountDue());
        if (invoiceDto.getStatus() != null) {
            try {
                existing.setStatus(InvoiceStatus.valueOf(invoiceDto.getStatus()));
            } catch (IllegalArgumentException e) {
                // invalid status string supplied
                throw new IllegalArgumentException("Invalid invoice status: " + invoiceDto.getStatus());
            }
        }

        // NOTE: If you want to allow updating patient/appointment/billing references,
        // map those here (typically you'd fetch and set the related entities).

        Invoice saved = invoiceRepository.save(existing);
        return InvoiceMapper.toDto(saved);
    }

    @Override
    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDto getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .map(InvoiceMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDto getInvoiceByBillingId(Long billingId) {
        return invoiceRepository.findByBillingId(billingId)
                .map(InvoiceMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with billing id: " + billingId));
    }

    @Override
    public InvoiceDto markInvoiceAsPaid(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        invoice.setAmountDue(java.math.BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.PAID);

        Invoice saved = invoiceRepository.save(invoice);
        return InvoiceMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDto> getInvoicesByPatient(Long patientId) {
        return invoiceRepository.findByPatientId(patientId)
                .stream()
                .map(InvoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDto> getInvoicesByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status)
                .stream()
                .map(InvoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDto> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(InvoiceMapper::toDto)
                .collect(Collectors.toList());
    }
}
