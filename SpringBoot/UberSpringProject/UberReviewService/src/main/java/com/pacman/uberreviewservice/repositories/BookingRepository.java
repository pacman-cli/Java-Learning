package com.pacman.uberreviewservice.repositories;

import com.pacman.uberreviewservice.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByDriverId(Long driverId);
}
