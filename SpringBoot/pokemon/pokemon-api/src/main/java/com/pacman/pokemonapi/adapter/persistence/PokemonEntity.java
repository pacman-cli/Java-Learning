package com.pacman.pokemonapi.adapter.persistence;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "pokemon")
public class PokemonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private String description;
    private String type; // store as string of enum name
    private Integer hp;
    private String imageUrl;

}
