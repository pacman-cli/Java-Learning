package com.pacman.uberreviewservice.services;

import com.pacman.uberreviewservice.models.Booking;
import com.pacman.uberreviewservice.repositories.BookingRepository;
import com.pacman.uberreviewservice.repositories.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewServices implements CommandLineRunner {
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    public ReviewServices(ReviewRepository reviewRepository, BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("***************");
//        Review review = Review.builder()  //code to create plain java object
//                .content("Amazing ride quality 5.")
//                .rating(3.5)
//                .build();
//        //System.out.println(review);
//        Booking booking = Booking.builder()
//                .review(review)
//                .endTime(new Date())
//                .build();

        //reviewRepository.save(review); //->booking is depend on the review so that's why first saving the review, and then we are saving booking.But here we are doing it manually but using CASCADE we can give this work to spring and spring will manage for us, we don't need to do this manually.

        //bookingRepository.save(booking);


//        List<Review> reviews = reviewRepository.findAll();
//        for (Review r : reviews) {
//            System.out.println("Title:" + r.getContent() + " Rating:" + r.getRating());
//        }
//        reviewRepository.deleteById(2L);
        Optional<Booking> booking = bookingRepository.findById(3L);
        if (booking.isPresent()) {
            bookingRepository.delete(booking.get());
        }
        
    }
}
