package com.pacman.pokemonapi.mapper;

import com.pacman.pokemonapi.adapter.persistence.UserEntity;
import com.pacman.pokemonapi.dto.UserRequestDto;
import com.pacman.pokemonapi.dto.UserResponseDto;

public class UserMapper {
    // Entity -> ResponseDto (for sending to a client)
    public static UserResponseDto toResponseDto(UserEntity userEntity) {
        if (userEntity == null) return null;
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .build();
    }

    // RequestDto -> Entity (for signup/login)
    public static UserEntity toEntity(UserRequestDto userRequestDto) {
        if (userRequestDto == null) return null;
        return UserEntity.builder()
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .build();
    }
}
