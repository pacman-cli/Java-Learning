package com.pacman.uberreviewservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacman.uberreviewservice.models.Booking;
import com.pacman.uberreviewservice.models.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  List<Booking> findAllByDriverId(Long driverId);

  // Additional methods needed by BookingController
  List<Booking> findByDriverId(Long driverId);

  List<Booking> findByPassengerId(Long passengerId);

  List<Booking> findByBookingStatus(BookingStatus bookingStatus);

  long countByBookingStatus(
      BookingStatus
          bookingStatus); // -> this is a custom method that will count the number of bookings with
  // a specific status.

  //    List<Booking> findAllByDriverIn(List<Driver> drivers);
}
