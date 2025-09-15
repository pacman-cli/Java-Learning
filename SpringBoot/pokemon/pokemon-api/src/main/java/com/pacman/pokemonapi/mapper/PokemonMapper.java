package com.pacman.pokemonapi.mapper;

import com.pacman.pokemonapi.adapter.persistence.PokemonEntity;
import com.pacman.pokemonapi.dto.PokemonDto;

public class PokemonMapper {
    public static PokemonDto toDto(PokemonEntity pokemonEntity) {
        if (pokemonEntity == null) return null;
        PokemonDto pokemonDto = new PokemonDto();
        pokemonDto.setId(pokemonEntity.getId());
        pokemonDto.setName(pokemonEntity.getName());
        pokemonDto.setDescription(pokemonEntity.getDescription());
        pokemonDto.setType(pokemonEntity.getType());
        pokemonDto.setHp(pokemonEntity.getHp());
        pokemonDto.setImageUrl(pokemonEntity.getImageUrl());
        return pokemonDto;
    }

    public static PokemonEntity toEntity(PokemonDto pokemonDto) {
        if (pokemonDto == null) return null;
        PokemonEntity pokemon = new PokemonEntity();
        pokemon.setId(pokemonDto.getId());
        pokemon.setName(pokemonDto.getName());
        pokemon.setDescription(pokemonDto.getDescription());
        pokemon.setType(pokemonDto.getType());
        pokemon.setHp(pokemonDto.getHp());
        pokemon.setImageUrl(pokemonDto.getImageUrl());
        return pokemon;
    }
}
