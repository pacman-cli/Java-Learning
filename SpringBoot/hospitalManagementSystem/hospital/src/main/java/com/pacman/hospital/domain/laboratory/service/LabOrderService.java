package com.pacman.hospital.domain.laboratory.service;

import com.pacman.hospital.domain.laboratory.dto.LabOrderDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LabOrderService {
    LabOrderDto createOrder(LabOrderDto dto);

    LabOrderDto updateOrder(Long id, LabOrderDto dto);

    LabOrderDto getOrderById(Long id);

    void deleteOrder(Long id);

    List<LabOrderDto> getOrdersForPatient(Long patientId);

    LabOrderDto attachReport(Long id, MultipartFile file) throws IOException;

    LabOrderDto changeStatus(Long id, String status);
}
