"use client";

import React, { useState } from "react";
import { useRouter, usePathname } from "next/navigation";
import { useAuth } from "@/providers/AuthProvider";
import { useTheme } from "@/providers/ThemeProvider";
import {
  Home,
  Users,
  Calendar,
  FileText,
  Settings,
  LogOut,
  Bell,
  Search,
  Menu,
  X,
  Stethoscope,
  Building2,
  DollarSign,
  Activity,
  Pill,
  UserCheck,
  Clipboard,
  BarChart3,
  Heart,
} from "lucide-react";

interface DashboardLayoutProps {
  children: React.ReactNode;
}

const DashboardLayout: React.FC<DashboardLayoutProps> = ({ children }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { user, logout, hasRole } = useAuth();
  const { actualTheme, toggleTheme } = useTheme();
  const router = useRouter();
  const pathname = usePathname();

  const handleLogout = () => {
    logout();
  };

  const getNavigationItems = () => {
    const baseItems = [
      {
        name: "Dashboard",
        href: "/dashboard",
        icon: Home,
        roles: ["ADMIN", "DOCTOR", "PATIENT"],
      },
    ];

    if (hasRole("ADMIN") || hasRole("SUPER_ADMIN")) {
      return [
        ...baseItems,
        { name: "Users", href: "/users", icon: Users, roles: ["ADMIN"] },
        {
          name: "Doctors",
          href: "/doctors",
          icon: Stethoscope,
          roles: ["ADMIN"],
        },
        {
          name: "Patients",
          href: "/patients",
          icon: UserCheck,
          roles: ["ADMIN"],
        },
        {
          name: "Appointments",
          href: "/appointments",
          icon: Calendar,
          roles: ["ADMIN"],
        },
        {
          name: "Departments",
          href: "/departments",
          icon: Building2,
          roles: ["ADMIN"],
        },
        {
          name: "Billing",
          href: "/billing",
          icon: DollarSign,
          roles: ["ADMIN"],
        },
        {
          name: "Reports",
          href: "/reports",
          icon: BarChart3,
          roles: ["ADMIN"],
        },
        {
          name: "Settings",
          href: "/settings",
          icon: Settings,
          roles: ["ADMIN"],
        },
      ];
    }

    if (hasRole("DOCTOR")) {
      return [
        ...baseItems,
        {
          name: "My Patients",
          href: "/my-patients",
          icon: Users,
          roles: ["DOCTOR"],
        },
        {
          name: "Appointments",
          href: "/appointments",
          icon: Calendar,
          roles: ["DOCTOR"],
        },
        {
          name: "Medical Records",
          href: "/records",
          icon: FileText,
          roles: ["DOCTOR"],
        },
        {
          name: "Prescriptions",
          href: "/prescriptions",
          icon: Pill,
          roles: ["DOCTOR"],
        },
        {
          name: "Lab Requests",
          href: "/lab-requests",
          icon: Activity,
          roles: ["DOCTOR"],
        },
        {
          name: "My Schedule",
          href: "/schedule",
          icon: Clipboard,
          roles: ["DOCTOR"],
        },
        {
          name: "Settings",
          href: "/settings",
          icon: Settings,
          roles: ["DOCTOR"],
        },
      ];
    }

    if (hasRole("PATIENT")) {
      return [
        ...baseItems,
        {
          name: "My Appointments",
          href: "/my-appointments",
          icon: Calendar,
          roles: ["PATIENT"],
        },
        {
          name: "Medical Records",
          href: "/my-records",
          icon: FileText,
          roles: ["PATIENT"],
        },
        {
          name: "Prescriptions",
          href: "/my-prescriptions",
          icon: Pill,
          roles: ["PATIENT"],
        },
        {
          name: "Lab Reports",
          href: "/my-lab-reports",
          icon: Activity,
          roles: ["PATIENT"],
        },
        {
          name: "Billing",
          href: "/my-billing",
          icon: DollarSign,
          roles: ["PATIENT"],
        },
        {
          name: "Health Tracker",
          href: "/health-tracker",
          icon: Heart,
          roles: ["PATIENT"],
        },
        {
          name: "Settings",
          href: "/settings",
          icon: Settings,
          roles: ["PATIENT"],
        },
      ];
    }

    return baseItems;
  };

  const navigationItems = getNavigationItems();

  const getRoleColor = () => {
    if (hasRole("ADMIN") || hasRole("SUPER_ADMIN")) return "bg-blue-600";
    if (hasRole("DOCTOR")) return "bg-teal-600";
    if (hasRole("PATIENT")) return "bg-purple-600";
    return "bg-primary-600";
  };

  const getRoleName = () => {
    if (hasRole("ADMIN") || hasRole("SUPER_ADMIN")) return "Admin";
    if (hasRole("DOCTOR")) return "Doctor";
    if (hasRole("PATIENT")) return "Patient";
    return user?.roles?.[0]?.replace("ROLE_", "") || "User";
  };

  return (
    <div className="min-h-screen bg-neutral-50 dark:bg-neutral-900">
      {/* Mobile sidebar backdrop */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black/50 z-40 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`fixed top-0 left-0 z-50 h-screen w-64 bg-white dark:bg-neutral-800 border-r border-neutral-200 dark:border-neutral-700 transition-transform duration-300 lg:translate-x-0 ${
          sidebarOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="flex flex-col h-full">
          {/* Logo */}
          <div className="flex items-center justify-between p-4 border-b border-neutral-200 dark:border-neutral-700">
            <div className="flex items-center space-x-3">
              <div className={`p-2 ${getRoleColor()} rounded-lg`}>
                <Heart className="h-6 w-6 text-white" />
              </div>
              <div>
                <h1 className="text-lg font-bold text-neutral-900 dark:text-neutral-100">
                  HMS
                </h1>
                <p className="text-xs text-neutral-600 dark:text-neutral-400">
                  {getRoleName()} Portal
                </p>
              </div>
            </div>
            <button
              onClick={() => setSidebarOpen(false)}
              className="lg:hidden p-1 text-neutral-600 dark:text-neutral-400 hover:text-neutral-900 dark:hover:text-neutral-100"
            >
              <X className="h-5 w-5" />
            </button>
          </div>

          {/* Navigation */}
          <nav className="flex-1 overflow-y-auto p-4">
            <div className="space-y-1">
              {navigationItems.map((item) => {
                const Icon = item.icon;
                const isActive = pathname === item.href;
                return (
                  <button
                    key={item.name}
                    onClick={() => {
                      router.push(item.href);
                      setSidebarOpen(false);
                    }}
                    className={`w-full flex items-center space-x-3 px-3 py-2 rounded-lg transition-colors ${
                      isActive
                        ? `${getRoleColor()} text-white`
                        : "text-neutral-700 dark:text-neutral-300 hover:bg-neutral-100 dark:hover:bg-neutral-700"
                    }`}
                  >
                    <Icon className="h-5 w-5 shrink-0" />
                    <span className="text-sm font-medium">{item.name}</span>
                  </button>
                );
              })}
            </div>
          </nav>

          {/* User Profile */}
          <div className="p-4 border-t border-neutral-200 dark:border-neutral-700">
            <div className="flex items-center space-x-3 mb-3">
              <div
                className={`h-10 w-10 ${getRoleColor()} rounded-full flex items-center justify-center shrink-0`}
              >
                <span className="text-white text-sm font-bold">
                  {user?.fullName?.charAt(0).toUpperCase()}
                </span>
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-neutral-900 dark:text-neutral-100 truncate">
                  {user?.fullName}
                </p>
                <p className="text-xs text-neutral-600 dark:text-neutral-400 truncate">
                  {user?.email}
                </p>
              </div>
            </div>
            <button
              onClick={handleLogout}
              className="w-full flex items-center justify-center space-x-2 px-3 py-2 text-sm font-medium text-red-600 hover:text-red-700 dark:text-red-400 dark:hover:text-red-300 bg-red-50 dark:bg-red-900/20 rounded-lg hover:bg-red-100 dark:hover:bg-red-900/30 transition-colors"
            >
              <LogOut className="h-4 w-4" />
              <span>Logout</span>
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <div className="lg:pl-64">
        {/* Top Header */}
        <header className="sticky top-0 z-30 bg-white dark:bg-neutral-800 border-b border-neutral-200 dark:border-neutral-700 shadow-sm">
          <div className="flex items-center justify-between px-4 py-3">
            {/* Mobile menu button */}
            <button
              onClick={() => setSidebarOpen(!sidebarOpen)}
              className="lg:hidden p-2 text-neutral-600 dark:text-neutral-400 hover:text-neutral-900 dark:hover:text-neutral-100"
            >
              <Menu className="h-6 w-6" />
            </button>

            {/* Search */}
            <div className="flex-1 max-w-2xl mx-4 hidden md:block">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-neutral-400" />
                <input
                  type="text"
                  placeholder="Search patients, doctors, appointments..."
                  className="w-full pl-10 pr-4 py-2 text-sm bg-neutral-50 dark:bg-neutral-700 border border-neutral-200 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 dark:focus:ring-primary-400 focus:border-transparent text-neutral-900 dark:text-neutral-100 placeholder-neutral-500 dark:placeholder-neutral-400"
                />
              </div>
            </div>

            {/* Actions */}
            <div className="flex items-center space-x-2">
              {/* Theme Toggle */}
              <button
                onClick={toggleTheme}
                className="p-2 text-neutral-600 dark:text-neutral-400 hover:text-neutral-900 dark:hover:text-neutral-100 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg transition-colors"
                title="Toggle theme"
              >
                {actualTheme === "light" ? (
                  <span className="text-xl">🌙</span>
                ) : (
                  <span className="text-xl">☀️</span>
                )}
              </button>

              {/* Notifications */}
              <button className="relative p-2 text-neutral-600 dark:text-neutral-400 hover:text-neutral-900 dark:hover:text-neutral-100 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg transition-colors">
                <Bell className="h-5 w-5" />
                <span className="absolute top-1 right-1 h-2 w-2 bg-red-500 rounded-full"></span>
              </button>
            </div>
          </div>
        </header>

        {/* Page Content */}
        <main className="p-4 md:p-6 lg:p-8">{children}</main>
      </div>
    </div>
  );
};

export default DashboardLayout;
