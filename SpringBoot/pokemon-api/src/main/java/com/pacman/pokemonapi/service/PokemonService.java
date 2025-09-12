package com.pacman.pokemonapi.service;

import com.pacman.pokemonapi.adapter.persistence.PokemonEntity;
import com.pacman.pokemonapi.dto.PokemonDto;
import com.pacman.pokemonapi.mapper.PokemonMapper;
import com.pacman.pokemonapi.repository.PokemonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonService {
    private final PokemonRepository pokemonRepository;

    public PokemonService(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public List<PokemonDto> findAll(){
        return pokemonRepository.findAll().stream()
                .map(PokemonMapper::toDto)
                .collect(Collectors.toList());

    }

    public PokemonDto findById(Long id){
//        return PokemonMapper.toDto(pokemonRepository.findById(id).orElse(null)); --> this is one way to do it
        return pokemonRepository.findById(id)
                .map(PokemonMapper::toDto)
                .orElseThrow(()-> new RuntimeException("Pokemon with id " + id + "not found"));
    }

    public PokemonDto create(PokemonDto pokemonDto){
        PokemonEntity pokemonEntity = pokemonRepository.save(PokemonMapper.toEntity(pokemonDto));
        return PokemonMapper.toDto(pokemonEntity);
    }

    public PokemonDto update(Long id,PokemonDto pokemonDto){
        PokemonEntity existing = pokemonRepository.findById(id).orElseThrow(()-> new RuntimeException("Pokemon with id " + id + "not found"));
        existing.setName(pokemonDto.getName());
        existing.setDescription(pokemonDto.getDescription());
        existing.setType(pokemonDto.getType());
        existing.setHp(pokemonDto.getHp());
        return PokemonMapper.toDto(pokemonRepository.save(existing));
    }
    public void delete(Long id){
        pokemonRepository.deleteById(id);
    }
}
