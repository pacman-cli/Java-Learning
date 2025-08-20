package com.pacman.uberreviewservice.dtos;

import com.pacman.uberreviewservice.models.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
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
