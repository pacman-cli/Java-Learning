package com.pacman.uberreviewservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Driver extends BaseModel {
    private String name;

    @Column(nullable = false, unique = true)
    private String licenceNumber;

    //1 driver can have many bookings so this relationship would be 1:n
    @OneToMany(mappedBy = "driver", fetch = FetchType.EAGER)
    //this is driver class so one part is driver many part is booking
    private List<Booking> bookings = new ArrayList<>();
}
