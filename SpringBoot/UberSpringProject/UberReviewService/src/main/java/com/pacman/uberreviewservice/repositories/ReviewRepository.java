package com.pacman.uberreviewservice.repositories;

import com.pacman.uberreviewservice.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Integer countByRatingLessThanEqual(Integer givenRating);

    List<Review> findAllByRatingLessThanEqual(Integer givenRating);

    List<Review> findAllByCreatedAtBefore(Date date);

    @Query("select r from Booking b inner join Review  r where b.id =:bookingId")
    Review findReviewByBookingId(Long bookingId);

    List<Review> id(Long id);
}
