import api from "./api";

export const pokemonAPI = {
  // Get all Pokemon
  getAll: async () => {
    try {
      const response = await api.get("/pokemons");
      return { success: true, data: response.data };
    } catch (error) {
      console.error("Error fetching Pokemon:", error);
      return {
        success: false,
        error: error.response?.data?.message || "Failed to fetch Pokemon",
      };
    }
  },

  // Get Pokemon by ID
  getById: async (id) => {
    try {
      const response = await api.get(`/pokemons/${id}`);
      return { success: true, data: response.data };
    } catch (error) {
      console.error("Error fetching Pokemon:", error);
      return {
        success: false,
        error: error.response?.data?.message || "Failed to fetch Pokemon",
      };
    }
  },

  // Create new Pokemon
  create: async (pokemonData) => {
    try {
      const response = await api.post("/pokemons", pokemonData);
      return { success: true, data: response.data };
    } catch (error) {
      console.error("Error creating Pokemon:", error);
      return {
        success: false,
        error: error.response?.data?.message || "Failed to create Pokemon",
      };
    }
  },

  // Update Pokemon
  update: async (id, pokemonData) => {
    try {
      const response = await api.put(`/pokemons/${id}`, pokemonData);
      return { success: true, data: response.data };
    } catch (error) {
      console.error("Error updating Pokemon:", error);
      return {
        success: false,
        error: error.response?.data?.message || "Failed to update Pokemon",
      };
    }
  },

  // Delete Pokemon
  delete: async (id) => {
    try {
      await api.delete(`/pokemons/${id}`);
      return { success: true };
    } catch (error) {
      console.error("Error deleting Pokemon:", error);
      return {
        success: false,
        error: error.response?.data?.message || "Failed to delete Pokemon",
      };
    }
  },
};
