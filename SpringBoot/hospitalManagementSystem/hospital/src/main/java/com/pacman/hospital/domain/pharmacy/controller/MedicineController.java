package com.pacman.hospital.domain.pharmacy.controller;

import com.pacman.hospital.domain.pharmacy.dto.MedicineDto;
import com.pacman.hospital.domain.pharmacy.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {
    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    public ResponseEntity<MedicineDto> create(@RequestBody MedicineDto medicineDto) {
        return ResponseEntity.ok(medicineService.createMedicine(medicineDto));
    }

    @GetMapping
    public ResponseEntity<List<MedicineDto>> getAll() {
        return ResponseEntity.ok((medicineService.getAllMedicines()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineDto> update(@RequestParam Long id, @RequestBody MedicineDto medicineDto) {
        return ResponseEntity.ok(medicineService.updateMedicine(id, medicineDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }
}
