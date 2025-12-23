import React, { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import {
    Bars3Icon,
    XMarkIcon,
    UserIcon,
    UsersIcon,
    ChartBarIcon,
    Cog6ToothIcon,
    ArrowRightOnRectangleIcon,
    ShieldCheckIcon,
    CodeBracketIcon,
} from "@heroicons/react/24/outline";
import { useAuth } from "../context/AuthContext";
import { getInitials } from "../utils/formatters";

interface LayoutProps {
    children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const { user, logout, isAdmin, isModerator } = useAuth();
    const location = useLocation();
    const navigate = useNavigate();

    const navigation = [
        {
            name: "Dashboard",
            href: "/dashboard",
            icon: ChartBarIcon,
            show: true,
        },
        {
            name: "Profile",
            href: "/profile",
            icon: UserIcon,
            show: true,
        },
        {
            name: "Users",
            href: "/users",
            icon: UsersIcon,
            show: isModerator,
        },
        {
            name: "Admin Panel",
            href: "/admin",
            icon: ShieldCheckIcon,
            show: isAdmin,
        },
        {
            name: "Settings",
            href: "/settings",
            icon: Cog6ToothIcon,
            show: true,
        },
        {
            name: "Code Demo",
            href: "/code-demo",
            icon: CodeBracketIcon,
            show: true,
        },
    ].filter((item) => item.show);

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    const isActivePath = (path: string) => {
        return (
            location.pathname === path ||
            location.pathname.startsWith(path + "/")
        );
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Mobile sidebar */}
            <div
                className={`fixed inset-0 z-50 lg:hidden ${sidebarOpen ? "" : "pointer-events-none"}`}
            >
                <div
                    className={`fixed inset-0 bg-gray-600 bg-opacity-75 transition-opacity ${
                        sidebarOpen ? "opacity-100" : "opacity-0"
                    }`}
                    onClick={() => setSidebarOpen(false)}
                />
                <div
                    className={`fixed inset-y-0 left-0 flex w-full max-w-xs flex-col bg-white shadow-xl transition-transform ${
                        sidebarOpen ? "translate-x-0" : "-translate-x-full"
                    }`}
                >
                    <div className="flex h-16 items-center justify-between px-6">
                        <div className="flex items-center">
                            <ShieldCheckIcon className="h-8 w-8 text-primary-600" />
                            <span className="ml-2 text-xl font-bold text-gray-900">
                                Auth Service
                            </span>
                        </div>
                        <button
                            type="button"
                            className="text-gray-400 hover:text-gray-600"
                            onClick={() => setSidebarOpen(false)}
                        >
                            <XMarkIcon className="h-6 w-6" />
                        </button>
                    </div>
                    <nav className="flex-1 px-6 py-6">
                        <div className="space-y-1">
                            {navigation.map((item) => (
                                <Link
                                    key={item.name}
                                    to={item.href}
                                    className={`group flex items-center px-2 py-2 text-sm font-medium rounded-md transition-colors ${
                                        isActivePath(item.href)
                                            ? "bg-primary-100 text-primary-900"
                                            : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                                    }`}
                                    onClick={() => setSidebarOpen(false)}
                                >
                                    <item.icon
                                        className={`mr-3 h-6 w-6 ${
                                            isActivePath(item.href)
                                                ? "text-primary-500"
                                                : "text-gray-400 group-hover:text-gray-500"
                                        }`}
                                    />
                                    {item.name}
                                </Link>
                            ))}
                        </div>
                    </nav>
                </div>
            </div>

            {/* Desktop sidebar */}
            <div className="hidden lg:fixed lg:inset-y-0 lg:flex lg:w-64 lg:flex-col">
                <div className="flex flex-col flex-grow bg-white border-r border-gray-200">
                    <div className="flex items-center flex-shrink-0 px-6 py-4">
                        <ShieldCheckIcon className="h-8 w-8 text-primary-600" />
                        <span className="ml-2 text-xl font-bold text-gray-900">
                            Auth Service
                        </span>
                    </div>
                    <nav className="flex-1 px-6 py-6 bg-white">
                        <div className="space-y-1">
                            {navigation.map((item) => (
                                <Link
                                    key={item.name}
                                    to={item.href}
                                    className={`group flex items-center px-2 py-2 text-sm font-medium rounded-md transition-colors ${
                                        isActivePath(item.href)
                                            ? "bg-primary-100 text-primary-900"
                                            : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                                    }`}
                                >
                                    <item.icon
                                        className={`mr-3 h-6 w-6 ${
                                            isActivePath(item.href)
                                                ? "text-primary-500"
                                                : "text-gray-400 group-hover:text-gray-500"
                                        }`}
                                    />
                                    {item.name}
                                </Link>
                            ))}
                        </div>
                    </nav>
                </div>
            </div>

            {/* Main content */}
            <div className="lg:pl-64">
                {/* Top navigation */}
                <div className="sticky top-0 z-40 bg-white shadow-sm border-b border-gray-200">
                    <div className="flex h-16 items-center justify-between px-4 sm:px-6 lg:px-8">
                        <button
                            type="button"
                            className="lg:hidden -ml-2 p-2 text-gray-400 hover:text-gray-600"
                            onClick={() => setSidebarOpen(true)}
                        >
                            <Bars3Icon className="h-6 w-6" />
                        </button>

                        <div className="flex-1 lg:hidden" />

                        {/* User menu */}
                        <div className="flex items-center space-x-4">
                            <div className="flex items-center space-x-3">
                                <div className="flex-shrink-0">
                                    <div className="h-10 w-10 rounded-full bg-primary-500 flex items-center justify-center">
                                        <span className="text-sm font-medium text-white">
                                            {user
                                                ? getInitials(
                                                      user.firstName,
                                                      user.lastName,
                                                  )
                                                : "U"}
                                        </span>
                                    </div>
                                </div>
                                <div className="hidden sm:block">
                                    <p className="text-sm font-medium text-gray-900">
                                        {user
                                            ? `${user.firstName} ${user.lastName}`
                                            : "User"}
                                    </p>
                                    <p className="text-xs text-gray-500">
                                        {user?.role}
                                    </p>
                                </div>
                            </div>
                            <button
                                onClick={handleLogout}
                                className="p-2 text-gray-400 hover:text-gray-600 transition-colors"
                                title="Logout"
                            >
                                <ArrowRightOnRectangleIcon className="h-6 w-6" />
                            </button>
                        </div>
                    </div>
                </div>

                {/* Page content */}
                <main className="py-8">
                    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                        {children}
                    </div>
                </main>
            </div>
        </div>
    );
};

export default Layout;
