package com.pacman.uberreviewservice.dtos;

import com.pacman.uberreviewservice.models.BookingStatus;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
  private Long id;
  private BookingStatus bookingStatus;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Long totalTime;
  private Long driverId;
  private Long passengerId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
