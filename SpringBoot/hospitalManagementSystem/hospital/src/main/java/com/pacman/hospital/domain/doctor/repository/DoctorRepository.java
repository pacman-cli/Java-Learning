package com.pacman.hospital.domain.doctor.repository;

import com.pacman.hospital.domain.doctor.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findByFullNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(
            String fullName,
            String specialization,
            Pageable pageable);
}

// 🧠 Breakdown:
// Part-->Meaning
// findBy-->Tells Spring Data to generate a SELECT query
// FullNameContainingIgnoreCase-->Finds users where the fullName field contains
// a given substring (case-insensitive)
// Or-->Combines two conditions with a logical OR
// ContactInfoContainingIgnoreCase-->Same for the contactInfo field —
// case-insensitive partial match
// auto generated query:
// SELECT *
// FROM users
// WHERE LOWER(full_name) LIKE LOWER('%keyword%')
// OR LOWER(contact_info) LIKE LOWER('%keyword%')
// LIMIT 10 OFFSET 0; -- (if pageable = PageRequest.of(0, 10))
