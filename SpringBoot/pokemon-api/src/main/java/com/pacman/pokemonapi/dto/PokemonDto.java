package com.pacman.pokemonapi.dto;

import com.pacman.pokemonapi.enums.PokemonType;
import lombok.Data;

@Data
public class PokemonDto {
    private Long id;
    private String name;
    private String description;
    private String type; //string for api simplicity
    private Integer hp;
    private String imageUrl;
}

