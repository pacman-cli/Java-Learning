package com.pacman.uberprojectauthservice.services;

import com.pacman.uberprojectauthservice.helpers.AuthPassengerDetatails;
import com.pacman.uberprojectauthservice.models.Passenger;
import com.pacman.uberprojectauthservice.repositories.PassengerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class is responsible for loading the user in the form of a UserDetails object for authentication.
 */
//Whenever spring security needs to fetch a user by the unique identifier, it will call this class
@Service
public class UserDetailsImp implements UserDetailsService {
    private final PassengerRepository passengerRepository;

    public UserDetailsImp(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Passenger> passenger = passengerRepository.findPassengerByEmail(email);//why passing email here? because email is a unique identifier
        if (passenger.isPresent()) {
            return new AuthPassengerDetatails(passenger.get());
        } else {
            throw new UsernameNotFoundException("User not found with email : " + email);
        }
    }
}
