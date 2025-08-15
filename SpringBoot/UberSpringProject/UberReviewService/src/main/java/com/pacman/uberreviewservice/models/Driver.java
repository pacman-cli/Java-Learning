package com.pacman.uberreviewservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    private String phoneNumber;
    //1 driver can have many bookings so this relationship would be 1:n ////this is driver class so one part is driver many part is booking
    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT) //executing nested query not doing one by one query solves N+1 query problem
    private List<Booking> bookings = new ArrayList<>();
}
