package com.pacman.uberreviewservice.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pacman.uberreviewservice.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  Integer countByRatingLessThanEqual(Double rating);

  List<Review> findAllByRatingLessThanEqual(Double rating);

  List<Review> findAllByCreatedAtBefore(Date date);

  @Query("SELECT r FROM Review r WHERE r.booking.id = :bookingId")
  Review findReviewByBookingId(Long bookingId);

  //    List<Review> id(Long id);
  List<Review> findAllById(
      Long id); // not useful though, because id is unique  Repository -> Dtabase
}
