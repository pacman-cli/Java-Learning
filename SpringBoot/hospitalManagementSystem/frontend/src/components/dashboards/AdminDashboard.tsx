"use client";

import React, { useState } from "react";
import {
    Users,
    UserCheck,
    Calendar,
    Activity,
    DollarSign,
    TrendingUp,
    Building2,
    Stethoscope,
    FileText,
    AlertCircle,
    CheckCircle,
    Clock,
    UserX,
    Plus,
    Download,
    Filter,
    Search,
    ChevronRight,
    Settings,
    Shield,
    Database,
    BarChart3,
} from "lucide-react";

interface StatCardProps {
    title: string;
    value: string | number;
    change?: number;
    icon: React.ElementType;
    color: string;
    trend?: "up" | "down";
}

const StatCard: React.FC<StatCardProps> = ({
    title,
    value,
    change,
    icon: Icon,
    color,
    trend,
}) => {
    return (
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
            <div className="flex items-center justify-between">
                <div className="flex-1">
                    <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                        {title}
                    </p>
                    <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                        {value}
                    </p>
                    {change !== undefined && (
                        <div className="flex items-center mt-2">
                            <span
                                className={`text-sm font-medium ${
                                    trend === "up"
                                        ? "text-green-600"
                                        : "text-red-600"
                                }`}
                            >
                                {trend === "up" ? "+" : ""}
                                {change}%
                            </span>
                            <span className="text-xs text-neutral-500 dark:text-neutral-400 ml-2">
                                vs last month
                            </span>
                        </div>
                    )}
                </div>
                <div className={`${color} p-3 rounded-lg`}>
                    <Icon className="h-6 w-6 text-white" />
                </div>
            </div>
        </div>
    );
};

interface QuickActionProps {
    title: string;
    description: string;
    icon: React.ElementType;
    color: string;
    onClick: () => void;
}

const QuickAction: React.FC<QuickActionProps> = ({
    title,
    description,
    icon: Icon,
    color,
    onClick,
}) => {
    return (
        <button
            onClick={onClick}
            className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4 hover:shadow-lg transition-all hover:scale-105 text-left w-full"
        >
            <div className="flex items-start space-x-3">
                <div className={`${color} p-2 rounded-lg`}>
                    <Icon className="h-5 w-5 text-white" />
                </div>
                <div className="flex-1 min-w-0">
                    <h4 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                        {title}
                    </h4>
                    <p className="text-xs text-neutral-600 dark:text-neutral-400 mt-1">
                        {description}
                    </p>
                </div>
                <ChevronRight className="h-4 w-4 text-neutral-400 flex-shrink-0" />
            </div>
        </button>
    );
};

interface RecentActivityProps {
    activity: {
        id: number;
        type: string;
        user: string;
        action: string;
        time: string;
        status: "success" | "warning" | "error";
    };
}

const RecentActivity: React.FC<RecentActivityProps> = ({ activity }) => {
    const getStatusIcon = () => {
        switch (activity.status) {
            case "success":
                return <CheckCircle className="h-4 w-4 text-green-500" />;
            case "warning":
                return <AlertCircle className="h-4 w-4 text-yellow-500" />;
            case "error":
                return <AlertCircle className="h-4 w-4 text-red-500" />;
        }
    };

    return (
        <div className="flex items-start space-x-3 p-3 hover:bg-neutral-50 dark:hover:bg-neutral-700 rounded-lg transition-colors">
            <div className="flex-shrink-0 mt-1">{getStatusIcon()}</div>
            <div className="flex-1 min-w-0">
                <p className="text-sm text-neutral-900 dark:text-neutral-100">
                    <span className="font-medium">{activity.user}</span>{" "}
                    {activity.action}
                </p>
                <p className="text-xs text-neutral-500 dark:text-neutral-400 mt-1">
                    {activity.type} • {activity.time}
                </p>
            </div>
        </div>
    );
};

