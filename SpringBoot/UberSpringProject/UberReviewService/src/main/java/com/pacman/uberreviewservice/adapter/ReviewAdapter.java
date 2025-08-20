package com.pacman.uberreviewservice.adapter;

import com.pacman.uberreviewservice.dtos.ReviewDto;
import com.pacman.uberreviewservice.models.Review;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

//contains methods to convert model objects to dto objects
//toReviewDto, toPassengerReviewDto, toEntity, toPassengerReviewEntity
@Component
public class ReviewAdapter {
    public ReviewDto toReviewDto(Review review) {
        if (review == null) {
            return null;
        }

        ReviewDto reviewDto = ReviewDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(convertToLocalDateTime(review.getCreatedAt()))
                .updatedAt(convertToLocalDateTime(review.getUpdatedAt()))
                .build();
        if (review.getBooking() != null) {
            reviewDto.setBookingId(review.getBooking().getId());
        }
        return reviewDto;
    }

    public Review toEntity(ReviewDto reviewDto) {
        if (reviewDto == null) {
            return null;
        }
        return Review.builder()
                .content(reviewDto.getContent())
                .rating(reviewDto.getRating())
                .build();
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
