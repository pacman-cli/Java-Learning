package com.pacman.uberprojectauthservice.dto;

import com.pacman.uberprojectauthservice.models.Passenger;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerSignupRequestDto {
  private String email;
  private String password;
  private String phoneNumber;
  private String name;

  public static PassengerDto from(Passenger passenger) {
    return PassengerDto.builder()
        .id(passenger.getId())
        .email(passenger.getEmail())
        .name(passenger.getName())
        .phoneNumber(passenger.getPhoneNumber())
        .build();
  }
}
