package com.pacman.hospital.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long id;

    private Long invoiceId;

    private Long billingId;

    private BigDecimal amount;

    private String paymentMethod; //this can be enum as well

    private String referenceNumber;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt;
}
