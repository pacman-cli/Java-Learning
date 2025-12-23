import React, { useState, useEffect } from "react";
import {
    UsersIcon,
    ShieldCheckIcon,
    ChartBarIcon,
    ExclamationTriangleIcon,
    CheckCircleIcon,
    ClockIcon,
    CogIcon,
} from "@heroicons/react/24/outline";
import { useAuth } from "../context/AuthContext";
import { User, UserStats } from "../types/auth";
import apiService from "../services/api";
import LoadingSpinner from "../components/LoadingSpinner";
import {
    formatRelativeTime,
    getRoleBadgeColor,
    formatUserRole,
} from "../utils/formatters";
import { Link } from "react-router-dom";

const Admin: React.FC = () => {
    const { isAdmin } = useAuth();
    const [stats, setStats] = useState<UserStats | null>(null);
    const [recentUsers, setRecentUsers] = useState<User[]>([]);
    const [activeUsers, setActiveUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [refreshing, setRefreshing] = useState(false);

    const fetchAdminData = async () => {
        try {
            setLoading(true);
            setError(null);

            const [statsResponse, recentResponse, activeResponse] =
                await Promise.allSettled([
                    apiService.getUserStats(),
                    apiService.getRecentUsers(5),
                    apiService.getActiveUsers(),
                ]);

            if (statsResponse.status === "fulfilled") {
                setStats(statsResponse.value);
            }

            if (recentResponse.status === "fulfilled") {
                setRecentUsers(recentResponse.value);
            }

            if (activeResponse.status === "fulfilled") {
                setActiveUsers(activeResponse.value.slice(0, 10)); // Limit to 10 for display
            }
        } catch (err: any) {
            setError(
                err.response?.data?.message || "Failed to load admin data",
            );
        } finally {
            setLoading(false);
        }
    };

    const handleRefresh = async () => {
        setRefreshing(true);
        await fetchAdminData();
        setRefreshing(false);
    };

    useEffect(() => {
        if (isAdmin) {
            fetchAdminData();
        }
    }, [isAdmin]);

    if (!isAdmin) {
        return (
            <div className="text-center py-12">
                <ExclamationTriangleIcon className="mx-auto h-12 w-12 text-red-400" />
                <h3 className="mt-2 text-sm font-medium text-gray-900">
                    Access Denied
                </h3>
                <p className="mt-1 text-sm text-gray-500">
                    You do not have administrator privileges to access this
                    page.
                </p>
            </div>
        );
    }

    if (loading) {
        return (
            <div className="flex items-center justify-center h-64">
                <LoadingSpinner size="large" />
            </div>
        );
    }

    const adminActions = [
        {
            name: "Manage Users",
            description: "View, edit, and manage user accounts",
            href: "/users",
            icon: UsersIcon,
            color: "bg-blue-500",
        },
        {
            name: "System Settings",
            description: "Configure system-wide settings",
            href: "/settings",
            icon: CogIcon,
            color: "bg-purple-500",
        },
        {
            name: "Security Audit",
            description: "Review security logs and audit trail",
            href: "#",
            icon: ShieldCheckIcon,
            color: "bg-red-500",
            disabled: true,
        },
        {
            name: "Analytics",
            description: "View detailed system analytics",
            href: "#",
            icon: ChartBarIcon,
            color: "bg-green-500",
            disabled: true,
        },
    ];

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="md:flex md:items-center md:justify-between">
                <div className="flex-1 min-w-0">
                    <h1 className="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:truncate">
                        Admin Panel
                    </h1>
                    <p className="mt-1 text-sm text-gray-500">
                        System administration and management tools
                    </p>
                </div>
                <div className="mt-4 flex md:mt-0 md:ml-4">
                    <button
                        onClick={handleRefresh}
                        disabled={refreshing}
                        className="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50"
                    >
                        {refreshing ? (
                            <LoadingSpinner size="small" />
                        ) : (
                            "Refresh Data"
                        )}
                    </button>
                </div>
            </div>

            {error && (
                <div className="rounded-md bg-red-50 p-4">
                    <div className="text-sm text-red-700">{error}</div>
                </div>
            )}

            {/* System Stats */}
            {stats && (
                <div>
                    <h2 className="text-lg font-medium text-gray-900 mb-4">
                        System Overview
                    </h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                        <div className="bg-white overflow-hidden shadow rounded-lg">
                            <div className="p-5">
                                <div className="flex items-center">
                                    <div className="flex-shrink-0">
                                        <UsersIcon className="h-6 w-6 text-gray-400" />
                                    </div>
                                    <div className="ml-5 w-0 flex-1">
                                        <dl>
                                            <dt className="text-sm font-medium text-gray-500 truncate">
                                                Total Users
                                            </dt>
                                            <dd className="flex items-baseline">
                                                <div className="text-2xl font-semibold text-gray-900">
                                                    {stats.totalUsers}
                                                </div>
                                            </dd>
                                        </dl>
                                    </div>
                                </div>
                            </div>
                            <div className="bg-gray-50 px-5 py-3">
                                <div className="text-sm">
                                    <Link
                                        to="/users"
                                        className="font-medium text-primary-700 hover:text-primary-900"
                                    >
                                        View all users
                                    </Link>
                                </div>
                            </div>
                        </div>

                        <div className="bg-white overflow-hidden shadow rounded-lg">
                            <div className="p-5">
                                <div className="flex items-center">
                                    <div className="flex-shrink-0">
                                        <ShieldCheckIcon className="h-6 w-6 text-red-400" />
                                    </div>
                                    <div className="ml-5 w-0 flex-1">
                                        <dl>
                                            <dt className="text-sm font-medium text-gray-500 truncate">
                                                Administrators
                                            </dt>
                                            <dd className="flex items-baseline">
                                                <div className="text-2xl font-semibold text-gray-900">
                                                    {stats.adminCount}
                                                </div>
                                            </dd>
                                        </dl>
                                    </div>
                                </div>
                            </div>
                            <div className="bg-gray-50 px-5 py-3">
                                <div className="text-sm">
                                    <span className="text-gray-500">
                                        System admins
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div className="bg-white overflow-hidden shadow rounded-lg">
                            <div className="p-5">
                                <div className="flex items-center">
                                    <div className="flex-shrink-0">
                                        <UsersIcon className="h-6 w-6 text-yellow-400" />
                                    </div>
                                    <div className="ml-5 w-0 flex-1">
                                        <dl>
                                            <dt className="text-sm font-medium text-gray-500 truncate">
                                                Moderators
                                            </dt>
                                            <dd className="flex items-baseline">
                                                <div className="text-2xl font-semibold text-gray-900">
                                                    {stats.moderatorCount}
                                                </div>
                                            </dd>
                                        </dl>
                                    </div>
                                </div>
                            </div>
                            <div className="bg-gray-50 px-5 py-3">
                                <div className="text-sm">
                                    <span className="text-gray-500">
                                        Content moderators
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div className="bg-white overflow-hidden shadow rounded-lg">
                            <div className="p-5">
                                <div className="flex items-center">
                                    <div className="flex-shrink-0">
                                        <CheckCircleIcon className="h-6 w-6 text-green-400" />
                                    </div>
                                    <div className="ml-5 w-0 flex-1">
                                        <dl>
                                            <dt className="text-sm font-medium text-gray-500 truncate">
                                                Active Users
                                            </dt>
                                            <dd className="flex items-baseline">
                                                <div className="text-2xl font-semibold text-gray-900">
                                                    {activeUsers.length}
                                                </div>
                                            </dd>
                                        </dl>
                                    </div>
                                </div>
                            </div>
                            <div className="bg-gray-50 px-5 py-3">
                                <div className="text-sm">
                                    <span className="text-gray-500">
                                        Currently enabled
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* Admin Actions */}
            <div>
                <h2 className="text-lg font-medium text-gray-900 mb-4">
                    Admin Actions
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {adminActions.map((action) => (
                        <div key={action.name}>
                            {action.disabled ? (
                                <div className="relative group bg-gray-100 p-6 rounded-lg border-2 border-dashed border-gray-300 cursor-not-allowed">
                                    <div>
                                        <span
                                            className={`rounded-lg inline-flex p-3 ${action.color} bg-opacity-20`}
                                        >
                                            <action.icon className="h-6 w-6 text-gray-400" />
                                        </span>
                                    </div>
                                    <div className="mt-4">
                                        <h3 className="text-lg font-medium text-gray-400">
                                            {action.name}
                                        </h3>
                                        <p className="mt-2 text-sm text-gray-400">
                                            {action.description}
                                        </p>
                                        <p className="mt-2 text-xs text-gray-500">
                                            Coming soon
                                        </p>
                                    </div>
                                </div>
                            ) : (
                                <Link
                                    to={action.href}
                                    className="relative group bg-white p-6 focus-within:ring-2 focus-within:ring-inset focus-within:ring-primary-500 rounded-lg shadow hover:shadow-md transition-shadow"
                                >
                                    <div>
                                        <span
                                            className={`rounded-lg inline-flex p-3 ${action.color} text-white`}
                                        >
                                            <action.icon className="h-6 w-6" />
                                        </span>
                                    </div>
                                    <div className="mt-4">
                                        <h3 className="text-lg font-medium text-gray-900 group-hover:text-primary-600">
                                            {action.name}
                                        </h3>
                                        <p className="mt-2 text-sm text-gray-500">
                                            {action.description}
                                        </p>
                                    </div>
                                </Link>
                            )}
                        </div>
                    ))}
                </div>
            </div>

            {/* Recent Users */}
            {recentUsers.length > 0 && (
                <div>
                    <div className="flex items-center justify-between mb-4">
                        <h2 className="text-lg font-medium text-gray-900">
                            Recent User Registrations
                        </h2>
                        <Link
                            to="/users"
                            className="text-sm text-primary-600 hover:text-primary-500"
                        >
                            View all
                        </Link>
                    </div>
                    <div className="bg-white shadow overflow-hidden sm:rounded-md">
                        <ul className="divide-y divide-gray-200">
                            {recentUsers.map((recentUser) => (
                                <li key={recentUser.id}>
                                    <div className="px-4 py-4 sm:px-6">
                                        <div className="flex items-center justify-between">
                                            <div className="flex items-center">
                                                <div className="flex-shrink-0 h-10 w-10">
                                                    <div className="h-10 w-10 rounded-full bg-gray-300 flex items-center justify-center">
                                                        <span className="text-sm font-medium text-gray-700">
                                                            {recentUser.firstName.charAt(
                                                                0,
                                                            )}
                                                            {recentUser.lastName.charAt(
                                                                0,
                                                            )}
                                                        </span>
                                                    </div>
                                                </div>
                                                <div className="ml-4">
                                                    <div className="flex items-center">
                                                        <p className="text-sm font-medium text-gray-900">
                                                            {
                                                                recentUser.firstName
                                                            }{" "}
                                                            {
                                                                recentUser.lastName
                                                            }
                                                        </p>
                                                        <span
                                                            className={`ml-2 inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getRoleBadgeColor(recentUser.role)}`}
                                                        >
                                                            {formatUserRole(
                                                                recentUser.role,
                                                            )}
                                                        </span>
                                                    </div>
                                                    <p className="text-sm text-gray-500">
                                                        @{recentUser.username} •{" "}
                                                        {recentUser.email}
                                                    </p>
                                                </div>
                                            </div>
                                            <div className="flex items-center text-sm text-gray-500">
                                                <ClockIcon className="flex-shrink-0 mr-1.5 h-4 w-4" />
                                                {formatRelativeTime(
                                                    recentUser.createdAt,
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            )}

            {/* System Status */}
            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <div className="px-4 py-5 sm:px-6">
                    <h3 className="text-lg leading-6 font-medium text-gray-900">
                        System Status
                    </h3>
                    <p className="mt-1 max-w-2xl text-sm text-gray-500">
                        Current system health and status indicators
                    </p>
                </div>
                <div className="border-t border-gray-200 px-4 py-5 sm:p-6">
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                        <div className="flex items-center">
                            <div className="flex-shrink-0 h-3 w-3 rounded-full bg-green-400" />
                            <span className="ml-3 text-sm text-gray-900">
                                Auth Service: Online
                            </span>
                        </div>
                        <div className="flex items-center">
                            <div className="flex-shrink-0 h-3 w-3 rounded-full bg-green-400" />
                            <span className="ml-3 text-sm text-gray-900">
                                Database: Connected
                            </span>
                        </div>
                        <div className="flex items-center">
                            <div className="flex-shrink-0 h-3 w-3 rounded-full bg-green-400" />
                            <span className="ml-3 text-sm text-gray-900">
                                API: Operational
                            </span>
                        </div>
                        <div className="flex items-center">
                            <div className="flex-shrink-0 h-3 w-3 rounded-full bg-yellow-400" />
                            <span className="ml-3 text-sm text-gray-900">
                                Monitoring: Partial
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Admin;
