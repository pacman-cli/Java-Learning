package com.pacman.uberreviewservice.services;

import java.util.List;
import java.util.Optional;

import com.pacman.uberreviewservice.models.Review;

public interface ReviewService {
  public Optional<Review> findReviewById(Long id);

  public List<Review> findAllReviews();

  public boolean deleteReviewById(Long id);

  Review saveReview(Review review); // saving reviews

  List<Review> findReviewsByRatingLessThanEqual(Double rating);
}
// you probably also want:
//
// Review saveReview(Review review); → so you can add new reviews.
//
// List<Review> findReviewsByRatingLessThanEqual(Double rating); → to fetch filtered reviews.
