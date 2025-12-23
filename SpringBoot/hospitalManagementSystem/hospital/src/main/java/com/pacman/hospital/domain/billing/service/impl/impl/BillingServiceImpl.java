package com.pacman.hospital.domain.billing.service.impl.impl;

import com.pacman.hospital.domain.appointment.repository.AppointmentRepository;
import com.pacman.hospital.domain.billing.dto.BillingDto;
import com.pacman.hospital.domain.billing.mapper.BillingMapper;
import com.pacman.hospital.domain.billing.model.Billing;
import com.pacman.hospital.domain.billing.model.BillingStatus;
import com.pacman.hospital.domain.billing.repository.BillingRepository;
import com.pacman.hospital.domain.billing.service.impl.BillingService;
import com.pacman.hospital.domain.insurance.model.Insurance;
import com.pacman.hospital.domain.insurance.repository.InsuranceRepository;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    private final BillingMapper billingMapper;
    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final InsuranceRepository insuranceRepository;

    public BillingServiceImpl(
        BillingMapper billingMapper,
        BillingRepository billingRepository,
        PatientRepository patientRepository,
        AppointmentRepository appointmentRepository,
        InsuranceRepository insuranceRepository
    ) {
        this.billingMapper = billingMapper;
        this.billingRepository = billingRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.insuranceRepository = insuranceRepository;
    }

    // @Override
    // public BillingDto createBilling(BillingDto billingDto) {
    // //convert dto to entity
    // Billing newBilling = billingMapper.toEntity(billingDto);
    //
    // // set patient and appointment using their ids from dto
    // newBilling.setPatient(patientRepository.findById(billingDto.getPatientId())
    // .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id:
    // " + billingDto.getPatientId())));
    //
    // newBilling.setAppointment(appointmentRepository.findById(billingDto.getAppointmentId())
    // .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with
    // id: " + billingDto.getAppointmentId()))
    // );
    // //save entity
    // billingRepository.save(newBilling);
    // //convert entity to dto
    // return billingMapper.toDto(newBilling);
    //
    // }

    @Override
    public BillingDto createBilling(BillingDto billingDto) {
        // find the patient first to ensure it exists
        var patient = patientRepository
            .findById(billingDto.getPatientId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Patient not found with id: " + billingDto.getPatientId()
                )
            );

        Billing billing = billingMapper.toEntity(billingDto);
        billing.setPatient(patient);

        // set appointment if provided
        if (billingDto.getAppointmentId() != null) {
            var appointment = appointmentRepository
                .findById(billingDto.getAppointmentId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Appointment not found with id: " +
                            billingDto.getAppointmentId()
                    )
                );
            billing.setAppointment(appointment);
        }

        // Ensure billing has a concrete billingDate (entity field), defaulting to now
        if (billing.getBillingDate() == null) {
            billing.setBillingDate(LocalDateTime.now());
        }

        // determine Insurance to use
        Insurance insurance = null; // this is nationalized to null and set only if valid insuranceId is provided
        if (billingDto.getInsuranceId() != null) {
            insurance = insuranceRepository
                .findById(billingDto.getInsuranceId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Insurance not found: " + billingDto.getInsuranceId()
                    )
                );
        } else {
            // try to find an active insurance for the patient on billing date
            // List<Insurance> insList =
            // insuranceRepository.findAllByPatientId(patient.getId());
            // var billingDateLocal = billing.getBillingDate().toLocalDate();
            // for (Insurance ins : insList) {
            // if ((ins.getValidFrom() == null ||
            // !ins.getValidFrom().isAfter(billingDateLocal))
            // && (ins.getValidTo() == null ||
            // !ins.getValidTo().isBefore(billingDateLocal))) {
            // insurance = ins; //if valid, then set insurance
            // break;
            // // Start date validation: ins.getValidFrom() == null ||
            // !ins.getValidFrom().isAfter(billingDateLocal)
            // //If validFrom is null → insurance has no start restriction (always valid
            // from the beginning)
            // //If validFrom is not after billing date → insurance has already started
            // //End date validation: ins.getValidTo() == null ||
            // !ins.getValidTo().isBefore(billingDateLocal)
            // //If validTo is null → insurance has no end date (still active)
            // //If validTo is not before billing date → insurance hasn't expired yet
            // }
            // }

            LocalDate billingDateLocal = billing.getBillingDate().toLocalDate();
            List<Insurance> activeInsurances =
                insuranceRepository.findByPatientIdAndValidFromLessThanEqualAndValidToGreaterThanEqual(
                    patient.getId(),
                    billingDateLocal,
                    billingDateLocal
                );
            if (!activeInsurances.isEmpty()) {
                insurance = activeInsurances.get(0);
            }
        }
        if (insurance != null) {
            billing.setInsurance(insurance);

            // apply coveragePercent if present
            var coveragePercent = insurance.getCoveragePercent();
            if (coveragePercent != null) {
                BigDecimal percentage = coveragePercent;
                BigDecimal amount = billing.getAmount() != null
                    ? billing.getAmount()
                    : BigDecimal.ZERO;
                BigDecimal covered = amount
                    .multiply(percentage)
                    .divide(BigDecimal.valueOf(100));
                BigDecimal patientPays = amount.subtract(covered);

                // append to description
                String coverLine = String.format(
                    "Insurance %s (%s) covers %s%% -> covered: %s, patient pays: %s",
                    insurance.getPolicyNumber(),
                    insurance.getProviderName(),
                    percentage.toPlainString(),
                    covered.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    patientPays
                        .setScale(2, RoundingMode.HALF_UP)
                        .toPlainString()
                );

                String description = billing.getDescription() != null
                    ? billing.getDescription() + "|" + coverLine
                    : coverLine;
                // update description
                billing.setDescription(description);

                // set a payment method by default to insurance
                if (billingDto.getPaymentMethod() == null) {
                    billing.setPaymentMethod("INSURANCE");
                }
            }
        } else {
            // no insurance found: compute patient payable equals amount and covered is zero
            billing.setCoveredAmount(BigDecimal.ZERO);
            billing.setPatientPayable(billing.getAmount());
        }
        if (billingDto.getBillingDate() == null) {
            billingDto.setBillingDate(LocalDateTime.now());
        }
        billingRepository.save(billing);
        return billingMapper.toDto(billing);
    }

    @Override
    @Transactional(readOnly = true)
    public BillingDto getBillingById(Long id) {
        Billing existing = billingRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Billing not found with id: " + id
                )
            );
        return billingMapper.toDto(existing);
    }

    @Override
    public BillingDto updateBilling(Long id, BillingDto billingDto) {
        Billing exitingBill = billingRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Billing not found with id: " + id
                )
            );

        billingMapper.updateEntityFromDto(billingDto, exitingBill); // update fields from dto to entity

        // handle patient and appointment updates if their ids are provided in dto
        if (billingDto.getPatientId() != null) {
            exitingBill.setPatient(
                patientRepository
                    .findById(billingDto.getPatientId())
                    .orElseThrow(() ->
                        new ResourceNotFoundException(
                            "Patient not found with id: " +
                                billingDto.getPatientId()
                        )
                    )
            );
        }
        if (billingDto.getAppointmentId() != null) {
            exitingBill.setAppointment(
                appointmentRepository
                    .findById(billingDto.getAppointmentId())
                    .orElseThrow(() ->
                        new ResourceNotFoundException(
                            "Appointment not found with id: " +
                                billingDto.getAppointmentId()
                        )
                    )
            );
        }

        Billing updatedBill = billingRepository.save(exitingBill);
        return billingMapper.toDto(updatedBill);
    }

    @Override
    public void deleteBilling(Long id) {
        Billing existing = billingRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Billing " + "not found with id: " + id
                )
            );
        billingRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true) // read-only transaction for fetching data means no locks are held
    public List<BillingDto> getAllBilling() {
        return billingRepository
            .findAll()
            .stream()
            .map(billingMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillingDto> getBillingsByPatient(Long patientId) {
        return billingRepository
            .findByPatientId(patientId)
            .stream()
            .map(billingMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public BillingDto markAsPaid(Long id, String paymentMethod) {
        Billing existing = billingRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Billing not found with id: " + id
                )
            );

        existing.setStatus(BillingStatus.PAID);
        existing.setPaymentMethod(
            paymentMethod != null ? paymentMethod : existing.getPaymentMethod()
        );
        existing.setPaidAt(LocalDateTime.now());
        Billing updated = billingRepository.save(existing);
        return billingMapper.toDto(updated);
    }
}
