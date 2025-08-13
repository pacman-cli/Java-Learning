package com.pacman.uberreviewservice.services;

import com.pacman.uberreviewservice.models.Booking;
import com.pacman.uberreviewservice.models.Driver;
import com.pacman.uberreviewservice.repositories.BookingRepository;
import com.pacman.uberreviewservice.repositories.DriverRepository;
import com.pacman.uberreviewservice.repositories.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReviewServices implements CommandLineRunner {
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final DriverRepository driverRepository;

    public ReviewServices(ReviewRepository reviewRepository, BookingRepository bookingRepository, DriverRepository driverRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    @Transactional
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
//        Optional<Booking> booking = bookingRepository.findById(3L);
//        if (booking.isPresent()) {
//            bookingRepository.delete(booking.get());
//        }
//        Optional<Driver> drivers = driverRepository.findByIdAndLicenceNumber(2L, "HU34848JJ");
//        Optional<Driver> drivers = driverRepository.findById(1L);
//
//        if (drivers.isPresent()) {
//            System.out.println(drivers.get().getName());
//            List<Booking> b = drivers.get().getBookings();
////            List<Booking> bookingList = bookingRepository.findAllByDriverId(1L);
//            for (Booking bookings : b) {
//                System.out.println(bookings.getId());
//            }
//        }
//        Optional<Booking> bookings = bookingRepository.findById(1L);


//        Optional<Driver> d = driverRepository.rawFindByIdAndLicenceNumber(1L, "HY3848JJ");
//        System.out.println(d.get().getName());

//        Optional<Driver> driver = driverRepository.hibernatedByIdAndLicenceNumber(1L, "HY3848JJ");
//        if (driver.isPresent()) {
//            System.out.println(driver.get().getName() + " " + driver.get().getId() + " " + driver.get().getCratedAt());
//        }

        List<Long> driverIds = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L));
        List<Driver> drivers = driverRepository.findAllByIdIn(driverIds);

//        List<Booking> bookings = bookingRepository.findAllByDriverIn(drivers); //N+1 problems solution 1 given by spring
        //N+1 times
        for (Driver driver : drivers) {
            List<Booking> bookings = driver.getBookings();
            bookings.forEach(booking -> System.out.println(booking.getId()));
        }
    }
}
