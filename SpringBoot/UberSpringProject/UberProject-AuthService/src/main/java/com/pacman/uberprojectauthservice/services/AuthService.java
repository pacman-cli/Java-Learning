package com.pacman.uberprojectauthservice.services;

import com.pacman.uberprojectauthservice.dto.PassengerDto;
import com.pacman.uberprojectauthservice.dto.PassengerSignupRequestDto;
import com.pacman.uberprojectauthservice.models.Passenger;
import com.pacman.uberprojectauthservice.repositories.PassengerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PassengerRepository passengerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(PassengerRepository passengerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.passengerRepository = passengerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public PassengerDto toSignedInPassengerDto(PassengerSignupRequestDto passengerSignupRequestDto) {
        if (passengerRepository.existsByEmail(passengerSignupRequestDto.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        Passenger passenger = Passenger.builder()
                .email(passengerSignupRequestDto.getEmail())
                .password(bCryptPasswordEncoder.encode(passengerSignupRequestDto.getPassword()))
                .name(passengerSignupRequestDto.getName())
                .phoneNumber(passengerSignupRequestDto.getPhoneNumber())
                .build();

        return PassengerDto.from(passengerRepository.save(passenger));
    }
}
