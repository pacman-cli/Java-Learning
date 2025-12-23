package com.pacman.safetymap.modules.place.mapper;

import com.pacman.safetymap.modules.place.dto.PlaceDto;
import com.pacman.safetymap.modules.place.entity.Place;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaceMapper {
    public PlaceDto toDto(Place place){
        if (place ==null){
            return null;
        }
        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .type(place.getType())
                .build();
    }

    public Place toEntity(PlaceDto placeDto){
        if (placeDto == null){
            return null;
        }
        return Place.builder()
                .id(placeDto.getId())
                .name(placeDto.getName())
                .address(placeDto.getAddress())
                .latitude(placeDto.getLatitude())
                .longitude(placeDto.getLongitude())
                .type(placeDto.getType())
                .build();
    }
}
