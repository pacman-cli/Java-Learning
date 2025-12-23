package com.pacman.safetymap.modules.place.controller;

import com.pacman.safetymap.modules.place.dto.PlaceDto;
import com.pacman.safetymap.modules.place.entity.Place;
import com.pacman.safetymap.modules.place.repository.PlaceRepository;
import com.pacman.safetymap.modules.place.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;
    private final PlaceRepository placeRepository;

    @PostMapping
    public ResponseEntity<PlaceDto> createPlace(@Valid @RequestBody PlaceDto placeDto) {
        return new ResponseEntity<>(placeService.createPlace(placeDto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<PlaceDto> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlaceDto>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PlaceDto>> getPlacesByType(@PathVariable String type) {
        return ResponseEntity.ok(placeService.getPlacesByType(type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceDto> updatePlace(@PathVariable Long id, @Valid @RequestBody PlaceDto placeDto) {
        return ResponseEntity.ok(placeService.updatePlace(id, placeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaceById(@PathVariable Long id) {
        placeService.deletePlaceById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<PlaceDto>> getNearbyPlaces(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radiusInKm
    ) {
        return ResponseEntity.ok(placeService.getNearbyPlaces(latitude, longitude, radiusInKm));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlaceDto>> searchPlacesByName(@RequestParam String name) {
        List<PlaceDto> places = placeService.searchPlacesByName(name);
        return ResponseEntity.ok(places);
    }
}
