package com.pacman.hospital.domain.payment.model;

import com.pacman.hospital.domain.billing.model.Billing;
import com.pacman.hospital.domain.invoice.model.Invoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // required: which invoice this payment is for
    @ManyToOne(fetch = FetchType.LAZY) //this is many payments can be made for one invoice
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    // optional link to bill
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Billing billing;

    @Column(nullable = false, precision = 38, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod; //this can be enum as well

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void createdAt() {
        this.createdAt = LocalDateTime.now();
    }
}
