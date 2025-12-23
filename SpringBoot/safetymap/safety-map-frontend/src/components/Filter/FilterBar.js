import React from "react";

const FilterBar = ({ filter, setFilter }) => {
  return (
    <div style={{ marginBottom: "10px" }}>
      <button onClick={() => setFilter("ALL")} style={{ marginRight: "5px" }}>
        All
      </button>
      <button
        onClick={() => setFilter("HOSPITAL")}
        style={{ marginRight: "5px" }}
      >
        Hospitals
      </button>
      <button onClick={() => setFilter("POLICE_STATION")}>
        Police Stations
      </button>
    </div>
  );
};

export default FilterBar;
