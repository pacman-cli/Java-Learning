package com.pacman.safetymap.modules.place.service;

import com.pacman.safetymap.modules.place.dto.PlaceDto;
import com.pacman.safetymap.modules.place.entity.Place;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceService {
    PlaceDto createPlace(PlaceDto placeDto);

    PlaceDto getPlaceById(Long id);

    List<PlaceDto> getAllPlaces();

    List<PlaceDto> getPlacesByType(String type);

    PlaceDto updatePlace(Long id, PlaceDto placeDto);

    void deletePlaceById(Long id);

    List<PlaceDto> getNearbyPlaces(double latitude, double longitude, double radiusInKm);

    List<PlaceDto> searchPlacesByName(String name);

}
