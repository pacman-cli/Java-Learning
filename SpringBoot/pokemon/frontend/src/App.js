import React, { useState, useEffect } from "react";
import { authAPI } from "./services/authService";
import AuthPage from "./components/AuthPage";
import PokemonList from "./components/PokemonList";
import "./App.css";

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is already logged in
    const checkAuth = () => {
      const isAuth = authAPI.isAuthenticated();
      setIsAuthenticated(isAuth);
      setLoading(false);
    };

    checkAuth();
  }, []);

  const handleLogin = (token) => {
    setIsAuthenticated(true);
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading...</p>
      </div>
    );
  }

  return (
    <div className="App">
      {isAuthenticated ? <PokemonList /> : <AuthPage onLogin={handleLogin} />}
    </div>
  );
}

export default App;
