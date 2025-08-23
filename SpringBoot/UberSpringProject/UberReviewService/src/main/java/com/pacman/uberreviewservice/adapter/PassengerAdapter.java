package com.pacman.uberreviewservice.adapter;

import com.pacman.uberreviewservice.dtos.PassengerDto;
import com.pacman.uberreviewservice.models.Passenger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class PassengerAdapter {
  // Entity to DTO
  public PassengerDto toPassengerDto(Passenger passenger) {
    if (passenger == null) {
      return null;
    }
    return PassengerDto.builder()
        .id(passenger.getId())
        .name(passenger.getName())
        .createdAt(convertToLocalTime(passenger.getCreatedAt()))
        .updatedAt(convertToLocalTime(passenger.getUpdatedAt()))
        .build();
  }

  // DTO to Entity
  public Passenger toEntity(PassengerDto passengerDto) {
    if (passengerDto == null) {
      return null;
    }
    return Passenger.builder().name(passengerDto.getName()).build();
  }

  private LocalDateTime convertToLocalTime(Date date) {
    if (date == null) {
      return null;
    }
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
