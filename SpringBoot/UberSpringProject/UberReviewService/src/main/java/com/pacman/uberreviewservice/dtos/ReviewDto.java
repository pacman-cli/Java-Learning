package com.pacman.uberreviewservice.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String content;
    private Double rating;
    private Long bookingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
