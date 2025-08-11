package com.pacman.uberreviewservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//@PrimaryKeyJoinColumn(name = "passanger_review_id")
public class PassangerReview extends Review {
    @Column(nullable = false)
    private String passangerReviewContent;

    @Column(nullable = false)
    private String passangerRating;
}
