"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Eye, EyeOff, Hospital, Lock, User } from "lucide-react";
import { useAuth } from "@/providers/AuthProvider";
import { ThemeToggleButton } from "@/providers/ThemeProvider";
import toast from "react-hot-toast";

const loginSchema = z.object({
    username: z
        .string()
        .min(1, "Username is required")
        .min(3, "Username must be at least 3 characters"),
    password: z
        .string()
        .min(1, "Password is required")
        .min(6, "Password must be at least 6 characters"),
});

type LoginFormData = z.infer<typeof loginSchema>;

export default function LoginPage() {
    const [showPassword, setShowPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const { login, isAuthenticated } = useAuth();
    const router = useRouter();

    const {
        register,
        handleSubmit,
        formState: { errors },
        setFocus,
    } = useForm<LoginFormData>({
        resolver: zodResolver(loginSchema),
        defaultValues: {
            username: "",
            password: "",
        },
    });

    // Redirect if already authenticated
    useEffect(() => {
        if (isAuthenticated) {
            router.push("/dashboard");
        }
    }, [isAuthenticated, router]);

    // Focus on username field when component mounts
    useEffect(() => {
        setFocus("username");
    }, [setFocus]);

    const onSubmit = async (data: LoginFormData) => {
        setIsLoading(true);
        try {
            await login(data);
        } catch (error) {
            // Error is already handled in the login function
            console.error("Login error:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    // Demo credentials for testing
    const demoCredentials = [
        {
            role: "Admin",
            username: "admin",
            password: "admin123",
            description: "Full system access"
        },
        {
            role: "Doctor",
            username: "doctor",
            password: "doctor123",
            description: "Medical staff access"
        },
        {
            role: "Patient",
            username: "patient",
            password: "patient123",
            description: "Patient portal access"
        },
    ];

    const fillDemoCredentials = (username: string, password: string) => {
        const form = document.getElementById("login-form") as HTMLFormElement;
        if (form) {
            (form.elements.namedItem("username") as HTMLInputElement).value = username;
            (form.elements.namedItem("password") as HTMLInputElement).value = password;
        }
    };

    return (
        <div className="min-h-screen flex">
            {/* Left Panel - Login Form */}
            <div className="flex-1 flex items-center justify-center px-4 sm:px-6 lg:px-8 bg-white dark:bg-neutral-900">
                <div className="max-w-md w-full space-y-8">
                    {/* Header */}
                    <div className="text-center">
                        <div className="mx-auto h-16 w-16 flex items-center justify-center rounded-full bg-primary-100 dark:bg-primary-900">
                            <Hospital className="h-8 w-8 text-primary-600 dark:text-primary-400" />
                        </div>
                        <h2 className="mt-6 text-3xl font-bold text-neutral-900 dark:text-neutral-100">
                            Welcome back
                        </h2>
                        <p className="mt-2 text-sm text-neutral-600 dark:text-neutral-400">
                            Sign in to your hospital management account
                        </p>
                    </div>

                    {/* Login Form */}
                    <form
                        id="login-form"
                        className="mt-8 space-y-6"
                        onSubmit={handleSubmit(onSubmit)}
                    >
                        <div className="space-y-4">
                            {/* Username Field */}
                            <div className="form-group">
                                <label htmlFor="username" className="form-label">
                                    Username
                                </label>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <User className="h-5 w-5 text-neutral-400" />
                                    </div>
                                    <input
                                        {...register("username")}
                                        type="text"
                                        id="username"
                                        autoComplete="username"
                                        className={`input pl-10 ${
                                            errors.username
                                                ? "border-error-500 focus-visible:ring-error-500"
                                                : ""
                                        }`}
                                        placeholder="Enter your username"
                                    />
                                </div>
                                {errors.username && (
                                    <p className="form-error">{errors.username.message}</p>
                                )}
                            </div>

                            {/* Password Field */}
                            <div className="form-group">
                                <label htmlFor="password" className="form-label">
                                    Password
                                </label>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <Lock className="h-5 w-5 text-neutral-400" />
                                    </div>
                                    <input
                                        {...register("password")}
                                        type={showPassword ? "text" : "password"}
                                        id="password"
                                        autoComplete="current-password"
                                        className={`input pl-10 pr-10 ${
                                            errors.password
                                                ? "border-error-500 focus-visible:ring-error-500"
                                                : ""
                                        }`}
                                        placeholder="Enter your password"
                                    />
                                    <button
                                        type="button"
                                        className="absolute inset-y-0 right-0 pr-3 flex items-center"
                                        onClick={togglePasswordVisibility}
                                    >
                                        {showPassword ? (
                                            <EyeOff className="h-5 w-5 text-neutral-400 hover:text-neutral-600" />
                                        ) : (
                                            <Eye className="h-5 w-5 text-neutral-400 hover:text-neutral-600" />
                                        )}
                                    </button>
                                </div>
                                {errors.password && (
                                    <p className="form-error">{errors.password.message}</p>
                                )}
                            </div>
                        </div>

                        {/* Submit Button */}
                        <div>
                            <button
                                type="submit"
                                disabled={isLoading}
                                className="btn-primary btn-lg w-full"
                            >
                                {isLoading ? (
                                    <>
                                        <div className="loading-spinner mr-2"></div>
                                        Signing in...
                                    </>
                                ) : (
                                    "Sign in"
                                )}
                            </button>
                        </div>
                    </form>

                    {/* Demo Credentials */}
                    <div className="mt-8 p-4 bg-neutral-50 dark:bg-neutral-800 rounded-lg">
                        <h3 className="text-sm font-medium text-neutral-900 dark:text-neutral-100 mb-3">
                            Demo Credentials
                        </h3>
                        <div className="space-y-2">
                            {demoCredentials.map((cred, index) => (
                                <div
                                    key={index}
                                    className="flex items-center justify-between p-2 bg-white dark:bg-neutral-700 rounded text-xs"
                                >
                                    <div>
                                        <div className="font-medium text-neutral-900 dark:text-neutral-100">
                                            {cred.role}
                                        </div>
                                        <div className="text-neutral-500 dark:text-neutral-400">
                                            {cred.description}
                                        </div>
                                    </div>
                                    <button
                                        type="button"
                                        onClick={() => fillDemoCredentials(cred.username, cred.password)}
                                        className="btn-outline btn-sm"
                                    >
                                        Use
                                    </button>
                                </div>
                            ))}
                        </div>
                    </div>

                    {/* Footer Links */}
                    <div className="text-center">
                        <p className="text-sm text-neutral-600 dark:text-neutral-400">
                            Don't have an account?{" "}
                            <button
                                type="button"
                                onClick={() => router.push("/register")}
                                className="font-medium text-primary-600 hover:text-primary-500 dark:text-primary-400 dark:hover:text-primary-300"
                            >
                                Register here
                            </button>
                        </p>
                    </div>
                </div>
            </div>

            {/* Right Panel - Branding */}
            <div className="hidden lg:block relative flex-1">
                <div className="absolute inset-0 bg-gradient-to-br from-primary-600 to-secondary-600">
                    <div className="absolute inset-0 bg-black/20"></div>
                </div>
                <div className="relative z-10 flex flex-col justify-center h-full px-12 text-white">
                    <div className="max-w-lg">
                        <Hospital className="h-16 w-16 mb-8" />
                        <h1 className="text-4xl font-bold mb-6">
                            Hospital Management System
                        </h1>
                        <p className="text-xl mb-8 text-primary-100">
                            Streamline your healthcare operations with our comprehensive
                            management platform.
                        </p>
                        <div className="space-y-4">
                            <div className="flex items-center">
                                <div className="w-2 h-2 bg-white rounded-full mr-3"></div>
                                <span>Patient Management</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-2 h-2 bg-white rounded-full mr-3"></div>
                                <span>Appointment Scheduling</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-2 h-2 bg-white rounded-full mr-3"></div>
                                <span>Medical Records</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-2 h-2 bg-white rounded-full mr-3"></div>
                                <span>Billing & Insurance</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Theme Toggle - Fixed Position */}
            <div className="fixed top-4 right-4 z-50">
                <ThemeToggleButton />
            </div>
        </div>
    );
}
