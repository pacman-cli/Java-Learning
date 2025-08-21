package com.pacman.uberreviewservice.adapter;

import com.pacman.uberreviewservice.dtos.DriverDto;
import com.pacman.uberreviewservice.models.Driver;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class DriverAdapter {
  public DriverDto toDriverDto(Driver driver) {
    if (driver == null) {
      return null;
    }
    return DriverDto.builder()
        .id(driver.getId())
        .name(driver.getName())
        .phoneNumber(driver.getPhoneNumber())
        .licenceNumber(driver.getLicenceNumber())
        .createdAt(convertToLocalDate(driver.getCreatedAt()))
        .updatedAt(convertToLocalDate(driver.getUpdatedAt()))
        .build();
  }

  private LocalDateTime convertToLocalDate(Date date) {
    if (date == null) {
      return null;
    }
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public Driver toEntity(DriverDto driverDto) {
    if (driverDto == null) {
      return null;
    }
    return Driver.builder()
        .name(driverDto.getName())
        .phoneNumber(driverDto.getPhoneNumber())
        .licenceNumber(driverDto.getLicenceNumber())
        .build();
  }
}
