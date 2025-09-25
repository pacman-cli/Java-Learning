package com.pacman.hospital.domain.billing.mapper;

import com.pacman.hospital.domain.billing.dto.BillingDto;
import com.pacman.hospital.domain.billing.model.Billing;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BillingMapper {
    public BillingDto toDto(Billing billing) {
        if (billing == null) {
            return null;
        }
        BillingDto billingDto = new BillingDto();
        billingDto.setId(billing.getId());
        billingDto.setAmount(billing.getAmount());
        billingDto.setBillingDate(billing.getBillingDate());
        billingDto.setPaymentMethod(billing.getPaymentMethod());

        if (billing.getPatient() != null) {  // avoid null pointer exception
            billingDto.setPatientId(billing.getPatient().getId());
        }
        if (billing.getAppointment() != null) {
            billingDto.setAppointmentId(billing.getAppointment().getId());
        }
        return billingDto;
    }

    public Billing toEntity(BillingDto billingDto) {
        if (billingDto == null) {
            return null;
        }
        return Billing.builder()
                .id(billingDto.getId())
                .amount(billingDto.getAmount())
                .billingDate(billingDto.getBillingDate() != null ? billingDto.getBillingDate() : LocalDateTime.now()) // set the current time if not provided
                .paymentMethod(billingDto.getPaymentMethod())
                // patient and appointment must be set manually in service using their ids
                .build();
    }

    public void updateEntityFromDto(BillingDto billingDto, Billing entity) {
        if (billingDto.getAmount() != null) {
            entity.setAmount(billingDto.getAmount());
        }
        if (billingDto.getBillingDate() != null) {
            entity.setBillingDate(billingDto.getBillingDate());
        }
        if (billingDto.getPaymentMethod() != null) {
            entity.setPaymentMethod(billingDto.getPaymentMethod());
        }
        // patient and appointment updates handled in service
    }
}
