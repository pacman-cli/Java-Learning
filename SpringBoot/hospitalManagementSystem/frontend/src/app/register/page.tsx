"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Eye, EyeOff, Hospital, Lock, User, Mail, UserPlus } from "lucide-react";
import { useAuth } from "@/providers/AuthProvider";
import { ThemeToggleButton } from "@/providers/ThemeProvider";
import toast from "react-hot-toast";

const registerSchema = z.object({
    username: z
        .string()
        .min(1, "Username is required")
        .min(3, "Username must be at least 3 characters")
        .max(50, "Username cannot exceed 50 characters")
        .regex(/^[a-zA-Z0-9_]+$/, "Username can only contain letters, numbers, and underscores"),
    fullName: z
        .string()
        .min(1, "Full name is required")
        .min(2, "Full name must be at least 2 characters")
        .max(100, "Full name cannot exceed 100 characters"),
    email: z
        .string()
        .min(1, "Email is required")
        .email("Please enter a valid email address"),
    password: z
        .string()
        .min(1, "Password is required")
        .min(8, "Password must be at least 8 characters")
        .regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/, "Password must contain at least one uppercase letter, one lowercase letter, and one number"),
    confirmPassword: z
        .string()
        .min(1, "Please confirm your password"),
    role: z
        .string()
        .min(1, "Please select a role"),
    terms: z
        .boolean()
        .refine(val => val === true, "You must accept the terms and conditions"),
}).refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ["confirmPassword"],
});

type RegisterFormData = z.infer<typeof registerSchema>;

const userRoles = [
    { value: "ROLE_PATIENT", label: "Patient", description: "Access to personal medical records and appointments" },
    { value: "ROLE_DOCTOR", label: "Doctor", description: "Access to patient records and medical management" },
    { value: "ROLE_NURSE", label: "Nurse", description: "Access to patient care and medical records" },
    { value: "ROLE_RECEPTIONIST", label: "Receptionist", description: "Manage appointments and patient registration" },
    { value: "ROLE_PHARMACIST", label: "Pharmacist", description: "Manage prescriptions and pharmacy operations" },
    { value: "ROLE_LAB_TECHNICIAN", label: "Lab Technician", description: "Handle laboratory tests and results" },
    { value: "ROLE_BILLING_STAFF", label: "Billing Staff", description: "Manage billing and payment processing" },
];

