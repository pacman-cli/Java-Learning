import React from "react";
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate,
} from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Layout from "./components/Layout";

// Pages
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Profile from "./pages/Profile";
import Users from "./pages/Users";
import Admin from "./pages/Admin";
import Settings from "./pages/Settings";
import CodeDemo from "./pages/CodeDemo";
import Unauthorized from "./pages/Unauthorized";
import NotFound from "./pages/NotFound";

// Create a client for React Query
const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            retry: 1,
            staleTime: 5 * 60 * 1000, // 5 minutes
            cacheTime: 10 * 60 * 1000, // 10 minutes
        },
    },
});

const App: React.FC = () => {
    return (
        <QueryClientProvider client={queryClient}>
            <Router>
                <AuthProvider>
                    <div className="App">
                        <Routes>
                            {/* Public routes */}
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />

                            {/* Protected routes */}
                            <Route
                                path="/dashboard"
                                element={
                                    <ProtectedRoute>
                                        <Layout>
                                            <Dashboard />
                                        </Layout>
                                    </ProtectedRoute>
                                }
                            />

                            <Route
                                path="/profile"
                                element={
                                    <ProtectedRoute>
                                        <Layout>
                                            <Profile />
                                        </Layout>
                                    </ProtectedRoute>
                                }
                            />

                            <Route
                                path="/users"
                                element={
                                    <ProtectedRoute requireModerator={true}>
                                        <Layout>
                                            <Users />
                                        </Layout>
                                    </ProtectedRoute>
                                }
                            />

                            <Route
                                path="/admin"
                                element={
                                    <ProtectedRoute requireAdmin={true}>
                                        <Layout>
                                            <Admin />
                                        </Layout>
                                    </ProtectedRoute>
                                }
                            />

                            <Route
                                path="/settings"
                                element={
                                    <ProtectedRoute>
                                        <Layout>
                                            <Settings />
                                        </Layout>
                                    </ProtectedRoute>
                                }
                            />

                            <Route
                                path="/code-demo"
                                element={
                                    <ProtectedRoute>
                                        <Layout>
                                            <CodeDemo />
                                        </Layout>
                                    </ProtectedRoute>
                                }
                            />

                            {/* Error pages */}
                            <Route
                                path="/unauthorized"
                                element={<Unauthorized />}
                            />
                            <Route path="/404" element={<NotFound />} />

                            {/* Redirects */}
                            <Route
                                path="/"
                                element={<Navigate to="/dashboard" replace />}
                            />
                            <Route
                                path="*"
                                element={<Navigate to="/404" replace />}
                            />
                        </Routes>

                        {/* Toast notifications */}
                        <ToastContainer
                            position="top-right"
                            autoClose={5000}
                            hideProgressBar={false}
                            newestOnTop={false}
                            closeOnClick
                            rtl={false}
                            pauseOnFocusLoss
                            draggable
                            pauseOnHover
                            theme="light"
                            className="mt-16"
                        />
                    </div>
                </AuthProvider>
            </Router>
        </QueryClientProvider>
    );
};

export default App;
