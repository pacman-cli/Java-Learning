import React from "react";
import "./Pokemon.css";

const PokemonCard = ({ pokemon, onEdit, onDelete, canModify }) => {
  const handleEdit = () => {
    onEdit(pokemon);
  };

  const handleDelete = () => {
    if (window.confirm(`Are you sure you want to delete ${pokemon.name}?`)) {
      onDelete(pokemon.id);
    }
  };

  return (
    <div className="pokemon-card">
      <div className="pokemon-image-container">
        {pokemon.imageUrl ? (
          <img
            src={pokemon.imageUrl}
            alt={pokemon.name}
            className="pokemon-image"
            onError={(e) => {
              e.target.style.display = "none";
              e.target.nextSibling.style.display = "flex";
            }}
          />
        ) : (
          <div className="pokemon-placeholder">
            <span>📱</span>
          </div>
        )}
        <div className="pokemon-placeholder" style={{ display: "none" }}>
          <span>📱</span>
        </div>
      </div>

      <div className="pokemon-info">
        <h3 className="pokemon-name">{pokemon.name}</h3>
        <p className="pokemon-description">{pokemon.description}</p>

        <div className="pokemon-stats">
          <span className={`pokemon-type type-${pokemon.type?.toLowerCase()}`}>
            {pokemon.type}
          </span>
          <span className="pokemon-hp">HP: {pokemon.hp}</span>
        </div>

        {canModify && (
          <div className="pokemon-actions">
            <button onClick={handleEdit} className="btn btn-edit">
              Edit
            </button>
            <button onClick={handleDelete} className="btn btn-delete">
              Delete
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default PokemonCard;
