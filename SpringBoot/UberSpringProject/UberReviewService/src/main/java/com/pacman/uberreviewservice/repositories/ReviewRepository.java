package com.pacman.uberreviewservice.repositories;

import com.pacman.uberreviewservice.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

}
