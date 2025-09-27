package com.pacman.hospital.domain.laboratory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lab_orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabOrder {
    @Id
    private Long id;

    @Enumerated
    private LabOrderStatus labOrderStatus;
}
