package com.pacman.uberprojectauthservice.dto;

import com.pacman.uberprojectauthservice.models.Passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
