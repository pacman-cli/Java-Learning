package com.pacman.uberreviewservice.controllers;

import com.pacman.uberreviewservice.adapter.ReviewAdapter;
import com.pacman.uberreviewservice.dtos.ReviewDto;
import com.pacman.uberreviewservice.repositories.ReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

// Allows cross-origin requests to be made to the server.origins = "*" → allows requests from any
// domain (not secure for production).
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewControllers {

    private final ReviewRepository reviewRepository;
    private final ReviewAdapter reviewAdapter;

    public ReviewControllers(ReviewRepository reviewRepository, ReviewAdapter reviewAdapter) {
        this.reviewRepository = reviewRepository;
        this.reviewAdapter = reviewAdapter;
    }

    // service layer has all the data access from the database
    // Get all reviews
//    The code you shared is a Spring MVC controller method that retrieves all reviews from the reviewRepository and returns them as a list of ReviewDto objects.
//   TODO: Benefits of using DTOs:
//
//Control what’s exposed → Only expose necessary fields (e.g. reviewText, rating), not database internals.
//
//API stability → If DB schema changes, you only update mapping logic, not the API contract.
//
//Performance → You can flatten nested objects or combine fields, so the client gets exactly what it needs.
//
//Validation / Transformation → Sometimes you want to format or enrich data before sending.
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewRepository.findAll().stream().map(reviewAdapter::toReviewDto).collect(Collectors.toList()));
//        List<Review> reviews = reviewRepository.findAll();
//        List<ReviewDto> reviewDtos = reviews.stream()
//                .map(reviewAdapter::toReviewDto)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(reviewDtos);
    }
}
