package com.pacman.uberreviewservice.controllers;

import com.pacman.uberreviewservice.adapter.DriverAdapter;
import com.pacman.uberreviewservice.dtos.DriverDto;
import com.pacman.uberreviewservice.models.Driver;
import com.pacman.uberreviewservice.repositories.DriverRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(origins = "*")
public class DriverController {
    private DriverRepository driverRepository;
    private DriverAdapter driverAdapter;

    public DriverController(DriverRepository driverRepository, DriverAdapter driverAdapter) {
        this.driverRepository = driverRepository;
        this.driverAdapter = driverAdapter;
    }

    // Get all drivers
    @GetMapping
    public ResponseEntity<List<DriverDto>> getAllDrivers() {
        List<com.pacman.uberreviewservice.models.Driver> drivers = driverRepository.findAll();
        List<DriverDto> driverDtos =
                drivers.stream().map(driverAdapter::toDriverDto).collect(Collectors.toList());
        return ResponseEntity.ok(driverDtos);
    }

    // Get all drivers by id
    @GetMapping("/{id}")
    public ResponseEntity<List<DriverDto>> getAllDrivers(@PathVariable Long id) {
        Optional<Driver> drivers = driverRepository.findById(id);
        List<DriverDto> driverDtos =
                drivers.stream().map(driverAdapter::toDriverDto).collect(Collectors.toList());
        return ResponseEntity.ok(driverDtos);
    }

    //Get driver by license number
    @GetMapping("/licence/{licenceNumber}")
    public ResponseEntity<DriverDto> getDriverByLicenceNumber(@PathVariable String licenceNumber) {
        Optional<Driver> driver = driverRepository.findByLicenceNumber(licenceNumber);
        if (driver.isPresent()) {
            return ResponseEntity.ok(driverAdapter.toDriverDto(driver.get()));
        }
        return ResponseEntity.notFound().build();
//  return driver.map(value -> ResponseEntity
//                        .ok(driverAdapter.toDriverDto(value)))
//                .orElseGet(() -> ResponseEntity
//                        .notFound().build());
    }

    //Create a new driver
    @PostMapping
    public ResponseEntity<DriverDto> createDriver(@RequestBody DriverDto driverDto) {
        //if licence number already exists
        if (driverRepository.findByLicenceNumber(driverDto.getLicenceNumber()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();//return conflict status
        }

        Driver driver = driverAdapter.toEntity(driverDto);//converting new driver to entity through adapter
        Driver savedDriver = driverRepository.save(driver);
        DriverDto savedDriverDto = driverAdapter.toDriverDto(savedDriver);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDriverDto);

    }
}
