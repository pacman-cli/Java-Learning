// src/components/Map/MapView.js
import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import SearchBar from "../Search/SearchBar";
import { getAllPlaces } from "../../api/placeApi";

// Fix Leaflet marker icons
import L from "leaflet";
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl:
    "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png",
  iconUrl:
    "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png",
  shadowUrl:
    "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png",
});

const MapView = () => {
  const [places, setPlaces] = useState([]);
  const [filteredPlaces, setFilteredPlaces] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterType, setFilterType] = useState("ALL");

  // Fetch places from backend
  useEffect(() => {
    getAllPlaces().then((res) => {
      setPlaces(res.data);
      setFilteredPlaces(res.data);
    });
  }, []);

  // Filtering logic
  useEffect(() => {
    let updated = [...places];

    // filter by type (HOSPITAL / POLICE_STATION / ALL)
    if (filterType !== "ALL") {
      updated = updated.filter((p) => p.type === filterType);
    }

    // filter by search term
    if (searchTerm.trim() !== "") {
      updated = updated.filter((p) =>
        p.name.toLowerCase().includes(searchTerm.toLowerCase()),
      );
    }

    setFilteredPlaces(updated);
  }, [searchTerm, filterType, places]);

  return (
    <div style={{ height: "100vh", width: "100%" }}>
      {/* Search + Filter Buttons */}
      <div style={{ padding: "10px", background: "#f8f8f8" }}>
        <SearchBar searchTerm={searchTerm} onSearch={setSearchTerm} />

        <div style={{ marginTop: "10px" }}>
          <button onClick={() => setFilterType("ALL")}>All</button>
          <button onClick={() => setFilterType("HOSPITAL")}>Hospitals</button>
          <button onClick={() => setFilterType("POLICE_STATION")}>
            Police Stations
          </button>
        </div>
      </div>

      {/* Map */}
      <MapContainer
        center={[23.8103, 90.4125]} // Dhaka default center
        zoom={13}
        style={{ height: "90%", width: "100%" }}
      >
        <TileLayer
          attribution="&copy; OpenStreetMap contributors"
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {filteredPlaces.map((place) => (
          <Marker key={place.id} position={[place.latitude, place.longitude]}>
            <Popup>
              <b>{place.name}</b>
              <br />
              {place.type}
              <br />
              {place.address}
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
};

export default MapView;
