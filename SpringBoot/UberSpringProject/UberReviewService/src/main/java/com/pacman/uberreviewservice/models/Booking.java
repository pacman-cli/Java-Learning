package com.pacman.uberreviewservice.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @ManyToOne private Driver driver;

  @ManyToOne private Passenger passenger;
}
// 1 to 1 --> default is Eager-->(if immediately need your data point is not eager make it eager)
// 1 to many --> default is Lazy-->(if immediately not need ,your data point is not lazy make it
// lazy)
// many to many --> default is Lazy
// many to 1 --> default is Eager
