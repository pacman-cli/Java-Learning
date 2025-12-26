package com.pacman.hospital.domain.laboratory.service.impl;

import com.pacman.hospital.common.storage.StorageService;
import com.pacman.hospital.domain.appointment.repository.AppointmentRepository;
import com.pacman.hospital.domain.laboratory.dto.LabOrderDto;
import com.pacman.hospital.domain.laboratory.mapper.LabOrderMapper;
import com.pacman.hospital.domain.laboratory.model.LabOrder;
import com.pacman.hospital.domain.laboratory.model.LabOrderStatus;
import com.pacman.hospital.domain.laboratory.model.LabTest;
import com.pacman.hospital.domain.laboratory.repository.LabOrderRepository;
import com.pacman.hospital.domain.laboratory.repository.LabTestRepository;
import com.pacman.hospital.domain.laboratory.service.LabOrderService;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.exception.ResourceNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class LabOrderServiceImpl implements LabOrderService {

    private final LabTestRepository labTestRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final LabOrderRepository labOrderRepository;
    private final LabOrderMapper labOrderMapper;
    private final StorageService storageService;

    public LabOrderServiceImpl(
        LabTestRepository labTestRepository,
        PatientRepository patientRepository,
        AppointmentRepository appointmentRepository,
        LabOrderRepository labOrderRepository,
        LabOrderMapper labOrderMapper,
        StorageService storageService
    ) {
        this.labTestRepository = labTestRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.labOrderRepository = labOrderRepository;
        this.labOrderMapper = labOrderMapper;
        this.storageService = storageService;
    }

    @Override
    public LabOrderDto createOrder(LabOrderDto dto) {
        LabTest test = labTestRepository
            .findById(dto.getLabTestId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "LabTest not found: " + dto.getLabTestId()
                )
            );
        var patient = patientRepository
            .findById(dto.getPatientId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Patient not found: " + dto.getPatientId()
                )
            );

        LabOrder e = new LabOrder();
        e.setLabTest(test);
        e.setPatient(patient);
        if (dto.getAppointmentId() != null) {
            var ap = appointmentRepository
                .findById(dto.getAppointmentId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Appointment not found: " + dto.getAppointmentId()
                    )
                );
            e.setAppointment(ap);
        }
        e.setOrderedAt(
            dto.getOrderedAt() != null
                ? dto.getOrderedAt()
                : LocalDateTime.now()
        );
        e.setStatus(LabOrderStatus.ORDERED);
        e.setNotes(dto.getNotes());
        LabOrder saved = labOrderRepository.save(e);
        return labOrderMapper.toDto(saved);
    }

    @Override
    public LabOrderDto updateOrder(Long id, LabOrderDto dto) {
        LabOrder existing = labOrderRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("LabOrder not found: " + id)
            );
        if (dto.getNotes() != null) existing.setNotes(dto.getNotes());
        if (dto.getLabTestId() != null) {
            LabTest test = labTestRepository
                .findById(dto.getLabTestId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "LabTest " + "not found: " + dto.getLabTestId()
                    )
                );
            existing.setLabTest(test);
        }
        if (dto.getStatus() != null) {
            try {
                existing.setStatus(LabOrderStatus.valueOf(dto.getStatus()));
            } catch (IllegalArgumentException ignored) {}
        }
        LabOrder saved = labOrderRepository.save(existing);
        return labOrderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LabOrderDto getOrderById(Long id) {
        return labOrderMapper.toDto(
            labOrderRepository
                .findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "LabOrder not " + "found: " + id
                    )
                )
        );
    }

    @Override
    public void deleteOrder(Long id) {
        labOrderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderDto> getAll() {
        return labOrderRepository
            .findAll()
            .stream()
            .map(labOrderMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderDto> getOrdersForPatient(Long patientId) {
        return labOrderRepository
            .findByPatientId(patientId)
            .stream()
            .map(labOrderMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public LabOrderDto attachReport(Long id, MultipartFile file)
        throws IOException {
        LabOrder order = labOrderRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("LabOrder not found: " + id)
            );
        String path = storageService.store(file, "lab-reports");
        order.setReportPath(path);
        order.setStatus(LabOrderStatus.COMPLETED);
        LabOrder saved = labOrderRepository.save(order);
        return labOrderMapper.toDto(saved);
    }

    @Override
    public LabOrderDto changeStatus(Long id, String status) {
        LabOrder order = labOrderRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("LabOrder not found: " + id)
            );
        try {
            LabOrderStatus s = LabOrderStatus.valueOf(status);
            order.setStatus(s);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        LabOrder saved = labOrderRepository.save(order);
        return labOrderMapper.toDto(saved);
    }
}
