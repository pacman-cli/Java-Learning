package com.pacman.hospital.domain.doctor.repository;

import com.pacman.hospital.domain.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // custom queries can be added later
}
