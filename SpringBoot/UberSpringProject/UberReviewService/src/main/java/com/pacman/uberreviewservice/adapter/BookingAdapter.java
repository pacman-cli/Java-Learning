package com.pacman.uberreviewservice.adapter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.pacman.uberreviewservice.dtos.BookingDto;
import com.pacman.uberreviewservice.models.Booking;

// toBookingDto,toEntity
@Component
public class BookingAdapter {
  // Entity to DTO
  public BookingDto toBookingDto(Booking booking) {
    if (booking == null) {
      return null;
    }
    return BookingDto.builder()
        .id(booking.getId())
        .bookingStatus(booking.getBookingStatus())
        .startTime(convertToLocalDateTime(booking.getStartTime()))
        .endTime(convertToLocalDateTime(booking.getEndTime()))
        .totalTime(booking.getTotalTime())
        .driverId(booking.getDriver() != null ? booking.getDriver().getId() : null)
        .passengerId(booking.getPassenger() != null ? booking.getPassenger().getId() : null)
        .createdAt(convertToLocalDateTime(booking.getCreatedAt()))
        .updatedAt(convertToLocalDateTime(booking.getUpdatedAt()))
        .build();
  }

  // DTO to Entity
  public Booking toEntity(BookingDto bookingDto) {
    if (bookingDto == null) {
      return null;
    }
    return Booking.builder()
        .bookingStatus(bookingDto.getBookingStatus())
        .startTime(convertToLocalDate(bookingDto.getStartTime()))
        .endTime(convertToLocalDate(bookingDto.getEndTime()))
        .totalTime(bookingDto.getTotalTime())
        .build();
  }

  private Date convertToLocalDate(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  private LocalDateTime convertToLocalDateTime(Date date) {
    if (date == null) {
      return null;
    }
    return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
  }
}
