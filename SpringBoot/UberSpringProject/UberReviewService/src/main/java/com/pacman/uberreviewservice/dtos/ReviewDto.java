package com.pacman.uberreviewservice.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
