package com.pacman.uberreviewservice.repositories;

import com.pacman.uberreviewservice.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
//    List<Booking> findAllByDriverId(Long driverId);

//    List<Booking> findAllByDriverIn(List<Driver> drivers);


}
