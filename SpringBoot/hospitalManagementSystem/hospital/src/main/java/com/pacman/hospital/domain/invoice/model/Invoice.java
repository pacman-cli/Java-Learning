package com.pacman.hospital.domain.invoice.model;


import com.pacman.hospital.domain.appointment.model.Appointment;
import com.pacman.hospital.domain.billing.model.Billing;
import com.pacman.hospital.domain.patient.model.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//This will tie billing + insurance + patients + appointments together.
@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amountDue;

    @Column(nullable = false)
    private LocalDateTime invoiceDate;

    //    @Column(nullable = false)
//    private String status; // PAID, PENDING, CANCELLED //this can be enum also
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvoiceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_id")
    private Billing billing;
}
