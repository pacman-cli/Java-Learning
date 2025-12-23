"use client";

import React, { createContext, useContext, useEffect, useState } from "react";

type Theme = "light" | "dark" | "system";

interface ThemeContextType {
    theme: Theme;
    actualTheme: "light" | "dark";
    setTheme: (theme: Theme) => void;
    toggleTheme: () => void;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export function ThemeProvider({ children }: { children: React.ReactNode }) {
    const [theme, setThemeState] = useState<Theme>("system");
    const [actualTheme, setActualTheme] = useState<"light" | "dark">("light");

    // Initialize theme from localStorage
    useEffect(() => {
        const savedTheme = localStorage.getItem("theme") as Theme;
        if (savedTheme && ["light", "dark", "system"].includes(savedTheme)) {
            setThemeState(savedTheme);
        }
    }, []);

    // Update actual theme based on theme setting and system preference
    useEffect(() => {
        const updateActualTheme = () => {
            if (theme === "system") {
                const systemPrefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
                setActualTheme(systemPrefersDark ? "dark" : "light");
            } else {
                setActualTheme(theme);
            }
        };

        updateActualTheme();

        // Listen for system theme changes
        const mediaQuery = window.matchMedia("(prefers-color-scheme: dark)");
        const handleChange = () => {
            if (theme === "system") {
                updateActualTheme();
            }
        };

        mediaQuery.addEventListener("change", handleChange);
        return () => mediaQuery.removeEventListener("change", handleChange);
    }, [theme]);

    // Apply theme to document
    useEffect(() => {
        const root = document.documentElement;

        // Remove all theme classes
        root.classList.remove("light", "dark");

        // Add current theme class
        root.classList.add(actualTheme);

        // Update meta theme-color for mobile browsers
        const metaThemeColor = document.querySelector('meta[name="theme-color"]');
        if (metaThemeColor) {
            metaThemeColor.setAttribute(
                "content",
                actualTheme === "dark" ? "#0a0a0a" : "#ffffff"
            );
        } else {
            // Create meta theme-color if it doesn't exist
            const meta = document.createElement("meta");
            meta.name = "theme-color";
            meta.content = actualTheme === "dark" ? "#0a0a0a" : "#ffffff";
            document.head.appendChild(meta);
        }
    }, [actualTheme]);

    const setTheme = (newTheme: Theme) => {
        setThemeState(newTheme);
        localStorage.setItem("theme", newTheme);
    };

    const toggleTheme = () => {
        const newTheme = actualTheme === "light" ? "dark" : "light";
        setTheme(newTheme);
    };

    const value: ThemeContextType = {
        theme,
        actualTheme,
        setTheme,
        toggleTheme,
    };

    return (
        <ThemeContext.Provider value={value}>
            {children}
        </ThemeContext.Provider>
    );
}

export function useTheme() {
    const context = useContext(ThemeContext);
    if (context === undefined) {
        throw new Error("useTheme must be used within a ThemeProvider");
    }
    return context;
}

// Theme toggle component
export function ThemeToggle() {
    const { theme, actualTheme, setTheme } = useTheme();

    const themes: { value: Theme; label: string; icon: string }[] = [
        { value: "light", label: "Light", icon: "☀️" },
        { value: "dark", label: "Dark", icon: "🌙" },
        { value: "system", label: "System", icon: "💻" },
    ];

    return (
        <div className="relative">
            <select
                value={theme}
                onChange={(e) => setTheme(e.target.value as Theme)}
                className="input h-9 w-32 cursor-pointer text-sm"
                aria-label="Select theme"
            >
                {themes.map((themeOption) => (
                    <option key={themeOption.value} value={themeOption.value}>
                        {themeOption.icon} {themeOption.label}
                    </option>
                ))}
            </select>
        </div>
    );
}

// Simple theme toggle button
export function ThemeToggleButton() {
    const { actualTheme, toggleTheme } = useTheme();

    return (
        <button
            onClick={toggleTheme}
            className="btn-ghost btn-sm flex items-center gap-2"
            aria-label={`Switch to ${actualTheme === "light" ? "dark" : "light"} mode`}
        >
            <span className="text-lg">
                {actualTheme === "light" ? "🌙" : "☀️"}
            </span>
            <span className="hidden sm:inline">
                {actualTheme === "light" ? "Dark" : "Light"}
            </span>
        </button>
    );
}
