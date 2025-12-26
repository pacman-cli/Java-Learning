package com.pacman.hospital.domain.invoice.controller;

import com.pacman.hospital.domain.invoice.dto.InvoiceDto;
import com.pacman.hospital.domain.invoice.model.InvoiceStatus;
import com.pacman.hospital.domain.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * Create invoice
     */
    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(
            @Valid @RequestBody InvoiceDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        InvoiceDto created = invoiceService.createInvoice(dto);
        URI location = uriBuilder.path("/api/invoices/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Update invoice (partial/full)
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDto> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceDto dto
    ) {
        InvoiceDto updated = invoiceService.updateInvoice(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete invoice
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get invoice by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    /**
     * Get invoice by billing id
     */
    @GetMapping("/billing/{billingId}")
    public ResponseEntity<InvoiceDto> getInvoiceByBillingId(@PathVariable Long billingId) {
        return ResponseEntity.ok(invoiceService.getInvoiceByBillingId(billingId));
    }

    /**
     * Mark invoice as paid (sets amountDue = 0 and status = PAID)
     */
    @PostMapping("/{id}/pay")
    public ResponseEntity<InvoiceDto> markAsPaid(@PathVariable Long id) {
        InvoiceDto paid = invoiceService.markInvoiceAsPaid(id);
        return ResponseEntity.ok(paid);
    }

    /**
     * Get invoices by status (PAID, PENDING, CANCELLED)
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InvoiceDto>> getByStatus(@PathVariable InvoiceStatus status) {
        return ResponseEntity.ok(invoiceService.getInvoicesByStatus(status));
    }

    /**
     * Get invoices for a patient
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<InvoiceDto>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByPatient(patientId));
    }

    /**
     * List all invoices
     */
    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getAll() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }
}
