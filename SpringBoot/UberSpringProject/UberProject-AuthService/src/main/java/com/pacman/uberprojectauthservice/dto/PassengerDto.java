package com.pacman.uberprojectauthservice.dto;

import com.pacman.uberprojectauthservice.models.Passenger;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password; //encrypted password
    private Date createdAt;

    public static PassengerDto  from(Passenger passenger){
         PassengerDto fromPassengerDto=PassengerDto.builder()
                .id(passenger.getId())
                .name(passenger.getName())
                .email(passenger.getEmail())
                .phoneNumber(passenger.getPhoneNumber())
                .password(passenger.getPassword())
                .createdAt(passenger.getCreatedAt())
                .build();
         return fromPassengerDto;
    }
}
