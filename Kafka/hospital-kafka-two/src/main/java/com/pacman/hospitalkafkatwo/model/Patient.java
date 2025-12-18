package com.pacman.hospitalkafkatwo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String name;
    private String medicine;
    private String doctor;
    private String notes;
}
