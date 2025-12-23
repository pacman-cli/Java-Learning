"use client";

import React, { createContext, useContext, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Cookies from "js-cookie";
import toast from "react-hot-toast";

export interface User {
  id: number;
  username: string;
  fullName: string;
  email: string;
  roles: string[];
  expiresAt: string;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface RegisterData {
  username: string;
  password: string;
  fullName: string;
  email: string;
  roles: string[];
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  fullName: string;
  email: string;
  roles: string[];
  expiresAt: string;
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => void;
  register: (data: RegisterData) => Promise<void>;
  hasRole: (role: string) => boolean;
  hasAnyRole: (roles: string[]) => boolean;
  refreshUser: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  const isAuthenticated = !!user && !!token;

  // Initialize auth state from cookies
  useEffect(() => {
    const initAuth = () => {
      try {
        const storedToken = Cookies.get("authToken");
        const storedUser = Cookies.get("authUser");

        if (storedToken && storedUser) {
          const parsedUser = JSON.parse(storedUser);

          // Check if token is expired
          if (
            parsedUser.expiresAt &&
            new Date(parsedUser.expiresAt) > new Date()
          ) {
            setToken(storedToken);
            setUser(parsedUser);
          } else {
            // Token expired, clear auth data
            clearAuthData();
          }
        }
      } catch (error) {
        console.error("Error initializing auth:", error);
        clearAuthData();
      } finally {
        setIsLoading(false);
      }
    };

    initAuth();
  }, []);

  const clearAuthData = () => {
    setUser(null);
    setToken(null);
    Cookies.remove("authToken");
    Cookies.remove("authUser");
  };

  const saveAuthData = (authResponse: AuthResponse) => {
    const userData: User = {
      id: authResponse.id,
      username: authResponse.username,
      fullName: authResponse.fullName,
      email: authResponse.email,
      roles: authResponse.roles,
      expiresAt: authResponse.expiresAt,
    };

    setToken(authResponse.token);
    setUser(userData);

    // Save to cookies with expiration
    const expiresAt = new Date(authResponse.expiresAt);
    Cookies.set("authToken", authResponse.token, {
      expires: expiresAt,
      secure: process.env.NODE_ENV === "production",
      sameSite: "lax",
    });
    Cookies.set("authUser", JSON.stringify(userData), {
      expires: expiresAt,
      secure: process.env.NODE_ENV === "production",
      sameSite: "lax",
    });
  };

  const login = async (credentials: LoginCredentials) => {
    setIsLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || "Login failed");
      }

      const authResponse: AuthResponse = await response.json();
      saveAuthData(authResponse);

      toast.success(`Welcome back, ${authResponse.fullName}!`);
      router.push("/dashboard");
    } catch (error) {
      const message = error instanceof Error ? error.message : "Login failed";
      toast.error(message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const register = async (data: RegisterData) => {
    setIsLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || "Registration failed");
      }

      await response.json();

      toast.success(
        `Registration successful! Please login with your credentials.`,
      );

      // Auto-login after successful registration
      await login({
        username: data.username,
        password: data.password,
      });
    } catch (error) {
      const message =
        error instanceof Error ? error.message : "Registration failed";
      toast.error(message);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    clearAuthData();
    toast.success("Logged out successfully");
    router.push("/login");
  };

  const refreshUser = async () => {
    if (!token) return;

    try {
      const response = await fetch(`${API_BASE_URL}/api/auth/me`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to refresh user data");
      }

      const userData: User = await response.json();
      setUser(userData);

      // Update cookie
      const expiresAt = new Date(userData.expiresAt);
      Cookies.set("authUser", JSON.stringify(userData), {
        expires: expiresAt,
        secure: process.env.NODE_ENV === "production",
        sameSite: "lax",
      });
    } catch (error) {
      console.error("Error refreshing user:", error);
      // If refresh fails, logout user
      logout();
    }
  };

  const hasRole = (role: string): boolean => {
    if (!user) return false;
    return user.roles.includes(role) || user.roles.includes(`ROLE_${role}`);
  };

  const hasAnyRole = (roles: string[]): boolean => {
    if (!user) return false;
    return roles.some((role) => hasRole(role));
  };

  // Auto logout when token expires
  useEffect(() => {
    if (user?.expiresAt) {
      const expiresAt = new Date(user.expiresAt);
      const now = new Date();
      const timeUntilExpiry = expiresAt.getTime() - now.getTime();

      if (timeUntilExpiry > 0) {
        const timeoutId = setTimeout(() => {
          toast.error("Session expired. Please login again.");
          logout();
        }, timeUntilExpiry);

        return () => clearTimeout(timeoutId);
      } else if (isAuthenticated) {
        // Token already expired
        logout();
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user, isAuthenticated]);

  const value: AuthContextType = {
    user,
    token,
    isLoading,
    isAuthenticated,
    login,
    logout,
    register,
    hasRole,
    hasAnyRole,
    refreshUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}

// Higher-order component for protected routes
export function withAuth<T extends Record<string, unknown>>(
  Component: React.ComponentType<T>,
  requiredRoles?: string[],
) {
  return function AuthenticatedComponent(props: T) {
    const { isAuthenticated, isLoading, hasAnyRole } = useAuth();
    const router = useRouter();

    useEffect(() => {
      if (!isLoading) {
        if (!isAuthenticated) {
          router.push("/login");
          return;
        }

        if (requiredRoles && !hasAnyRole(requiredRoles)) {
          toast.error("You don't have permission to access this page");
          router.push("/dashboard");
          return;
        }
      }
    }, [isAuthenticated, isLoading, hasAnyRole, router]);

    if (isLoading) {
      return (
        <div className="flex min-h-screen items-center justify-center">
          <div className="loading-spinner h-8 w-8"></div>
        </div>
      );
    }

    if (!isAuthenticated) {
      return null;
    }

    if (requiredRoles && !hasAnyRole(requiredRoles)) {
      return null;
    }

    return <Component {...props} />;
  };
}

// Hook to check if user has specific permissions
export function usePermissions() {
  const { hasRole, hasAnyRole, user } = useAuth();

  const canAccessPatients = () =>
    hasAnyRole(["PATIENT", "DOCTOR", "NURSE", "ADMIN", "RECEPTIONIST"]);
  const canManagePatients = () =>
    hasAnyRole(["DOCTOR", "NURSE", "ADMIN", "RECEPTIONIST"]);
  const canAccessDoctors = () =>
    hasAnyRole([
      "DOCTOR",
      "ADMIN",
      "DEPARTMENT_HEAD",
      "PATIENT",
      "RECEPTIONIST",
      "NURSE",
    ]);
  const canManageDoctors = () => hasAnyRole(["ADMIN", "DEPARTMENT_HEAD"]);
  const canAccessAppointments = () =>
    hasAnyRole(["PATIENT", "DOCTOR", "NURSE", "ADMIN", "RECEPTIONIST"]);
  const canManageAppointments = () =>
    hasAnyRole(["DOCTOR", "ADMIN", "RECEPTIONIST"]);
  const canAccessBilling = () => hasAnyRole(["BILLING_STAFF", "ADMIN"]);
  const canAccessPharmacy = () => hasAnyRole(["PHARMACIST", "ADMIN"]);
  const canAccessLaboratory = () =>
    hasAnyRole(["LAB_TECHNICIAN", "DOCTOR", "ADMIN"]);
  const canAccessReports = () =>
    hasAnyRole(["ADMIN", "HOSPITAL_MANAGER", "DEPARTMENT_HEAD"]);
  const isAdmin = () => hasRole("ADMIN");
  const isDoctor = () => hasRole("DOCTOR");
  const isPatient = () => hasRole("PATIENT");

  return {
    canAccessPatients,
    canManagePatients,
    canAccessDoctors,
    canManageDoctors,
    canAccessAppointments,
    canManageAppointments,
    canAccessBilling,
    canAccessPharmacy,
    canAccessLaboratory,
    canAccessReports,
    isAdmin,
    isDoctor,
    isPatient,
    user,
  };
}
