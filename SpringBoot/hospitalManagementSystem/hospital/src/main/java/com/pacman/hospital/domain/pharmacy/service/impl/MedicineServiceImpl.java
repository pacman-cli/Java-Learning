package com.pacman.hospital.domain.pharmacy.service.impl;

import com.pacman.hospital.domain.pharmacy.dto.MedicineDto;
import com.pacman.hospital.domain.pharmacy.mapper.MedicineMapper;
import com.pacman.hospital.domain.pharmacy.model.Medicine;
import com.pacman.hospital.domain.pharmacy.repository.MedicineRepository;
import com.pacman.hospital.domain.pharmacy.service.MedicineService;
import com.pacman.hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;

    @Override
    public MedicineDto createMedicine(MedicineDto medicineDto) {
        Medicine create = MedicineMapper.toEntity(medicineDto);
        medicineRepository.save(create);
        return MedicineMapper.toDto(create);
    }

    @Override
    public MedicineDto updateMedicine(Long id, MedicineDto medicineDto) {
        Medicine existing = medicineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Medicine" +
                " not found with " +
                "id: " + id));
        existing.setName(medicineDto.getName());
        existing.setDescription(medicineDto.getDescription());
        existing.setQuantityAvailable(medicineDto.getQuantityAvailable());
        existing.setPrice(medicineDto.getPrice());
        existing.setExpiryDate(medicineDto.getExpiryDate());
        return MedicineMapper.toDto(medicineRepository.save(existing));
    }

    @Override
    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }

    @Override
    public MedicineDto getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .map(MedicineMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id: " + id));
    }

    @Override
    public List<MedicineDto> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(MedicineMapper::toDto)
                .collect(Collectors.toList());
    }
}