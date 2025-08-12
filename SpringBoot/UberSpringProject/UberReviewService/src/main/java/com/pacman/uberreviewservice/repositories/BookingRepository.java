package com.pacman.uberreviewservice.repositories;

import com.pacman.uberreviewservice.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
