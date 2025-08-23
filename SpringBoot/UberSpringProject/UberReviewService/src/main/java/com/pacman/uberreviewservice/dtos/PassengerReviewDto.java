package com.pacman.uberreviewservice.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PassengerReviewDto extends ReviewDto {
  private String passengerReviewContent;
  private Double passengerRating;
}
