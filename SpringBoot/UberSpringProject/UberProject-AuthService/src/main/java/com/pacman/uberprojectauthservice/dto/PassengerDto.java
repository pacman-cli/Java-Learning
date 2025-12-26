package com.pacman.uberprojectauthservice.dto;

import com.pacman.uberprojectauthservice.models.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {
  private Long id;
  private String name;
  private String email;
  private String phoneNumber;
  private Date createdAt;

  public static PassengerDto from(Passenger passenger) {
    return PassengerDto.builder()
        .id(passenger.getId())
        .name(passenger.getName())
        .email(passenger.getEmail())
        .phoneNumber(passenger.getPhoneNumber())
        .createdAt(passenger.getCreatedAt())
        .build();
  }
}
