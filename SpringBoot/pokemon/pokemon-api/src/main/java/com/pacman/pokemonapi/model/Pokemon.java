package com.pacman.pokemonapi.model;

import com.pacman.pokemonapi.enums.PokemonType;
import lombok.Data;

@Data
public class Pokemon {
    private Long id;
    private String name;
    private String description;
    private PokemonType type;
    private Integer hp;
    private String imageUrl;
}
