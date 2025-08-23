package com.pacman.uberreviewservice.repositories;

import com.pacman.uberreviewservice.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    //On the top of default functions, we can make custom methods like findByIdAndLicenceNumber which will work like defaults one.
    Optional<Driver> findByIdAndLicenceNumber(Long id, String licenceNumber);//we have defined a signature spring is smart enough to execute queries understanding the methods.

    //we can execute raw queries by writing raw query in @Quarry --> this is one way
//    @Query(nativeQuery = true, value = "SELECT * FROM driver WHERE id=:id AND licence_number=:licenceNumber")
    //-> in raw my sql queries you have to give 'licence_number'->column name as it is.
//    Optional<Driver> rawFindByIdAndLicenceNumber(Long id, String licenceNumber);

    //Hibernate way
//    @Query("SELECT d FROM Driver d WHERE d.id=:id AND d.licenceNumber=:licence")
//    Optional<Driver> hibernatedByIdAndLicenceNumber(Long id, String licence);

    List<Driver> findAllByIdIn(List<Long> driverIds);
    
    // Additional methods needed by DriverController
    Optional<Driver> findByLicenceNumber(String licenceNumber);
    
    List<Driver> findByNameContainingIgnoreCase(String name);
    
    long countByPhoneNumberIsNotNull();//-> this is a custom method that will count the number of drivers with phone number not null.
}
//3 ways.