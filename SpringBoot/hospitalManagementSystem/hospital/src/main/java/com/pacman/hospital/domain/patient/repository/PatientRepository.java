package com.pacman.hospital.domain.patient.repository;

import com.pacman.hospital.domain.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByContactInfo(String contactInfo);

    Page<
        Patient
    > findByFullNameContainingIgnoreCaseOrContactInfoContainingIgnoreCase(
        String fullName,
        String contactInfo,
        Pageable pageable
    ); // Pageable is part of Spring Data's pagination and sorting support.
    // Pageable defines the page number, size, and sorting.
    // The method returns a Page<User> — not a full List<User> — so you also get
    // metadata like total pages, total elements, etc.

    // Find patient by userId (link to User account)
    java.util.Optional<Patient> findByUserId(Long userId);
}
