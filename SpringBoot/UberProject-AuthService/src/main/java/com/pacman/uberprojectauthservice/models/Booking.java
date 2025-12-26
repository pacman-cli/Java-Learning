package com.pacman.uberprojectauthservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends BaseModel {

    // ->this tells this is an enum and type which is stored in the database as String.
    @Enumerated(value = EnumType.STRING)
    private BookingStatus
            bookingStatus; // ->BookingStatus is enum {pending,canceled,approved} there can be multiple

    // booking status.

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime;

    private Long totalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Passenger passenger;
}
