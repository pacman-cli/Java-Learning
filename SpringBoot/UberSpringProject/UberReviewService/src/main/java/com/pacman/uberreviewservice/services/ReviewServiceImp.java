package com.pacman.uberreviewservice.services;

import com.pacman.uberreviewservice.models.Review;
import com.pacman.uberreviewservice.repositories.ReviewRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImp implements ReviewService {

  public final ReviewRepository
      reviewRepository; // Better to make it private final for immutability.Since we're using
                        // constructor injection, no need for @Autowired (Spring Boot handles it
                        // automatically)

  public ReviewServiceImp(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  @Override
  public Optional<Review> findReviewById(Long id) {
    return reviewRepository.findById(id);
  }

  @Override
  public List<Review> findAllReviews() {
    return reviewRepository.findAll(); // service -> repository
  }

  @Override
  public boolean deleteReviewById(Long id) {
    try {
      reviewRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public Review saveReview(Review review) {
    return reviewRepository.save(review);
  }

  @Override
  public List<Review> findReviewsByRatingLessThanEqual(Double rating) {
    return reviewRepository.findAllByRatingLessThanEqual(rating);
  }
}
