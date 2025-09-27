package com.pacman.hospital.domain.billing.controller;

import com.pacman.hospital.domain.billing.dto.BillingDto;
import com.pacman.hospital.domain.billing.service.impl.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billings")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping
    public ResponseEntity<BillingDto> createBilling(
            @RequestBody BillingDto billingDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        BillingDto created = billingService.createBilling(billingDto);
        URI location = uriComponentsBuilder.path("/api/billings/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<BillingDto>> getAllBilling() {
        List<BillingDto> billings = billingService.getAllBilling();
        return ResponseEntity.ok(billings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingDto> getBillingById(@PathVariable Long id) {
        BillingDto billingDto = billingService.getBillingById(id);
        return ResponseEntity.ok(billingDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingDto> updateBilling(
            @PathVariable Long id,
            @RequestBody BillingDto billingDto
    ) {
        BillingDto updated = billingService.updateBilling(id, billingDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Long id) {
        billingService.deleteBilling(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Mark bill as paid. Accepts optional JSON body: {"paymentMethod":"CARD"}
     */
    public ResponseEntity<BillingDto> payBill(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> paymentInfo
    ) {
        String paymentMethod = (paymentInfo != null) ? paymentInfo.get("paymentMethod") : null;
        return ResponseEntity.ok(billingService.markAsPaid(id, paymentMethod));
    }
}
