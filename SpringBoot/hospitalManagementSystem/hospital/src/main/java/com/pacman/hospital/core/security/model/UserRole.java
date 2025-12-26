package com.pacman.hospital.core.security.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    // Patient roles
    PATIENT("ROLE_PATIENT", "Patient"),

    // Medical staff roles
    DOCTOR("ROLE_DOCTOR", "Doctor"),
    NURSE("ROLE_NURSE", "Nurse"),
    PHARMACIST("ROLE_PHARMACIST", "Pharmacist"),
    LAB_TECHNICIAN("ROLE_LAB_TECHNICIAN", "Lab Technician"),

    // Administrative roles
    ADMIN("ROLE_ADMIN", "Administrator"),
    RECEPTIONIST("ROLE_RECEPTIONIST", "Receptionist"),
    BILLING_STAFF("ROLE_BILLING_STAFF", "Billing Staff"),
    INSURANCE_STAFF("ROLE_INSURANCE_STAFF", "Insurance Staff"),

    // Management roles
    HOSPITAL_MANAGER("ROLE_HOSPITAL_MANAGER", "Hospital Manager"),
    DEPARTMENT_HEAD("ROLE_DEPARTMENT_HEAD", "Department Head"),

    // System roles
    SUPER_ADMIN("ROLE_SUPER_ADMIN", "Super Administrator");

    private final String authority;
    private final String displayName;

    public static UserRole fromAuthority(String authority) {
        for (UserRole role : values()) {
            if (role.getAuthority().equals(authority)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown authority: " + authority);
    }
}
