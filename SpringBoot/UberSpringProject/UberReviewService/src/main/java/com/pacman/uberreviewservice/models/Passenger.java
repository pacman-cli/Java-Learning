package com.pacman.uberreviewservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passenger extends BaseModel {
    private String name;

    //here 1 passenger has a lot of bookings and booking boilings to passenger
    @OneToMany(mappedBy = "passenger")
    List<Booking> bookings = new ArrayList<>();
}
