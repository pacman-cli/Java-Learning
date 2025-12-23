import React, {
    createContext,
    useContext,
    useState,
    useEffect,
    ReactNode,
} from "react";
import { toast } from "react-toastify";
import {
    AuthContextType,
    User,
    LoginRequest,
    RegisterRequest,
} from "../types/auth";
import apiService from "../services/api";

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const initializeAuth = async () => {
            const storedToken = localStorage.getItem("auth_token");
            const storedUser = localStorage.getItem("user");

            if (storedToken && storedUser) {
                try {
                    setToken(storedToken);
                    setUser(JSON.parse(storedUser));

                    // Verify token is still valid by fetching current user
                    const currentUser = await apiService.getCurrentUser();
                    setUser(currentUser);
                    localStorage.setItem("user", JSON.stringify(currentUser));
                } catch (error) {
                    // Token is invalid, clear storage
                    localStorage.removeItem("auth_token");
                    localStorage.removeItem("user");
                    setToken(null);
                    setUser(null);
                }
            }
            setLoading(false);
        };

        initializeAuth();
    }, []);

    const login = async (credentials: LoginRequest): Promise<void> => {
        try {
            setLoading(true);
            const response = await apiService.login(credentials);

            const userData: User = {
                id: response.id,
                username: response.username,
                email: response.email,
                firstName: response.firstName,
                lastName: response.lastName,
                role: response.role,
                isEnabled: true,
                isAccountNonExpired: true,
                isAccountNonLocked: true,
                isCredentialsNonExpired: true,
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString(),
            };

            setToken(response.token);
            setUser(userData);

            localStorage.setItem("auth_token", response.token);
            localStorage.setItem("user", JSON.stringify(userData));

            apiService.setToken(response.token);

            toast.success(`Welcome back, ${response.firstName}!`);
        } catch (error: any) {
            const errorMessage =
                error.response?.data?.message ||
                "Login failed. Please try again.";
            toast.error(errorMessage);
            throw error;
        } finally {
            setLoading(false);
        }
    };

    const register = async (userData: RegisterRequest): Promise<void> => {
        try {
            setLoading(true);
            const response = await apiService.register(userData);

            const newUser: User = {
                id: response.id,
                username: response.username,
                email: response.email,
                firstName: response.firstName,
                lastName: response.lastName,
                role: response.role,
                isEnabled: true,
                isAccountNonExpired: true,
                isAccountNonLocked: true,
                isCredentialsNonExpired: true,
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString(),
            };

            setToken(response.token);
            setUser(newUser);

            localStorage.setItem("auth_token", response.token);
            localStorage.setItem("user", JSON.stringify(newUser));

            apiService.setToken(response.token);

            toast.success(`Welcome to Auth Service, ${response.firstName}!`);
        } catch (error: any) {
            const errorMessage =
                error.response?.data?.message ||
                "Registration failed. Please try again.";
            toast.error(errorMessage);
            throw error;
        } finally {
            setLoading(false);
        }
    };

    const logout = (): void => {
        setUser(null);
        setToken(null);

        localStorage.removeItem("auth_token");
        localStorage.removeItem("user");

        apiService.removeToken();

        toast.info("You have been logged out successfully");
    };

    const isAuthenticated = !!user && !!token;

    const isAdmin = user?.role === "ADMIN";

    const isModerator = user?.role === "MODERATOR" || isAdmin;

    const contextValue: AuthContextType = {
        user,
        token,
        login,
        register,
        logout,
        isAuthenticated,
        isAdmin,
        isModerator,
        loading,
    };

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};

export default AuthContext;
