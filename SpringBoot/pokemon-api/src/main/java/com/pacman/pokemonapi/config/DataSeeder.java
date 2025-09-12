package com.pacman.pokemonapi.config;

import com.pacman.pokemonapi.adapter.persistence.PokemonEntity;
import com.pacman.pokemonapi.repository.PokemonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(PokemonRepository repo) {
        return args -> {
            if (repo.count() == 0) {

                PokemonEntity pikachu = new com.pacman.pokemonapi.adapter.persistence.PokemonEntity();
                pikachu.setName("Pikachu");
                pikachu.setDescription("Electric mouse Pokémon");
                pikachu.setType("ELECTRIC");
                pikachu.setHp(60);
                pikachu.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png");

                PokemonEntity bulbasaur = new PokemonEntity();
                bulbasaur.setName("Bulbasaur");
                bulbasaur.setDescription("Grass/Poison Pokémon");
                bulbasaur.setType("GRASS");
                bulbasaur.setHp(70);
                bulbasaur.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png");

                PokemonEntity charmander = new PokemonEntity();
                charmander.setName("Charmander");
                charmander.setDescription("Fire lizard Pokémon");
                charmander.setType("FIRE");
                charmander.setHp(65);
                charmander.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png");

                PokemonEntity squirtle = new PokemonEntity();
                squirtle.setName("Squirtle");
                squirtle.setDescription("Water turtle Pokémon");
                squirtle.setType("WATER");
                squirtle.setHp(60);
                squirtle.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png");

                PokemonEntity jigglypuff = new PokemonEntity();
                jigglypuff.setName("Jigglypuff");
                jigglypuff.setDescription("Balloon Pokémon");
                jigglypuff.setType("NORMAL");
                jigglypuff.setHp(70);
                jigglypuff.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/39.png");

                PokemonEntity meowth = new PokemonEntity();
                meowth.setName("Meowth");
                meowth.setDescription("Scratch Cat Pokémon");
                meowth.setType("NORMAL");
                meowth.setHp(65);
                meowth.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/52.png");

                PokemonEntity psyduck = new PokemonEntity();
                psyduck.setName("Psyduck");
                psyduck.setDescription("Duck Pokémon");
                psyduck.setType("WATER");
                psyduck.setHp(60);
                psyduck.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/54.png");

                PokemonEntity snorlax = new PokemonEntity();
                snorlax.setName("Snorlax");
                snorlax.setDescription("Sleeping Pokémon");
                snorlax.setType("NORMAL");
                snorlax.setHp(150);
                snorlax.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/143.png");

                PokemonEntity gengar = new PokemonEntity();
                gengar.setName("Gengar");
                gengar.setDescription("Shadow Pokémon");
                gengar.setType("GHOST");
                gengar.setHp(120);
                gengar.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/94.png");

                PokemonEntity eevee = new PokemonEntity();
                eevee.setName("Eevee");
                eevee.setDescription("Evolution Pokémon");
                eevee.setType("NORMAL");
                eevee.setHp(55);
                eevee.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/133.png");

                // Save all at once
                repo.save(pikachu);
                repo.save(bulbasaur);
                repo.save(charmander);
                repo.save(squirtle);
                repo.save(jigglypuff);
                repo.save(meowth);
                repo.save(psyduck);
                repo.save(snorlax);
                repo.save(gengar);
                repo.save(eevee);

                System.out.println("Seeded 10 Pokémon with images!");
            }
        };
    }
}
