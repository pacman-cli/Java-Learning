import React, { useState, useEffect } from "react";
import "./Pokemon.css";

const POKEMON_TYPES = [
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

const PokemonForm = ({ pokemon, onSubmit, onCancel, isEdit = false }) => {
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    type: "NORMAL",
    hp: 50,
    imageUrl: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (pokemon && isEdit) {
      setFormData({
        name: pokemon.name || "",
        description: pokemon.description || "",
        type: pokemon.type || "NORMAL",
        hp: pokemon.hp || 50,
        imageUrl: pokemon.imageUrl || "",
      });
    }
  }, [pokemon, isEdit]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "hp" ? parseInt(value) || 0 : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    // Validation
    if (!formData.name.trim()) {
      setError("Pokemon name is required");
      setLoading(false);
      return;
    }

    if (formData.hp < 1 || formData.hp > 999) {
      setError("HP must be between 1 and 999");
      setLoading(false);
      return;
    }

    try {
      await onSubmit(formData);
      if (!isEdit) {
        // Reset form for new Pokemon
        setFormData({
          name: "",
          description: "",
          type: "NORMAL",
          hp: 50,
          imageUrl: "",
        });
      }
    } catch (error) {
      setError(error.message || "Failed to save Pokemon");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="pokemon-form-container">
      <form onSubmit={handleSubmit} className="pokemon-form">
        <h3>{isEdit ? "Edit Pokemon" : "Add New Pokemon"}</h3>

        {error && <div className="error-message">{error}</div>}

        <div className="form-group">
          <label htmlFor="name">Name *</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
            disabled={loading}
            maxLength="50"
          />
        </div>

        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            disabled={loading}
            maxLength="200"
            rows="3"
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="type">Type</label>
            <select
              id="type"
              name="type"
              value={formData.type}
              onChange={handleChange}
              disabled={loading}
            >
              {POKEMON_TYPES.map((type) => (
                <option key={type} value={type}>
                  {type}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="hp">HP *</label>
            <input
              type="number"
              id="hp"
              name="hp"
              value={formData.hp}
              onChange={handleChange}
              required
              disabled={loading}
              min="1"
              max="999"
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="imageUrl">Image URL</label>
          <input
            type="url"
            id="imageUrl"
            name="imageUrl"
            value={formData.imageUrl}
            onChange={handleChange}
            disabled={loading}
            placeholder="https://example.com/pokemon-image.jpg"
          />
        </div>

        <div className="form-actions">
          <button type="submit" disabled={loading} className="btn btn-primary">
            {loading ? "Saving..." : isEdit ? "Update Pokemon" : "Add Pokemon"}
          </button>
          <button
            type="button"
            onClick={onCancel}
            disabled={loading}
            className="btn btn-secondary"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default PokemonForm;
