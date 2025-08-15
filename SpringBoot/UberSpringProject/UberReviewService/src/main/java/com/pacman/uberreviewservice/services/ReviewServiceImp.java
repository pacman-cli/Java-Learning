package com.pacman.uberreviewservice.services;

import com.pacman.uberreviewservice.models.Review;
import com.pacman.uberreviewservice.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImp implements ReviewService {

    public ReviewRepository reviewRepository;

    public ReviewServiceImp(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Optional<Review> findReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
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
}
