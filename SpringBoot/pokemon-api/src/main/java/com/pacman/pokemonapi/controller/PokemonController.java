package com.pacman.pokemonapi.controller;

import com.pacman.pokemonapi.dto.PokemonDto;
import com.pacman.pokemonapi.repository.PokemonRepository;
import com.pacman.pokemonapi.service.PokemonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pokemons")
//@CrossOrigin(origins = "http://localhost:3000")
public class PokemonController {
    private final PokemonService pokemonService;

    public PokemonController(PokemonRepository pokemonRepository, PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public List<PokemonDto> getAll() {
        return pokemonService.findAll();
    }

    @GetMapping("/{id}")
    public PokemonDto getById(@PathVariable Long id) {
        return pokemonService.findById(id);
    }

    @PostMapping
    public PokemonDto create(@RequestBody PokemonDto pokemonDto) {
        return pokemonService.create(pokemonDto);
    }

    @PutMapping("/{id}")
    public PokemonDto update(@PathVariable Long id, @RequestBody PokemonDto pokemonDto) {
        return pokemonService.update(id, pokemonDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pokemonService.delete(id);
    }
}
