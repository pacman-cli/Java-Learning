package com.pacman.hospital.domain.billing.dto;

import com.pacman.hospital.domain.billing.model.BillingStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class BillingDto {
    private Long id;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private LocalDateTime billingDate;
    private String paymentMethod;

    @NotNull(message = "Patient Id is required")
    private Long patientId;

    private Long appointmentId;

    private BillingStatus status;

    private String description;

    private LocalDateTime paidAt;

    private Long insuranceId;
    private BigDecimal coveredAmount;
    private BigDecimal patientPayable;

}
