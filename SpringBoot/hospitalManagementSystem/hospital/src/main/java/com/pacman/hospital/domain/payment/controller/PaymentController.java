package com.pacman.hospital.domain.payment.controller;

import com.pacman.hospital.domain.payment.dto.PaymentDto;
import com.pacman.hospital.domain.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping
  public ResponseEntity<List<PaymentDto>> createPayment(@RequestBody PaymentDto paymentDto) {
    return ResponseEntity.ok(paymentService.createPayment(paymentDto));
  }

  @GetMapping
  public ResponseEntity<List<PaymentDto>> getAllPayments() {
    return ResponseEntity.ok(paymentService.getAllPayments());
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
    return ResponseEntity.ok(paymentService.getPaymentById(id));
  }

  @GetMapping("/invoice/{invoiceId}")
  public ResponseEntity<PaymentDto> getPaymentByInvoiceId(@PathVariable Long invoiceId) {
    return ResponseEntity.ok(paymentService.getPaymentByInvoiceId(invoiceId));
  }

  @GetMapping("/billing/{billingId}")
  public ResponseEntity<PaymentDto> getPaymentByBillingId(@PathVariable Long billingId) {
    return ResponseEntity.ok(paymentService.getPaymentByBillingId(billingId));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
    paymentService.deletePayment(id);
    return ResponseEntity.notFound().build();
  }
}
