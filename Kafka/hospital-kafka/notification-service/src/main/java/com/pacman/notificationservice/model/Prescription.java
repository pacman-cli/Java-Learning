package com.pacman.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    private String patient;
    private String medicine;
    private String doctor;
    private String notes;
}
