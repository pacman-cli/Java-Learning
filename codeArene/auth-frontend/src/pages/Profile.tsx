import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import {
    UserIcon,
    EnvelopeIcon,
    KeyIcon,
    CalendarIcon,
    ShieldCheckIcon,
    PencilIcon,
    CheckIcon,
    XMarkIcon,
} from "@heroicons/react/24/outline";
import { useAuth } from "../context/AuthContext";
import { User, PasswordChangeRequest } from "../types/auth";
import apiService from "../services/api";
import LoadingSpinner from "../components/LoadingSpinner";
import {
    formatDate,
    getRoleBadgeColor,
    formatUserRole,
    getStatusBadgeColor,
    getStatusText,
} from "../utils/formatters";

interface PasswordChangeForm {
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
}

const Profile: React.FC = () => {
    const { user: authUser } = useAuth();
    const [user, setUser] = useState<User | null>(authUser);
    const [isChangingPassword, setIsChangingPassword] = useState(false);
    const [showPasswordForm, setShowPasswordForm] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const {
        register,
        handleSubmit,
        watch,
        formState: { errors },
        reset,
    } = useForm<PasswordChangeForm>();

    const newPassword = watch("newPassword");

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                setLoading(true);
                const currentUser = await apiService.getCurrentUser();
                setUser(currentUser);
            } catch (err: any) {
                setError(
                    err.response?.data?.message || "Failed to load profile",
                );
            } finally {
                setLoading(false);
            }
        };

        if (authUser) {
            fetchUserProfile();
        }
    }, [authUser]);

    const onPasswordChange = async (data: PasswordChangeForm) => {
        if (!user) return;

        try {
            setIsChangingPassword(true);
            const passwordData: PasswordChangeRequest = {
                password: data.newPassword,
            };

            await apiService.changeUserPassword(user.id, passwordData);
            toast.success("Password changed successfully");
            setShowPasswordForm(false);
            reset();
        } catch (error: any) {
            toast.error(
                error.response?.data?.message || "Failed to change password",
            );
        } finally {
            setIsChangingPassword(false);
        }
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center h-64">
                <LoadingSpinner size="large" />
            </div>
        );
    }

    if (error || !user) {
        return (
            <div className="rounded-md bg-red-50 p-4">
                <div className="text-sm text-red-700">
                    {error || "User not found"}
                </div>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            <div className="md:flex md:items-center md:justify-between">
                <div className="flex-1 min-w-0">
                    <h1 className="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:truncate">
                        Profile
                    </h1>
                    <p className="mt-1 text-sm text-gray-500">
                        Manage your account information and security settings
                    </p>
                </div>
            </div>

            {/* Profile Information Card */}
            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <div className="px-4 py-5 sm:px-6 border-b border-gray-200">
                    <div className="flex items-center">
                        <div className="flex-shrink-0">
                            <div className="h-20 w-20 rounded-full bg-primary-500 flex items-center justify-center">
                                <span className="text-2xl font-medium text-white">
                                    {user.firstName.charAt(0)}
                                    {user.lastName.charAt(0)}
                                </span>
                            </div>
                        </div>
                        <div className="ml-6">
                            <h3 className="text-lg leading-6 font-medium text-gray-900">
                                {user.firstName} {user.lastName}
                            </h3>
                            <p className="mt-1 max-w-2xl text-sm text-gray-500">
                                @{user.username}
                            </p>
                            <div className="mt-2 flex space-x-2">
                                <span
                                    className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getRoleBadgeColor(user.role)}`}
                                >
                                    {formatUserRole(user.role)}
                                </span>
                                <span
                                    className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusBadgeColor(user.isEnabled, !user.isAccountNonLocked)}`}
                                >
                                    {getStatusText(
                                        user.isEnabled,
                                        !user.isAccountNonLocked,
                                    )}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="px-4 py-5 sm:p-6">
                    <dl className="grid grid-cols-1 gap-x-4 gap-y-6 sm:grid-cols-2">
                        <div>
                            <dt className="text-sm font-medium text-gray-500 flex items-center">
                                <UserIcon className="h-4 w-4 mr-1" />
                                Username
                            </dt>
                            <dd className="mt-1 text-sm text-gray-900">
                                {user.username}
                            </dd>
                        </div>

                        <div>
                            <dt className="text-sm font-medium text-gray-500 flex items-center">
                                <EnvelopeIcon className="h-4 w-4 mr-1" />
                                Email address
                            </dt>
                            <dd className="mt-1 text-sm text-gray-900">
                                {user.email}
                            </dd>
                        </div>

                        <div>
                            <dt className="text-sm font-medium text-gray-500">
                                First name
                            </dt>
                            <dd className="mt-1 text-sm text-gray-900">
                                {user.firstName}
                            </dd>
                        </div>

                        <div>
                            <dt className="text-sm font-medium text-gray-500">
                                Last name
                            </dt>
                            <dd className="mt-1 text-sm text-gray-900">
                                {user.lastName}
                            </dd>
                        </div>

                        <div>
                            <dt className="text-sm font-medium text-gray-500 flex items-center">
                                <ShieldCheckIcon className="h-4 w-4 mr-1" />
                                Role
                            </dt>
                            <dd className="mt-1 text-sm text-gray-900">
                                {formatUserRole(user.role)}
                            </dd>
                        </div>

                        <div>
                            <dt className="text-sm font-medium text-gray-500 flex items-center">
                                <CalendarIcon className="h-4 w-4 mr-1" />
                                Member since
                            </dt>
                            <dd className="mt-1 text-sm text-gray-900">
                                {formatDate(user.createdAt)}
                            </dd>
                        </div>

                        <div>
                            <dt className="text-sm font-medium text-gray-500">
                                Last updated
                            </dt>
                            <dd className="mt-1 text-sm text-gray-900">
                                {formatDate(user.updatedAt)}
                            </dd>
                        </div>

                        <div>
                            <dt className="text-sm font-medium text-gray-500">
                                Last login
                            </dt>
                            <dd className="mt-1 text-sm text-gray-900">
                                {user.lastLogin
                                    ? formatDate(user.lastLogin)
                                    : "Never"}
                            </dd>
                        </div>
                    </dl>
                </div>
            </div>

            {/* Account Status */}
            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <div className="px-4 py-5 sm:px-6">
                    <h3 className="text-lg leading-6 font-medium text-gray-900">
                        Account Status
                    </h3>
                    <p className="mt-1 max-w-2xl text-sm text-gray-500">
                        Current status of your account and permissions
                    </p>
                </div>
                <div className="border-t border-gray-200 px-4 py-5 sm:p-6">
                    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                        <div className="flex items-center">
                            <div
                                className={`flex-shrink-0 h-3 w-3 rounded-full ${user.isEnabled ? "bg-green-400" : "bg-red-400"}`}
                            />
                            <span className="ml-3 text-sm font-medium text-gray-900">
                                Account{" "}
                                {user.isEnabled ? "Enabled" : "Disabled"}
                            </span>
                        </div>
                        <div className="flex items-center">
                            <div
                                className={`flex-shrink-0 h-3 w-3 rounded-full ${user.isAccountNonLocked ? "bg-green-400" : "bg-red-400"}`}
                            />
                            <span className="ml-3 text-sm font-medium text-gray-900">
                                Account{" "}
                                {user.isAccountNonLocked
                                    ? "Not Locked"
                                    : "Locked"}
                            </span>
                        </div>
                        <div className="flex items-center">
                            <div
                                className={`flex-shrink-0 h-3 w-3 rounded-full ${user.isAccountNonExpired ? "bg-green-400" : "bg-red-400"}`}
                            />
                            <span className="ml-3 text-sm font-medium text-gray-900">
                                Account{" "}
                                {user.isAccountNonExpired
                                    ? "Not Expired"
                                    : "Expired"}
                            </span>
                        </div>
                        <div className="flex items-center">
                            <div
                                className={`flex-shrink-0 h-3 w-3 rounded-full ${user.isCredentialsNonExpired ? "bg-green-400" : "bg-red-400"}`}
                            />
                            <span className="ml-3 text-sm font-medium text-gray-900">
                                Credentials{" "}
                                {user.isCredentialsNonExpired
                                    ? "Valid"
                                    : "Expired"}
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            {/* Security Settings */}
            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <div className="px-4 py-5 sm:px-6">
                    <h3 className="text-lg leading-6 font-medium text-gray-900 flex items-center">
                        <KeyIcon className="h-5 w-5 mr-2" />
                        Security Settings
                    </h3>
                    <p className="mt-1 max-w-2xl text-sm text-gray-500">
                        Manage your password and security preferences
                    </p>
                </div>
                <div className="border-t border-gray-200 px-4 py-5 sm:p-6">
                    {!showPasswordForm ? (
                        <div className="flex items-center justify-between">
                            <div>
                                <h4 className="text-sm font-medium text-gray-900">
                                    Password
                                </h4>
                                <p className="text-sm text-gray-500">
                                    Last changed: {formatDate(user.updatedAt)}
                                </p>
                            </div>
                            <button
                                onClick={() => setShowPasswordForm(true)}
                                className="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
                            >
                                <PencilIcon className="h-4 w-4 mr-1" />
                                Change Password
                            </button>
                        </div>
                    ) : (
                        <form
                            onSubmit={handleSubmit(onPasswordChange)}
                            className="space-y-4"
                        >
                            <div>
                                <label
                                    htmlFor="currentPassword"
                                    className="block text-sm font-medium text-gray-700"
                                >
                                    Current Password
                                </label>
                                <input
                                    {...register("currentPassword", {
                                        required:
                                            "Current password is required",
                                    })}
                                    type="password"
                                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
                                />
                                {errors.currentPassword && (
                                    <p className="mt-1 text-sm text-red-600">
                                        {String(errors.currentPassword.message)}
                                    </p>
                                )}
                            </div>

                            <div>
                                <label
                                    htmlFor="newPassword"
                                    className="block text-sm font-medium text-gray-700"
                                >
                                    New Password
                                </label>
                                <input
                                    {...register("newPassword", {
                                        required: "New password is required",
                                        minLength: {
                                            value: 8,
                                            message:
                                                "Password must be at least 8 characters",
                                        },
                                        pattern: {
                                            value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/,
                                            message:
                                                "Password must contain uppercase, lowercase, number, and special character",
                                        },
                                    })}
                                    type="password"
                                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
                                />
                                {errors.newPassword && (
                                    <p className="mt-1 text-sm text-red-600">
                                        {String(errors.newPassword.message)}
                                    </p>
                                )}
                            </div>

                            <div>
                                <label
                                    htmlFor="confirmPassword"
                                    className="block text-sm font-medium text-gray-700"
                                >
                                    Confirm New Password
                                </label>
                                <input
                                    {...register("confirmPassword", {
                                        required:
                                            "Please confirm your password",
                                        validate: (value) =>
                                            value === newPassword ||
                                            "Passwords do not match",
                                    })}
                                    type="password"
                                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
                                />
                                {errors.confirmPassword && (
                                    <p className="mt-1 text-sm text-red-600">
                                        {String(errors.confirmPassword.message)}
                                    </p>
                                )}
                            </div>

                            <div className="flex justify-end space-x-3">
                                <button
                                    type="button"
                                    onClick={() => {
                                        setShowPasswordForm(false);
                                        reset();
                                    }}
                                    className="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
                                >
                                    <XMarkIcon className="h-4 w-4 mr-1" />
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    disabled={isChangingPassword}
                                    className="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50"
                                >
                                    {isChangingPassword ? (
                                        <LoadingSpinner
                                            size="small"
                                            color="white"
                                        />
                                    ) : (
                                        <>
                                            <CheckIcon className="h-4 w-4 mr-1" />
                                            Update Password
                                        </>
                                    )}
                                </button>
                            </div>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Profile;
