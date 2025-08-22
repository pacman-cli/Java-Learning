package com.pacman.uberreviewservice.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pacman.uberreviewservice.models.Passenger;
import com.pacman.uberreviewservice.repositories.PassengerRepository;

@RestController
@RequestMapping("/api/passenger")
@CrossOrigin(origins = "*")
public class PassengerController {
  private final PassengerRepository passengerRepository;

  public PassengerController(PassengerRepository passengerRepository) {
    this.passengerRepository = passengerRepository;
  }

  // Get all passengers
  @GetMapping
  public ResponseEntity<List<Passenger>> getAllPassengers() {
    List<Passenger> passengers = passengerRepository.findAll();
    return ResponseEntity.ok(passengers);
  }

  // Get passenger by Id
  @GetMapping("/{id}")
  public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
    Optional<Passenger> passenger = passengerRepository.findById(id);
    if (passenger.isPresent()) {
      return ResponseEntity.ok(passenger.get());
    }
    return ResponseEntity.notFound().build();
    // return passenger.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    //        return passengerRepository.findById(id)
    //                .map(ResponseEntity::ok)
    //                .orElse(ResponseEntity.notFound().build());
  }

  // Create a new passsenger
  @PostMapping
  public ResponseEntity<Passenger> createPassenger(@RequestBody Passenger passenger) {
    Passenger savePassenger = passengerRepository.save(passenger);
    return ResponseEntity.status(HttpStatus.CREATED).body(savePassenger);
  }

  // Update passsenger
  @PutMapping("/{id}")
  public ResponseEntity<Passenger> updatePassenger(
      @PathVariable Long id, @RequestBody Passenger passenger) {
    Optional<Passenger> existingPassenger = passengerRepository.findById(id);
    if (existingPassenger.isPresent()) {
      passenger.setId(id);
      Passenger updatedPassenger = passengerRepository.save(passenger);
      return ResponseEntity.ok(updatedPassenger);
    }
    return ResponseEntity.notFound().build();
  }

  // Delete passsenger
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
    if (passengerRepository.existsById(id)) {
      passengerRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  // Search passenger by name
  @GetMapping("/search")
  public ResponseEntity<List<Passenger>> searchpassengerByName(@RequestParam String name) {
    List<Passenger> passengers = passengerRepository.findByNameContainingIgnoreCase(name);
    return ResponseEntity.ok(passengers);
  }
}
