package com.pacman.uberreviewservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pacman.uberreviewservice.models.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

  // Find passengers by name (partial match, case-insensitive)
  List<Passenger> findByNameContainingIgnoreCase(String name);
}
