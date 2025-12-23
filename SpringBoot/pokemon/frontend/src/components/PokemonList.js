import React, { useState, useEffect } from "react";
import { pokemonAPI } from "../services/pokemonService";
import { authAPI } from "../services/authService";
import PokemonCard from "./PokemonCard";
import PokemonForm from "./PokemonForm";
import "./Pokemon.css";

const PokemonList = () => {
  const [pokemon, setPokemon] = useState([]);
  const [filteredPokemon, setFilteredPokemon] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [editingPokemon, setEditingPokemon] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState("ALL");

  const POKEMON_TYPES = [
    "ALL",
    "NORMAL",
    "FIRE",
    "WATER",
    "ELECTRIC",
    "GRASS",
    "ICE",
    "FIGHTING",
    "POISON",
    "GROUND",
    "FLYING",
    "PSYCHIC",
    "ROCK",
    "GHOST",
    "DARK",
    "STEEL",
  ];

  useEffect(() => {
    fetchPokemon();
  }, []);

  const filterPokemon = () => {
    let filtered = pokemon;

    // Filter by search term
    if (searchTerm) {
      filtered = filtered.filter(
        (p) =>
          p.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          p.description.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Filter by type
    if (typeFilter !== "ALL") {
      filtered = filtered.filter((p) => p.type === typeFilter);
    }

    setFilteredPokemon(filtered);
  };

  useEffect(() => {
    filterPokemon();
  }, [pokemon, searchTerm, typeFilter]); // eslint-disable-line react-hooks/exhaustive-deps

  const fetchPokemon = async () => {
    setLoading(true);
    try {
      const result = await pokemonAPI.getAll();
      if (result.success) {
        setPokemon(result.data);
      } else {
        setError(result.error);
      }
    } catch (error) {
      setError("Failed to fetch Pokemon");
    } finally {
      setLoading(false);
    }
  };

  const handleAddPokemon = async (pokemonData) => {
    const result = await pokemonAPI.create(pokemonData);
    if (result.success) {
      setPokemon((prev) => [...prev, result.data]);
      setShowForm(false);
      setError("");
    } else {
      throw new Error(result.error);
    }
  };

  const handleEditPokemon = async (pokemonData) => {
    const result = await pokemonAPI.update(editingPokemon.id, pokemonData);
    if (result.success) {
      setPokemon((prev) =>
        prev.map((p) => (p.id === editingPokemon.id ? result.data : p))
      );
      setEditingPokemon(null);
      setShowForm(false);
      setError("");
    } else {
      throw new Error(result.error);
    }
  };

  const handleDeletePokemon = async (pokemonId) => {
    const result = await pokemonAPI.delete(pokemonId);
    if (result.success) {
      setPokemon((prev) => prev.filter((p) => p.id !== pokemonId));
      setError("");
    } else {
      setError(result.error);
    }
  };

  const startEdit = (pokemon) => {
    setEditingPokemon(pokemon);
    setShowForm(true);
  };

  const cancelForm = () => {
    setShowForm(false);
    setEditingPokemon(null);
  };

  const handleLogout = () => {
    authAPI.logout();
    window.location.reload();
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading Pokemon...</p>
      </div>
    );
  }

  return (
    <div className="pokemon-manager">
      <header className="pokemon-header">
        <h1>🔥 Pokemon Manager</h1>
        <button onClick={handleLogout} className="btn btn-logout">
          Logout
        </button>
      </header>

      {error && (
        <div className="error-message">
          {error}
          <button onClick={() => setError("")} className="close-error">
            ×
          </button>
        </div>
      )}

      <div className="pokemon-controls">
        <div className="search-filters">
          <input
            type="text"
            placeholder="Search Pokemon..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />

          <select
            value={typeFilter}
            onChange={(e) => setTypeFilter(e.target.value)}
            className="type-filter"
          >
            {POKEMON_TYPES.map((type) => (
              <option key={type} value={type}>
                {type === "ALL" ? "All Types" : type}
              </option>
            ))}
          </select>
        </div>

        <button
          onClick={() => setShowForm(!showForm)}
          className="btn btn-primary"
        >
          {showForm ? "Cancel" : "+ Add Pokemon"}
        </button>
      </div>

      {showForm && (
        <PokemonForm
          pokemon={editingPokemon}
          onSubmit={editingPokemon ? handleEditPokemon : handleAddPokemon}
          onCancel={cancelForm}
          isEdit={!!editingPokemon}
        />
      )}

      <div className="pokemon-stats">
        <p>
          Showing {filteredPokemon.length} of {pokemon.length} Pokemon
        </p>
      </div>

      {filteredPokemon.length === 0 ? (
        <div className="no-pokemon">
          <p>
            No Pokemon found.{" "}
            {pokemon.length === 0
              ? "Add your first Pokemon!"
              : "Try adjusting your search or filters."}
          </p>
        </div>
      ) : (
        <div className="pokemon-grid">
          {filteredPokemon.map((p) => (
            <PokemonCard
              key={p.id}
              pokemon={p}
              onEdit={startEdit}
              onDelete={handleDeletePokemon}
              canModify={true}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default PokemonList;
