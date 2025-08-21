package com.pacman.uberreviewservice.controllers;

import com.pacman.uberreviewservice.models.Booking;
import com.pacman.uberreviewservice.models.BookingStatus;
import com.pacman.uberreviewservice.repositories.BookingRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
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

  // 👉 By default, Spring doesn’t know where the Booking data is coming from.
  // It could be:
  // URL parameters (@RequestParam)
  // Path variables (@PathVariable)
  // Form data
  // Or JSON from the request body
  // If you want Spring to read the JSON sent in the request body (like when you send {
  // "customerName": "John", "date": "2025-08-21" } from Postman or frontend), you must tell it
  // explicitly using @RequestBody.

  // Create new Booking
  @PostMapping
  public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
    Booking saveBooking = bookingRepository.save(booking);
    return ResponseEntity.status(HttpStatus.CREATED).body(saveBooking);
  }

  // Update Booking
  @PutMapping("/{id}")
  public ResponseEntity<Booking> updateBooking(
      @PathVariable Long id, @RequestBody Booking booking) {
    Optional<Booking> existingBooking = bookingRepository.findById(id);
    if (existingBooking.isPresent()) {
      booking.setId(id);
      Booking updateBooking = bookingRepository.save(booking);
      return ResponseEntity.ok(updateBooking);
    }
    return ResponseEntity.notFound().build();
  }

  // Update Booking status
  @PutMapping("/{id}/status")
  public ResponseEntity<Booking> updateBookingStatusj(
      @PathVariable Long id, @RequestBody String status) {
    Optional<Booking> existingBooking = bookingRepository.findById(id);
    if (existingBooking.isPresent()) {
      try {
        BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
        Booking booking = existingBooking.get();
        booking.setBookingStatus(bookingStatus);
        Booking updatedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(updatedBooking);
      } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
      }
    }
    return ResponseEntity.notFound().build();
  }

  // Delete booking
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteBooking(@PathVariable Long id) {
    if (bookingRepository.existsById(id)) {
      bookingRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
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
    if (bookings.isPresent()) {
      return ResponseEntity.ok(bookings.get());
    } else {
      return ResponseEntity.notFound().build();
    }
    // return bookings.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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

  // Get bookings by Driver id
  @GetMapping("/driver/{driverId}")
  public ResponseEntity<List<Booking>> getBookingsByDriver(@PathVariable Long driverId) {
    List<Booking> bookings = bookingRepository.findByDriverId(driverId);

    return ResponseEntity.ok(bookings);
  }

  // Get bookings by Driver id
  @GetMapping("/passenger/{passengerId}")
  public ResponseEntity<List<Booking>> getBookingsByPassenger(@PathVariable Long passengerId) {
    List<Booking> bookings = bookingRepository.findByDriverId(passengerId);
    return ResponseEntity.ok(bookings);
  }
}
