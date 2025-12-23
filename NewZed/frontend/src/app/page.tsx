"use client";

import { useEffect, useState } from "react";
import { getUserStats } from "@/lib/api";
import { UserStats } from "@/lib/types";
import { formatNumber } from "@/lib/utils";
import { handleApiError } from "@/lib/api";
import toast from "react-hot-toast";
import Link from "next/link";
import {
    Users,
    UserCheck,
    UserX,
    Clock,
    Shield,
    UserCog,
    TrendingUp,
    Activity,
    Plus,
    Search,
    BarChart3,
} from "lucide-react";

export default function HomePage() {
    const [stats, setStats] = useState<UserStats | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string>("");

    useEffect(() => {
        loadStats();
    }, []);

    const loadStats = async () => {
        try {
            setLoading(true);
            setError("");
            const response = await getUserStats();
            setStats(response.data);
        } catch (err) {
            const errorMessage = handleApiError(err);
            setError(errorMessage);
            toast.error("Failed to load statistics");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
            {/* Header */}
            <header className="bg-white shadow-sm border-b border-gray-200">
                <div className="container-custom">
                    <div className="flex items-center justify-between h-16">
                        <div className="flex items-center gap-3">
                            <div className="w-10 h-10 bg-gradient-to-br from-blue-600 to-purple-600 rounded-lg flex items-center justify-center">
                                <Users className="w-6 h-6 text-white" />
                            </div>
                            <div>
                                <h1 className="text-xl font-bold text-gray-900">
                                    User Management System
                                </h1>
                                <p className="text-xs text-gray-500">
                                    Powered by Spring Boot & Next.js
                                </p>
                            </div>
                        </div>
                        <nav className="flex gap-3">
                            <Link href="/users" className="btn btn-primary">
                                <Users className="w-4 h-4" />
                                View All Users
                            </Link>
                        </nav>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="container-custom py-12">
                {/* Welcome Section */}
                <div className="mb-12 text-center">
                    <h2 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
                        Welcome to User Management
                    </h2>
                    <p className="text-lg text-gray-600 max-w-2xl mx-auto">
                        Efficiently manage users, roles, and permissions with
                        our comprehensive system built with modern technologies.
                    </p>
                </div>

                {/* Quick Actions */}
                <div className="mb-12">
                    <h3 className="text-2xl font-semibold text-gray-900 mb-6">
                        Quick Actions
                    </h3>
                    <div className="grid md:grid-cols-3 gap-4">
                        <Link
                            href="/users/create"
                            className="card-hover flex items-center gap-4 p-6 group"
                        >
                            <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center group-hover:bg-blue-600 transition-colors">
                                <Plus className="w-6 h-6 text-blue-600 group-hover:text-white transition-colors" />
                            </div>
                            <div>
                                <h4 className="font-semibold text-gray-900">
                                    Create User
                                </h4>
                                <p className="text-sm text-gray-600">
                                    Add a new user to the system
                                </p>
                            </div>
                        </Link>

                        <Link
                            href="/users?status=ACTIVE"
                            className="card-hover flex items-center gap-4 p-6 group"
                        >
                            <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center group-hover:bg-green-600 transition-colors">
                                <Search className="w-6 h-6 text-green-600 group-hover:text-white transition-colors" />
                            </div>
                            <div>
                                <h4 className="font-semibold text-gray-900">
                                    Browse Users
                                </h4>
                                <p className="text-sm text-gray-600">
                                    Search and filter all users
                                </p>
                            </div>
                        </Link>

                        <Link
                            href="/users/stats"
                            className="card-hover flex items-center gap-4 p-6 group"
                        >
                            <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center group-hover:bg-purple-600 transition-colors">
                                <BarChart3 className="w-6 h-6 text-purple-600 group-hover:text-white transition-colors" />
                            </div>
                            <div>
                                <h4 className="font-semibold text-gray-900">
                                    View Analytics
                                </h4>
                                <p className="text-sm text-gray-600">
                                    Detailed user statistics
                                </p>
                            </div>
                        </Link>
                    </div>
                </div>

                {/* Statistics Section */}
                <div>
                    <div className="flex items-center justify-between mb-6">
                        <h3 className="text-2xl font-semibold text-gray-900">
                            System Statistics
                        </h3>
                        {!loading && stats && (
                            <button
                                onClick={loadStats}
                                className="btn btn-sm btn-secondary"
                            >
                                <Activity className="w-4 h-4" />
                                Refresh
                            </button>
                        )}
                    </div>

                    {/* Loading State */}
                    {loading && (
                        <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
                            {[...Array(8)].map((_, i) => (
                                <div key={i} className="card">
                                    <div className="animate-pulse">
                                        <div className="w-12 h-12 bg-gray-200 rounded-lg mb-4"></div>
                                        <div className="h-4 bg-gray-200 rounded w-1/2 mb-2"></div>
                                        <div className="h-8 bg-gray-200 rounded w-3/4"></div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}

                    {/* Error State */}
                    {error && !loading && (
                        <div className="alert alert-danger flex items-center justify-between">
                            <div>
                                <p className="font-semibold">
                                    Failed to load statistics
                                </p>
                                <p className="text-sm">{error}</p>
                            </div>
                            <button
                                onClick={loadStats}
                                className="btn btn-sm btn-danger"
                            >
                                Retry
                            </button>
                        </div>
                    )}

                    {/* Statistics Grid */}
                    {stats && !loading && !error && (
                        <>
                            {/* User Status Statistics */}
                            <div className="mb-8">
                                <h4 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4">
                                    User Status
                                </h4>
                                <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
                                    {/* Total Users */}
                                    <div className="card-hover bg-gradient-to-br from-blue-50 to-blue-100 border-2 border-blue-200">
                                        <div className="flex items-center justify-between mb-4">
                                            <div className="w-12 h-12 bg-blue-600 rounded-lg flex items-center justify-center">
                                                <Users className="w-6 h-6 text-white" />
                                            </div>
                                            <TrendingUp className="w-5 h-5 text-blue-600" />
                                        </div>
                                        <h4 className="text-sm font-medium text-blue-900 mb-1">
                                            Total Users
                                        </h4>
                                        <p className="text-3xl font-bold text-blue-900">
                                            {formatNumber(stats.totalUsers)}
                                        </p>
                                    </div>

                                    {/* Active Users */}
                                    <div className="card-hover bg-gradient-to-br from-green-50 to-green-100 border-2 border-green-200">
                                        <div className="flex items-center justify-between mb-4">
                                            <div className="w-12 h-12 bg-green-600 rounded-lg flex items-center justify-center">
                                                <UserCheck className="w-6 h-6 text-white" />
                                            </div>
                                            <span className="status-dot-active"></span>
                                        </div>
                                        <h4 className="text-sm font-medium text-green-900 mb-1">
                                            Active Users
                                        </h4>
                                        <p className="text-3xl font-bold text-green-900">
                                            {formatNumber(stats.activeUsers)}
                                        </p>
                                        <p className="text-xs text-green-700 mt-1">
                                            {(
                                                (stats.activeUsers /
                                                    stats.totalUsers) *
                                                100
                                            ).toFixed(1)}
                                            % of total
                                        </p>
                                    </div>

                                    {/* Inactive Users */}
                                    <div className="card-hover bg-gradient-to-br from-gray-50 to-gray-100 border-2 border-gray-200">
                                        <div className="flex items-center justify-between mb-4">
                                            <div className="w-12 h-12 bg-gray-600 rounded-lg flex items-center justify-center">
                                                <UserX className="w-6 h-6 text-white" />
                                            </div>
                                            <span className="status-dot-inactive"></span>
                                        </div>
                                        <h4 className="text-sm font-medium text-gray-900 mb-1">
                                            Inactive Users
                                        </h4>
                                        <p className="text-3xl font-bold text-gray-900">
                                            {formatNumber(stats.inactiveUsers)}
                                        </p>
                                    </div>

                                    {/* Pending Users */}
                                    <div className="card-hover bg-gradient-to-br from-yellow-50 to-yellow-100 border-2 border-yellow-200">
                                        <div className="flex items-center justify-between mb-4">
                                            <div className="w-12 h-12 bg-yellow-600 rounded-lg flex items-center justify-center">
                                                <Clock className="w-6 h-6 text-white" />
                                            </div>
                                            <span className="status-dot-pending"></span>
                                        </div>
                                        <h4 className="text-sm font-medium text-yellow-900 mb-1">
                                            Pending Users
                                        </h4>
                                        <p className="text-3xl font-bold text-yellow-900">
                                            {formatNumber(stats.pendingUsers)}
                                        </p>
                                    </div>
                                </div>
                            </div>

                            {/* User Role Statistics */}
                            <div>
                                <h4 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4">
                                    User Roles
                                </h4>
                                <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
                                    {/* Regular Users */}
                                    <div className="card-hover">
                                        <div className="flex items-center gap-4 mb-4">
                                            <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                                                <Users className="w-6 h-6 text-blue-600" />
                                            </div>
                                        </div>
                                        <h4 className="text-sm font-medium text-gray-700 mb-1">
                                            Regular Users
                                        </h4>
                                        <p className="text-3xl font-bold text-gray-900">
                                            {formatNumber(stats.userCount)}
                                        </p>
                                        <div className="mt-3 bg-blue-100 rounded-full h-2 overflow-hidden">
                                            <div
                                                className="bg-blue-600 h-full transition-all"
                                                style={{
                                                    width: `${(stats.userCount / stats.totalUsers) * 100}%`,
                                                }}
                                            ></div>
                                        </div>
                                    </div>

                                    {/* Admins */}
                                    <div className="card-hover">
                                        <div className="flex items-center gap-4 mb-4">
                                            <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                                                <Shield className="w-6 h-6 text-purple-600" />
                                            </div>
                                        </div>
                                        <h4 className="text-sm font-medium text-gray-700 mb-1">
                                            Administrators
                                        </h4>
                                        <p className="text-3xl font-bold text-gray-900">
                                            {formatNumber(stats.adminCount)}
                                        </p>
                                        <div className="mt-3 bg-purple-100 rounded-full h-2 overflow-hidden">
                                            <div
                                                className="bg-purple-600 h-full transition-all"
                                                style={{
                                                    width: `${(stats.adminCount / stats.totalUsers) * 100}%`,
                                                }}
                                            ></div>
                                        </div>
                                    </div>

                                    {/* Moderators */}
                                    <div className="card-hover">
                                        <div className="flex items-center gap-4 mb-4">
                                            <div className="w-12 h-12 bg-indigo-100 rounded-lg flex items-center justify-center">
                                                <UserCog className="w-6 h-6 text-indigo-600" />
                                            </div>
                                        </div>
                                        <h4 className="text-sm font-medium text-gray-700 mb-1">
                                            Moderators
                                        </h4>
                                        <p className="text-3xl font-bold text-gray-900">
                                            {formatNumber(stats.moderatorCount)}
                                        </p>
                                        <div className="mt-3 bg-indigo-100 rounded-full h-2 overflow-hidden">
                                            <div
                                                className="bg-indigo-600 h-full transition-all"
                                                style={{
                                                    width: `${(stats.moderatorCount / stats.totalUsers) * 100}%`,
                                                }}
                                            ></div>
                                        </div>
                                    </div>

                                    {/* Blocked Users */}
                                    <div className="card-hover">
                                        <div className="flex items-center gap-4 mb-4">
                                            <div className="w-12 h-12 bg-red-100 rounded-lg flex items-center justify-center">
                                                <UserX className="w-6 h-6 text-red-600" />
                                            </div>
                                        </div>
                                        <h4 className="text-sm font-medium text-gray-700 mb-1">
                                            Blocked Users
                                        </h4>
                                        <p className="text-3xl font-bold text-gray-900">
                                            {formatNumber(stats.blockedUsers)}
                                        </p>
                                        <div className="mt-3 bg-red-100 rounded-full h-2 overflow-hidden">
                                            <div
                                                className="bg-red-600 h-full transition-all"
                                                style={{
                                                    width: `${(stats.blockedUsers / stats.totalUsers) * 100}%`,
                                                }}
                                            ></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </>
                    )}
                </div>

                {/* Features Section */}
                <div className="mt-16">
                    <h3 className="text-2xl font-semibold text-gray-900 mb-6 text-center">
                        System Features
                    </h3>
                    <div className="grid md:grid-cols-3 gap-6">
                        <div className="card text-center">
                            <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                <Users className="w-8 h-8 text-blue-600" />
                            </div>
                            <h4 className="font-semibold text-gray-900 mb-2">
                                User Management
                            </h4>
                            <p className="text-sm text-gray-600">
                                Create, update, delete, and manage users with
                                comprehensive CRUD operations
                            </p>
                        </div>

                        <div className="card text-center">
                            <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                <Shield className="w-8 h-8 text-green-600" />
                            </div>
                            <h4 className="font-semibold text-gray-900 mb-2">
                                Role-Based Access
                            </h4>
                            <p className="text-sm text-gray-600">
                                Manage user permissions with multiple role
                                levels and access controls
                            </p>
                        </div>

                        <div className="card text-center">
                            <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                <Activity className="w-8 h-8 text-purple-600" />
                            </div>
                            <h4 className="font-semibold text-gray-900 mb-2">
                                Real-Time Updates
                            </h4>
                            <p className="text-sm text-gray-600">
                                Get instant feedback and notifications for all
                                user operations
                            </p>
                        </div>
                    </div>
                </div>
            </main>

            {/* Footer */}
            <footer className="bg-white border-t border-gray-200 mt-16">
                <div className="container-custom py-8">
                    <div className="text-center text-sm text-gray-600">
                        <p className="mb-2">
                            Built with <span className="text-red-500">♥</span>{" "}
                            using <span className="font-semibold">Next.js</span>
                            , <span className="font-semibold">TypeScript</span>,{" "}
                            <span className="font-semibold">Tailwind CSS</span>{" "}
                            and{" "}
                            <span className="font-semibold">Spring Boot</span>
                        </p>
                        <p className="text-gray-500">
                            © {new Date().getFullYear()} User Management
                            System. All rights reserved.
                        </p>
                    </div>
                </div>
            </footer>
        </div>
    );
}
