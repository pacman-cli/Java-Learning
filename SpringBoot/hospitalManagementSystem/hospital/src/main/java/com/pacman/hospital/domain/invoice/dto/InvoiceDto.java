package com.pacman.hospital.domain.invoice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDto {
    private Long id;

    private BigDecimal totalAmount;

    private BigDecimal amountDue;

    private LocalDateTime invoiceDate;

    private String status; // PAID, PENDING, CANCELLED //this can be enum also

    private Long patientId;

    private Long appointmentId;

    private Long billingId;
}
