import api from "./api";

export const authAPI = {
  // Login user
  login: async (credentials) => {
    try {
      const response = await api.post("/auth/login", credentials);
      const { token, message } = response.data;

      if (token) {
        // Store token in localStorage
        localStorage.setItem("token", token);
        return { success: true, token };
      } else {
        return { success: false, error: message || "Login failed" };
      }
    } catch (error) {
      console.error("Login error:", error);
      return {
        success: false,
        error: error.response?.data?.message || "Login failed",
      };
    }
  },

  // Register user
  signup: async (userData) => {
    try {
      const response = await api.post("/auth/signup", userData);
      const { message } = response.data;

      return {
        success: true,
        message: message || "User registered successfully",
      };
    } catch (error) {
      console.error("Signup error:", error);
      return {
        success: false,
        error: error.response?.data?.message || "Signup failed",
      };
    }
  },

  // Logout user
  logout: () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  },

  // Check if user is authenticated
  isAuthenticated: () => {
    const token = localStorage.getItem("token");
    return !!token;
  },

  // Get token
  getToken: () => {
    return localStorage.getItem("token");
  },
};
