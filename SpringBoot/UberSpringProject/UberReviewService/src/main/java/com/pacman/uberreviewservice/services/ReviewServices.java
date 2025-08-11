package com.pacman.uberreviewservice.services;

import com.pacman.uberreviewservice.models.Review;
import com.pacman.uberreviewservice.repositories.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewServices implements CommandLineRunner {
    private final ReviewRepository reviewRepository;

    public ReviewServices(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("***************");
        Review review = Review.builder()  //code to create plain java object
                .content("This isn't a Book")
                .rating(3.0)
                .build();
        //System.out.println(review);
        reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findAll();
        for (Review r : reviews) {
            System.out.println("Title:" + r.getContent() + " Rating:" + r.getRating());
        }
        reviewRepository.deleteById(2L);
    }
}