const AdminDashboard: React.FC = () => {
    const [searchQuery, setSearchQuery] = useState("");

    // Mock data
    const stats = {
        totalPatients: 1247,
        totalDoctors: 45,
        totalStaff: 128,
        todayAppointments: 89,
        pendingAppointments: 12,
        completedAppointments: 67,
        monthlyRevenue: 1250000,
        revenueGrowth: 15.3,
        occupancyRate: 82,
        activeUsers: 234,
    };

    const recentActivities = [
        {
            id: 1,
            type: "User Management",
            user: "Dr. Sarah Smith",
            action: "registered a new account",
            time: "5 minutes ago",
            status: "success" as const,
        },
        {
            id: 2,
            type: "Appointment",
            user: "John Doe",
            action: "cancelled an appointment",
            time: "12 minutes ago",
            status: "warning" as const,
        },
        {
            id: 3,
            type: "System",
            user: "System",
            action: "completed daily backup",
            time: "1 hour ago",
            status: "success" as const,
        },
        {
            id: 4,
            type: "Payment",
            user: "Mary Johnson",
            action: "payment failed",
            time: "2 hours ago",
            status: "error" as const,
        },
        {
            id: 5,
            type: "Department",
            user: "Admin",
            action: "updated Cardiology department",
            time: "3 hours ago",
            status: "success" as const,
        },
    ];

    const departmentStats = [
        { name: "Cardiology", patients: 145, revenue: 450000, growth: 8.5 },
        { name: "Orthopedics", patients: 132, revenue: 380000, growth: 12.3 },
        { name: "Pediatrics", patients: 201, revenue: 290000, growth: 5.7 },
        { name: "Neurology", patients: 89, revenue: 520000, growth: 15.2 },
    ];

    const handleQuickAction = (action: string) => {
        console.log(`Quick action: ${action}`);
        // Implement navigation or modal opening
    };

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="bg-gradient-to-r from-blue-600 to-blue-800 rounded-lg shadow-lg p-6 text-white">
                <div className="flex items-center justify-between">
                    <div>
                        <h1 className="text-3xl font-bold flex items-center">
                            <Shield className="h-8 w-8 mr-3" />
                            Admin Dashboard
                        </h1>
                        <p className="mt-2 text-blue-100">
                            Complete system overview and management
                        </p>
                    </div>
                    <div className="hidden lg:flex items-center space-x-4">
                        <button className="bg-white/20 hover:bg-white/30 px-4 py-2 rounded-lg transition-colors flex items-center space-x-2">
                            <Download className="h-4 w-4" />
                            <span>Export Report</span>
                        </button>
                        <button className="bg-white/20 hover:bg-white/30 px-4 py-2 rounded-lg transition-colors flex items-center space-x-2">
                            <Settings className="h-4 w-4" />
                            <span>System Settings</span>
                        </button>
                    </div>
                </div>
            </div>

            {/* Key Statistics */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <StatCard
                    title="Total Patients"
                    value={stats.totalPatients}
                    change={8.2}
                    icon={Users}
                    color="bg-blue-500"
                    trend="up"
                />
                <StatCard
                    title="Total Doctors"
                    value={stats.totalDoctors}
                    change={3.1}
                    icon={Stethoscope}
                    color="bg-green-500"
                    trend="up"
                />
                <StatCard
                    title="Today's Appointments"
                    value={stats.todayAppointments}
                    change={-2.5}
                    icon={Calendar}
                    color="bg-purple-500"
                    trend="down"
                />
                <StatCard
                    title="Monthly Revenue"
                    value={`$${(stats.monthlyRevenue / 1000).toFixed(0)}K`}
                    change={stats.revenueGrowth}
                    icon={DollarSign}
                    color="bg-orange-500"
                    trend="up"
                />
            </div>

            {/* Secondary Statistics */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <StatCard
                    title="Total Staff"
                    value={stats.totalStaff}
                    icon={UserCheck}
                    color="bg-indigo-500"
                />
                <StatCard
                    title="Pending Appointments"
                    value={stats.pendingAppointments}
                    icon={Clock}
                    color="bg-yellow-500"
                />
                <StatCard
                    title="Occupancy Rate"
                    value={`${stats.occupancyRate}%`}
                    icon={Building2}
                    color="bg-teal-500"
                />
                <StatCard
                    title="Active Users"
                    value={stats.activeUsers}
                    icon={Activity}
                    color="bg-pink-500"
                />
            </div>

            {/* Quick Actions */}
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4 flex items-center">
                    <Plus className="h-5 w-5 mr-2" />
                    Quick Actions
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                    <QuickAction
                        title="Manage Users"
                        description="Add, edit, or remove users"
                        icon={Users}
                        color="bg-blue-500"
                        onClick={() => handleQuickAction("manage-users")}
                    />
                    <QuickAction
                        title="Manage Doctors"
                        description="View and manage doctor profiles"
                        icon={Stethoscope}
                        color="bg-green-500"
                        onClick={() => handleQuickAction("manage-doctors")}
                    />
                    <QuickAction
                        title="Manage Departments"
                        description="Configure hospital departments"
                        icon={Building2}
                        color="bg-purple-500"
                        onClick={() => handleQuickAction("manage-departments")}
                    />
                    <QuickAction
                        title="View Reports"
                        description="Access system reports and analytics"
                        icon={BarChart3}
                        color="bg-orange-500"
                        onClick={() => handleQuickAction("view-reports")}
                    />
                    <QuickAction
                        title="Financial Overview"
                        description="Billing and revenue management"
                        icon={DollarSign}
                        color="bg-teal-500"
                        onClick={() => handleQuickAction("financial")}
                    />
                    <QuickAction
                        title="System Settings"
                        description="Configure system preferences"
                        icon={Settings}
                        color="bg-indigo-500"
                        onClick={() => handleQuickAction("settings")}
                    />
                    <QuickAction
                        title="Database Management"
                        description="Backup and restore operations"
                        icon={Database}
                        color="bg-red-500"
                        onClick={() => handleQuickAction("database")}
                    />
                    <QuickAction
                        title="Audit Logs"
                        description="View system activity logs"
                        icon={FileText}
                        color="bg-gray-500"
                        onClick={() => handleQuickAction("audit-logs")}
                    />
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {/* Department Performance */}
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                    <div className="flex items-center justify-between mb-4">
                        <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 flex items-center">
                            <Building2 className="h-5 w-5 mr-2" />
                            Department Performance
                        </h2>
                        <button className="text-sm text-blue-600 hover:text-blue-700 dark:text-blue-400 flex items-center">
                            View All
                            <ChevronRight className="h-4 w-4 ml-1" />
                        </button>
                    </div>
                    <div className="space-y-4">
                        {departmentStats.map((dept, index) => (
                            <div
                                key={index}
                                className="border-b border-neutral-200 dark:border-neutral-700 last:border-0 pb-4 last:pb-0"
                            >
                                <div className="flex items-center justify-between mb-2">
                                    <h3 className="font-medium text-neutral-900 dark:text-neutral-100">
                                        {dept.name}
                                    </h3>
                                    <span className="text-sm text-green-600 font-medium">
                                        +{dept.growth}%
                                    </span>
                                </div>
                                <div className="flex items-center justify-between text-sm">
                                    <div className="flex items-center text-neutral-600 dark:text-neutral-400">
                                        <Users className="h-4 w-4 mr-1" />
                                        {dept.patients} patients
                                    </div>
                                    <div className="flex items-center text-neutral-600 dark:text-neutral-400">
                                        <DollarSign className="h-4 w-4 mr-1" />
                                        ${(dept.revenue / 1000).toFixed(0)}K
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Recent Activity */}
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                    <div className="flex items-center justify-between mb-4">
                        <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 flex items-center">
                            <Activity className="h-5 w-5 mr-2" />
                            Recent Activity
                        </h2>
                        <button className="text-sm text-blue-600 hover:text-blue-700 dark:text-blue-400 flex items-center">
                            View All
                            <ChevronRight className="h-4 w-4 ml-1" />
                        </button>
                    </div>
                    <div className="space-y-2">
                        {recentActivities.map((activity) => (
                            <RecentActivity key={activity.id} activity={activity} />
                        ))}
                    </div>
                </div>
            </div>

            {/* System Status */}
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4 flex items-center">
                    <Activity className="h-5 w-5 mr-2" />
                    System Status
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div className="border border-neutral-200 dark:border-neutral-700 rounded-lg p-4">
                        <div className="flex items-center justify-between">
                            <span className="text-sm text-neutral-600 dark:text-neutral-400">
                                Server Status
                            </span>
                            <span className="flex items-center text-green-600">
                                <div className="h-2 w-2 bg-green-500 rounded-full mr-2"></div>
                                Online
                            </span>
                        </div>
                    </div>
                    <div className="border border-neutral-200 dark:border-neutral-700 rounded-lg p-4">
                        <div className="flex items-center justify-between">
                            <span className="text-sm text-neutral-600 dark:text-neutral-400">
                                Database
                            </span>
                            <span className="flex items-center text-green-600">
                                <div className="h-2 w-2 bg-green-500 rounded-full mr-2"></div>
                                Healthy
                            </span>
                        </div>
                    </div>
                    <div className="border border-neutral-200 dark:border-neutral-700 rounded-lg p-4">
                        <div className="flex items-center justify-between">
                            <span className="text-sm text-neutral-600 dark:text-neutral-400">
                                Last Backup
                            </span>
                            <span className="text-sm text-neutral-900 dark:text-neutral-100">
                                2 hours ago
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
