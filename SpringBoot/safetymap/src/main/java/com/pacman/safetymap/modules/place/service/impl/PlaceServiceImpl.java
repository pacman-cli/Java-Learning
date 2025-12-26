package com.pacman.safetymap.modules.place.service.impl;

import com.pacman.safetymap.common.exception.ResourceNotFoundException;
import com.pacman.safetymap.modules.place.dto.PlaceDto;
import com.pacman.safetymap.modules.place.entity.Place;
import com.pacman.safetymap.modules.place.mapper.PlaceMapper;
import com.pacman.safetymap.modules.place.repository.PlaceRepository;
import com.pacman.safetymap.modules.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //this will inject all the dependencies in the constructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    @Override
    public PlaceDto createPlace(PlaceDto placeDto) {
        Place place = placeMapper.toEntity(placeDto);
        Place saved = placeRepository.save(place);
        return placeMapper.toDto(saved);
    }

    @Override
    public PlaceDto getPlaceById(Long id) {
        Place place = placeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Place not found " +
                "with " +
                "id: " + id));
        return placeMapper.toDto(place);
    }

    @Override
    public List<PlaceDto> getAllPlaces() {
        return placeRepository.findAll()
                .stream()
                .map(placeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceDto> getPlacesByType(String type) {
        return placeRepository.findByType(type.toUpperCase())
                .stream()
                .map(placeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlaceDto updatePlace(Long id, PlaceDto placeDto) {
        Place place = placeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Place not found with " + "id: " + id));
        place.setName(placeDto.getName());
        place.setAddress(placeDto.getAddress());
        place.setLongitude(placeDto.getLongitude());
        place.setType(placeDto.getType());
        place.setLatitude(placeDto.getLatitude());
        Place updatedPlace = placeRepository.save(place);

        return placeMapper.toDto(updatedPlace);
    }

    @Override
    public void deletePlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with id: " + id));
        placeRepository.delete(place);
    }

    @Override
    public List<PlaceDto> getNearbyPlaces(double latitude, double longitude, double radiusInKm) {
        List<Place> nearbyPlaces = placeRepository.findNearbyPlaces(latitude, longitude, radiusInKm);
        return nearbyPlaces.stream()
                .map(placeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceDto> searchPlacesByName(String name) {
        List<Place> places = placeRepository.findByNameContainingIgnoreCase(name);
        return places.stream()
                .map(placeMapper::toDto)
                .collect(Collectors.toList());
    }
}
