package com.pacman.uberreviewservice.controllers;

import com.pacman.uberreviewservice.models.Booking;
import com.pacman.uberreviewservice.models.BookingStatus;
import com.pacman.uberreviewservice.repositories.BookingRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(
    origins = "*") // allows cross-origin requests to be made to the server.origins = "*" → allows
// requests from any domain (not secure for production).
public class BookingControllers {
  private final BookingRepository bookingRepository;

  public BookingControllers(BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
  }

  // Get all bookings
  @GetMapping
  public ResponseEntity<List<Booking>> getAllBookings() {
    List<Booking> bookings = bookingRepository.findAll();
    return ResponseEntity.ok(bookings);
  }

  // Get all bookings by Id
  @GetMapping("/{id}")
  public ResponseEntity<Booking> getBookingsById(@PathVariable Long id) {
    Optional<Booking> bookings = bookingRepository.findById(id);
    return bookings.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  // Get booking by status
  @GetMapping("/status/{status}")
  public ResponseEntity<List<Booking>> getBookingByStatus(@PathVariable String status) {
    try {
      BookingStatus bookingStatus =
          BookingStatus.valueOf(status.toUpperCase()); // converting to uppercase
      List<Booking> bookings = bookingRepository.findByBookingStatus(bookingStatus);
      return ResponseEntity.ok(bookings);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
