package com.pacman.hospital.domain.billing.service.impl.impl;

import com.pacman.hospital.domain.appointment.repository.AppointmentRepository;
import com.pacman.hospital.domain.billing.dto.BillingDto;
import com.pacman.hospital.domain.billing.mapper.BillingMapper;
import com.pacman.hospital.domain.billing.model.Billing;
import com.pacman.hospital.domain.billing.repository.BillingRepository;
import com.pacman.hospital.domain.billing.service.impl.BillingService;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {
    private final BillingMapper billingMapper;
    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public BillingServiceImpl(BillingMapper billingMapper, BillingRepository billingRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.billingMapper = billingMapper;
        this.billingRepository = billingRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public BillingDto createBilling(BillingDto billingDto) {
        //convert dto to entity
        Billing newBilling = billingMapper.toEntity(billingDto);

        // set patient and appointment using their ids from dto
        newBilling.setPatient(patientRepository.findById(billingDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + billingDto.getPatientId())));

        newBilling.setAppointment(appointmentRepository.findById(billingDto.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + billingDto.getAppointmentId()))
        );
        //save entity
        billingRepository.save(newBilling);
        //convert entity to dto
        return billingMapper.toDto(newBilling);

    }

    @Override
    @Transactional(readOnly = true)
    public BillingDto getBillingById(Long id) {
        Billing existing = billingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Billing not found with id: " + id));
        return billingMapper.toDto(existing);
    }

    @Override
    public BillingDto updateBilling(Long id, BillingDto billingDto) {
        Billing exitingBill = billingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Billing not found with id: " + id));

        billingMapper.updateEntityFromDto(billingDto, exitingBill); // update fields from dto to entity

        // handle patient and appointment updates if their ids are provided in dto
        if (billingDto.getPatientId() != null) {
            exitingBill.setPatient(patientRepository.findById(billingDto.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + billingDto.getPatientId())));
        }
        if (billingDto.getAppointmentId() != null) {
            exitingBill.setAppointment(appointmentRepository.findById(billingDto.getAppointmentId()).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + billingDto.getAppointmentId())));
        }

        Billing updatedBill = billingRepository.save(exitingBill);
        return billingMapper.toDto(updatedBill);
    }

    @Override
    public void deleteBilling(Long id) {
        Billing existing = billingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Billing " +
                "not found with id: " + id));
        billingRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true) // read-only transaction for fetching data means no locks are held
    public List<BillingDto> getAllBilling() {
        return billingRepository.findAll()
                .stream()
                .map(billingMapper::toDto)
                .collect(Collectors.toList());
    }
}
