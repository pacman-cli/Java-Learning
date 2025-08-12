package com.pacman.uberreviewservice.models;

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

    //->CascadeType.ALL propagates all operations — including Hibernate-specific ones — from a parent to a child entity.
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Review review;//we have defined a 1:1 relationship between booking and review

    @Enumerated(value = EnumType.STRING)
//->this tells this is an enum and type which is stored in the database as String.
    private BookingStatus bookingStatus; //->BookingStatus is enum {pending,canceled,approved} there can be multiple booking status.

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime;

    private Long totalTime;
}
