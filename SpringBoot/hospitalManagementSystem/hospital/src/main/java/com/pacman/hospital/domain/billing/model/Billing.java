package com.pacman.hospital.domain.billing.model;

import com.pacman.hospital.domain.appointment.model.Appointment;
import com.pacman.hospital.domain.insurance.model.Insurance;
import com.pacman.hospital.domain.patient.model.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "billings")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "billing_date", nullable = false)
    private LocalDateTime billingDate;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // e.g., "CREDIT_CARD", "CASH", "INSURANCE" //    @Enumerated(EnumType.STRING)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    //adding more columns for better billing management
    @Enumerated(EnumType.STRING)
    private BillingStatus status = BillingStatus.PENDING;

    @Column(length = 1000)
    private String description; // brief description or notes about the billing

    @Column(name = "paid_at")
    private LocalDateTime paidAt; // timestamp when the bill was paid

    @ManyToOne
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

    @Column(name = "covered_amount", precision = 38, scale = 2)
    private BigDecimal coveredAmount;

    @Column(name = "patient_payable", precision = 38, scale = 2)
    private BigDecimal patientPayable;
}