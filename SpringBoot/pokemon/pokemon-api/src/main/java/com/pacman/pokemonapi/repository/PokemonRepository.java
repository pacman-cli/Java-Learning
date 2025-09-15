package com.pacman.pokemonapi.repository;

import com.pacman.pokemonapi.adapter.persistence.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository extends JpaRepository<PokemonEntity, Long> {
}
