package com.pacman.uberprojectauthservice.services;

import com.pacman.uberprojectauthservice.dto.PassengerDto;
import com.pacman.uberprojectauthservice.dto.PassengerSignupRequestDto;
import com.pacman.uberprojectauthservice.models.Passenger;
import com.pacman.uberprojectauthservice.repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PassengerRepository passengerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public AuthService(PassengerRepository passengerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.passengerRepository = passengerRepository;

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public PassengerDto toSignedInPassengerDto(PassengerSignupRequestDto passengerSignupRequestDto){
        Passenger passenger= Passenger.builder()
                .email(passengerSignupRequestDto.getEmail())
                .password(bCryptPasswordEncoder.encode(passengerSignupRequestDto.getPassword())) //TODO: encrypted password
                .name(passengerSignupRequestDto.getName())
                .phoneNumber(passengerSignupRequestDto.getPhoneNumber())
                .build();
        Passenger newPassenger=passengerRepository.save(passenger);
//        PassengerDto passengerDto = PassengerDto.from(newPassenger);
//        return passengerDto;
        return PassengerDto.from(newPassenger);
    }
}