export default function RegisterPage() {
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const { register: registerUser, isAuthenticated } = useAuth();
    const router = useRouter();

    const {
        register,
        handleSubmit,
        formState: { errors },
        setFocus,
        watch,
    } = useForm<RegisterFormData>({
        resolver: zodResolver(registerSchema),
        defaultValues: {
            username: "",
            fullName: "",
            email: "",
            password: "",
            confirmPassword: "",
            role: "",
            terms: false,
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

    const onSubmit = async (data: RegisterFormData) => {
        setIsLoading(true);
        try {
            await registerUser({
                username: data.username,
                fullName: data.fullName,
                email: data.email,
                password: data.password,
                roles: [data.role],
            });
        } catch (error) {
            // Error is already handled in the register function
            console.error("Registration error:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const toggleConfirmPasswordVisibility = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const selectedRole = watch("role");

    return (
        <div className="min-h-screen flex">
            {/* Left Panel - Registration Form */}
            <div className="flex-1 flex items-center justify-center px-4 sm:px-6 lg:px-8 bg-white dark:bg-neutral-900">
                <div className="max-w-md w-full space-y-8">
                    {/* Header */}
                    <div className="text-center">
                        <div className="mx-auto h-16 w-16 flex items-center justify-center rounded-full bg-primary-100 dark:bg-primary-900">
                            <Hospital className="h-8 w-8 text-primary-600 dark:text-primary-400" />
                        </div>
                        <h2 className="mt-6 text-3xl font-bold text-neutral-900 dark:text-neutral-100">
                            Create your account
                        </h2>
                        <p className="mt-2 text-sm text-neutral-600 dark:text-neutral-400">
                            Join our hospital management system
                        </p>
                    </div>

                    {/* Registration Form */}
                    <form className="mt-8 space-y-6" onSubmit={handleSubmit(onSubmit)}>
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
                                        placeholder="Choose a username"
                                    />
                                </div>
                                {errors.username && (
                                    <p className="form-error">{errors.username.message}</p>
                                )}
                            </div>

                            {/* Full Name Field */}
                            <div className="form-group">
                                <label htmlFor="fullName" className="form-label">
                                    Full Name
                                </label>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <UserPlus className="h-5 w-5 text-neutral-400" />
                                    </div>
                                    <input
                                        {...register("fullName")}
                                        type="text"
                                        id="fullName"
                                        autoComplete="name"
                                        className={`input pl-10 ${
                                            errors.fullName
                                                ? "border-error-500 focus-visible:ring-error-500"
                                                : ""
                                        }`}
                                        placeholder="Enter your full name"
                                    />
                                </div>
                                {errors.fullName && (
                                    <p className="form-error">{errors.fullName.message}</p>
                                )}
                            </div>

                            {/* Email Field */}
                            <div className="form-group">
                                <label htmlFor="email" className="form-label">
                                    Email Address
                                </label>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <Mail className="h-5 w-5 text-neutral-400" />
                                    </div>
                                    <input
                                        {...register("email")}
                                        type="email"
                                        id="email"
                                        autoComplete="email"
                                        className={`input pl-10 ${
                                            errors.email
                                                ? "border-error-500 focus-visible:ring-error-500"
                                                : ""
                                        }`}
                                        placeholder="Enter your email address"
                                    />
                                </div>
                                {errors.email && (
                                    <p className="form-error">{errors.email.message}</p>
                                )}
                            </div>

                            {/* Role Selection */}
                            <div className="form-group">
                                <label htmlFor="role" className="form-label">
                                    Role
                                </label>
                                <select
                                    {...register("role")}
                                    id="role"
                                    className={`input ${
                                        errors.role
                                            ? "border-error-500 focus-visible:ring-error-500"
                                            : ""
                                    }`}
                                >
                                    <option value="">Select your role</option>
                                    {userRoles.map((role) => (
                                        <option key={role.value} value={role.value}>
                                            {role.label}
                                        </option>
                                    ))}
                                </select>
                                {selectedRole && (
                                    <p className="mt-1 text-xs text-neutral-600 dark:text-neutral-400">
                                        {userRoles.find(r => r.value === selectedRole)?.description}
                                    </p>
                                )}
                                {errors.role && (
                                    <p className="form-error">{errors.role.message}</p>
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
                                        autoComplete="new-password"
                                        className={`input pl-10 pr-10 ${
                                            errors.password
                                                ? "border-error-500 focus-visible:ring-error-500"
                                                : ""
                                        }`}
                                        placeholder="Create a strong password"
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

                            {/* Confirm Password Field */}
                            <div className="form-group">
                                <label htmlFor="confirmPassword" className="form-label">
                                    Confirm Password
                                </label>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <Lock className="h-5 w-5 text-neutral-400" />
                                    </div>
                                    <input
                                        {...register("confirmPassword")}
                                        type={showConfirmPassword ? "text" : "password"}
                                        id="confirmPassword"
                                        autoComplete="new-password"
                                        className={`input pl-10 pr-10 ${
                                            errors.confirmPassword
                                                ? "border-error-500 focus-visible:ring-error-500"
                                                : ""
                                        }`}
                                        placeholder="Confirm your password"
                                    />
                                    <button
                                        type="button"
                                        className="absolute inset-y-0 right-0 pr-3 flex items-center"
                                        onClick={toggleConfirmPasswordVisibility}
                                    >
                                        {showConfirmPassword ? (
                                            <EyeOff className="h-5 w-5 text-neutral-400 hover:text-neutral-600" />
                                        ) : (
                                            <Eye className="h-5 w-5 text-neutral-400 hover:text-neutral-600" />
                                        )}
                                    </button>
                                </div>
                                {errors.confirmPassword && (
                                    <p className="form-error">{errors.confirmPassword.message}</p>
                                )}
                            </div>

                            {/* Terms and Conditions */}
                            <div className="form-group">
                                <div className="flex items-start">
                                    <input
                                        {...register("terms")}
                                        type="checkbox"
                                        id="terms"
                                        className="mt-1 h-4 w-4 text-primary-600 focus:ring-primary-500 border-neutral-300 rounded"
                                    />
                                    <label htmlFor="terms" className="ml-3 text-sm text-neutral-600 dark:text-neutral-400">
                                        I agree to the{" "}
                                        <a href="/terms" className="text-primary-600 hover:text-primary-500 dark:text-primary-400 dark:hover:text-primary-300">
                                            Terms and Conditions
                                        </a>{" "}
                                        and{" "}
                                        <a href="/privacy" className="text-primary-600 hover:text-primary-500 dark:text-primary-400 dark:hover:text-primary-300">
                                            Privacy Policy
                                        </a>
                                    </label>
                                </div>
                                {errors.terms && (
                                    <p className="form-error mt-1">{errors.terms.message}</p>
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
                                        Creating account...
                                    </>
                                ) : (
                                    "Create Account"
                                )}
                            </button>
                        </div>
                    </form>

                    {/* Footer Links */}
                    <div className="text-center">
                        <p className="text-sm text-neutral-600 dark:text-neutral-400">
                            Already have an account?{" "}
                            <button
                                type="button"
                                onClick={() => router.push("/login")}
                                className="font-medium text-primary-600 hover:text-primary-500 dark:text-primary-400 dark:hover:text-primary-300"
                            >
                                Sign in here
                            </button>
                        </p>
                    </div>
                </div>
            </div>

            {/* Right Panel - Branding */}
            <div className="hidden lg:block relative flex-1">
                <div className="absolute inset-0 bg-gradient-to-br from-secondary-600 to-accent-600">
                    <div className="absolute inset-0 bg-black/20"></div>
                </div>
                <div className="relative z-10 flex flex-col justify-center h-full px-12 text-white">
                    <div className="max-w-lg">
                        <Hospital className="h-16 w-16 mb-8" />
                        <h1 className="text-4xl font-bold mb-6">
                            Join Our Healthcare Team
                        </h1>
                        <p className="text-xl mb-8 text-secondary-100">
                            Become part of our comprehensive hospital management platform and help us deliver better healthcare.
                        </p>
                        <div className="space-y-4">
                            <div className="flex items-center">
                                <div className="w-2 h-2 bg-white rounded-full mr-3"></div>
                                <span>Secure and HIPAA-compliant platform</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-2 h-2 bg-white rounded-full mr-3"></div>
                                <span>Role-based access control</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-2 h-2 bg-white rounded-full mr-3"></div>
                                <span>Comprehensive patient management</span>
                            </div>
                            <div className="flex items-center">
                                <div className="w-2 h-2 bg-white rounded-full mr-3"></div>
                                <span>24/7 technical support</span>
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
