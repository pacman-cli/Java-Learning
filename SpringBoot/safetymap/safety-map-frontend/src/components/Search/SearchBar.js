// src/components/Search/SearchBar.js
import React from "react";

const SearchBar = ({ searchTerm, onSearch }) => {
  return (
    <div style={{ marginBottom: "1rem" }}>
      <input
        type="text"
        value={searchTerm}
        onChange={(e) => onSearch(e.target.value)}
        placeholder="Search places..."
        style={{
          width: "100%",
          padding: "8px 12px",
          fontSize: "16px",
          borderRadius: "6px",
          border: "1px solid #ccc",
        }}
      />
    </div>
  );
};

export default SearchBar;
