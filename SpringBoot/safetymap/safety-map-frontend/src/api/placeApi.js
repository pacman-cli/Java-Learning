import axios from "axios";

// Base URL of Spring Boot backend
const API_BASE = "http://localhost:8080/api/places";

// Get all places
export const getAllPlaces = () => axios.get(API_BASE);

// Get nearby places by latitude, longitude, and radius (in km)
export const getNearbyPlaces = (lat, lon, radius) =>
  axios.get(`${API_BASE}/nearby`, { params: { lat, lon, radius } });

// Search places by name
export const searchPlaces = (name) =>
  axios.get(`${API_BASE}/search`, { params: { name } });
