"use client";

import React, { useState } from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
    Search,
    Filter,
    Users,
    Phone,
    Mail,
    Calendar,
    FileText,
    MoreVertical,
    Eye,
    Edit,
    AlertCircle,
    Heart,
    Activity,
    Pill,
    Plus,
    Download,
    ChevronRight,
} from "lucide-react";

interface Patient {
    id: number;
    name: string;
    age: number;
    gender: string;
    phone: string;
    email: string;
    bloodType: string;
    lastVisit: string;
    nextAppointment: string;
    condition: string;
    priority: "high" | "medium" | "low";
    status: "active" | "inactive";
    appointments: number;
    prescriptions: number;
}

function MyPatientsPage() {
    const { user } = useAuth();
    const [searchQuery, setSearchQuery] = useState("");
    const [filterStatus, setFilterStatus] = useState<"all" | "active" | "inactive">("all");
    const [filterPriority, setFilterPriority] = useState<"all" | "high" | "medium" | "low">("all");

    // Mock patient data
    const patients: Patient[] = [
        {
            id: 1,
            name: "Robert Chen",
            age: 45,
            gender: "Male",
            phone: "+1 (555) 123-4567",
            email: "robert.chen@email.com",
            bloodType: "A+",
            lastVisit: "Nov 18, 2024",
            nextAppointment: "Nov 25, 2024",
            condition: "Hypertension, Type 2 Diabetes",
            priority: "high",
            status: "active",
            appointments: 12,
            prescriptions: 3,
        },
        {
            id: 2,
            name: "Maria Garcia",
            age: 32,
            gender: "Female",
            phone: "+1 (555) 234-5678",
            email: "maria.garcia@email.com",
            bloodType: "O+",
            lastVisit: "Nov 15, 2024",
            nextAppointment: "Dec 5, 2024",
            condition: "Migraine",
            priority: "medium",
            status: "active",
            appointments: 8,
            prescriptions: 2,
        },
        {
            id: 3,
            name: "David Kim",
            age: 58,
            gender: "Male",
            phone: "+1 (555) 345-6789",
            email: "david.kim@email.com",
            bloodType: "B+",
            lastVisit: "Nov 17, 2024",
            nextAppointment: "Nov 27, 2024",
            condition: "Type 2 Diabetes, High Cholesterol",
            priority: "high",
            status: "active",
            appointments: 15,
            prescriptions: 4,
        },
        {
            id: 4,
            name: "Sarah Johnson",
            age: 28,
            gender: "Female",
            phone: "+1 (555) 456-7890",
            email: "sarah.j@email.com",
            bloodType: "AB+",
            lastVisit: "Nov 10, 2024",
            nextAppointment: "Dec 10, 2024",
            condition: "Asthma",
            priority: "low",
            status: "active",
            appointments: 6,
            prescriptions: 1,
        },
        {
            id: 5,
            name: "Michael Brown",
            age: 62,
            gender: "Male",
            phone: "+1 (555) 567-8901",
            email: "m.brown@email.com",
            bloodType: "O-",
            lastVisit: "Nov 20, 2024",
            nextAppointment: "Nov 22, 2024",
            condition: "Cardiovascular Disease",
            priority: "high",
            status: "active",
            appointments: 20,
            prescriptions: 5,
        },
        {
            id: 6,
            name: "Emily Davis",
            age: 35,
            gender: "Female",
            phone: "+1 (555) 678-9012",
            email: "emily.d@email.com",
            bloodType: "A-",
            lastVisit: "Oct 28, 2024",
            nextAppointment: "-",
            condition: "Annual Checkup",
            priority: "low",
            status: "inactive",
            appointments: 3,
            prescriptions: 0,
        },
    ];

    const filteredPatients = patients.filter((patient) => {
        const matchesSearch =
            patient.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            patient.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
            patient.condition.toLowerCase().includes(searchQuery.toLowerCase());

        const matchesStatus = filterStatus === "all" || patient.status === filterStatus;
        const matchesPriority = filterPriority === "all" || patient.priority === filterPriority;

        return matchesSearch && matchesStatus && matchesPriority;
    });

    const stats = {
        total: patients.length,
        active: patients.filter((p) => p.status === "active").length,
        high: patients.filter((p) => p.priority === "high").length,
        appointments: patients.reduce((sum, p) => sum + p.appointments, 0),
    };

    const getPriorityColor = (priority: string) => {
        switch (priority) {
            case "high":
                return "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200";
            case "medium":
                return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200";
            case "low":
                return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200";
            default:
                return "bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200";
        }
    };

    const getPriorityBorder = (priority: string) => {
        switch (priority) {
            case "high":
                return "border-l-4 border-l-red-500";
            case "medium":
                return "border-l-4 border-l-yellow-500";
            case "low":
                return "border-l-4 border-l-green-500";
            default:
                return "";
        }
    };

    return (
        <DashboardLayout>
            <div className="space-y-6">
                {/* Header */}
                <div className="flex items-center justify-between">
                    <div>
                        <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 flex items-center">
                            <Users className="h-8 w-8 mr-3 text-teal-600" />
                            My Patients
                        </h1>
                        <p className="mt-2 text-neutral-600 dark:text-neutral-400">
                            Manage and monitor your patients
                        </p>
                    </div>
                    <div className="flex items-center space-x-3">
                        <button className="btn-outline btn-sm flex items-center space-x-2">
                            <Download className="h-4 w-4" />
                            <span>Export</span>
                        </button>
                        <button className="btn-primary btn-sm flex items-center space-x-2">
                            <Plus className="h-4 w-4" />
                            <span>Add Patient</span>
                        </button>
                    </div>
                </div>

                {/* Stats */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-neutral-600 dark:text-neutral-400">Total Patients</p>
                                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-1">
                                    {stats.total}
                                </p>
                            </div>
                            <div className="bg-blue-500 p-3 rounded-lg">
                                <Users className="h-6 w-6 text-white" />
                            </div>
                        </div>
                    </div>
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-neutral-600 dark:text-neutral-400">Active Patients</p>
                                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-1">
                                    {stats.active}
                                </p>
                            </div>
                            <div className="bg-green-500 p-3 rounded-lg">
                                <Activity className="h-6 w-6 text-white" />
                            </div>
                        </div>
                    </div>
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-neutral-600 dark:text-neutral-400">High Priority</p>
                                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-1">
                                    {stats.high}
                                </p>
                            </div>
                            <div className="bg-red-500 p-3 rounded-lg">
                                <AlertCircle className="h-6 w-6 text-white" />
                            </div>
                        </div>
                    </div>
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-neutral-600 dark:text-neutral-400">Total Visits</p>
                                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-1">
                                    {stats.appointments}
                                </p>
                            </div>
                            <div className="bg-purple-500 p-3 rounded-lg">
                                <Calendar className="h-6 w-6 text-white" />
                            </div>
                        </div>
                    </div>
                </div>

                {/* Filters and Search */}
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4">
                    <div className="flex flex-col md:flex-row gap-4">
                        {/* Search */}
                        <div className="flex-1">
                            <div className="relative">
                                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                                <input
                                    type="text"
                                    placeholder="Search patients by name, email, or condition..."
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                    className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                                />
                            </div>
                        </div>

                        {/* Status Filter */}
                        <select
                            value={filterStatus}
                            onChange={(e) => setFilterStatus(e.target.value as any)}
                            className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                        >
                            <option value="all">All Status</option>
                            <option value="active">Active</option>
                            <option value="inactive">Inactive</option>
                        </select>

                        {/* Priority Filter */}
                        <select
                            value={filterPriority}
                            onChange={(e) => setFilterPriority(e.target.value as any)}
                            className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                        >
                            <option value="all">All Priority</option>
                            <option value="high">High Priority</option>
                            <option value="medium">Medium Priority</option>
                            <option value="low">Low Priority</option>
                        </select>
                    </div>
                </div>

                {/* Patients List */}
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow">
                    <div className="p-6">
                        <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                            Patient List ({filteredPatients.length})
                        </h2>
                        <div className="space-y-4">
                            {filteredPatients.map((patient) => (
                                <div
                                    key={patient.id}
                                    className={`bg-neutral-50 dark:bg-neutral-700 rounded-lg p-6 hover:shadow-md transition-shadow ${getPriorityBorder(
                                        patient.priority
                                    )}`}
                                >
                                    <div className="flex items-start justify-between mb-4">
                                        <div className="flex items-start space-x-4">
                                            <div className="bg-teal-100 dark:bg-teal-900 p-3 rounded-full">
                                                <Users className="h-6 w-6 text-teal-600 dark:text-teal-300" />
                                            </div>
                                            <div>
                                                <div className="flex items-center space-x-3 mb-2">
                                                    <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                                                        {patient.name}
                                                    </h3>
                                                    <span
                                                        className={`text-xs font-semibold px-2.5 py-1 rounded ${getPriorityColor(
                                                            patient.priority
                                                        )}`}
                                                    >
                                                        {patient.priority.toUpperCase()}
                                                    </span>
                                                    <span
                                                        className={`text-xs font-semibold px-2.5 py-1 rounded ${
                                                            patient.status === "active"
                                                                ? "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200"
                                                                : "bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200"
                                                        }`}
                                                    >
                                                        {patient.status.toUpperCase()}
                                                    </span>
                                                </div>
                                                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm text-neutral-600 dark:text-neutral-400">
                                                    <div>
                                                        <span className="font-medium">Age:</span> {patient.age} years
                                                    </div>
                                                    <div>
                                                        <span className="font-medium">Gender:</span> {patient.gender}
                                                    </div>
                                                    <div>
                                                        <span className="font-medium">Blood:</span> {patient.bloodType}
                                                    </div>
                                                    <div>
                                                        <span className="font-medium">Last Visit:</span>{" "}
                                                        {patient.lastVisit}
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                                        <div className="flex items-center text-sm text-neutral-700 dark:text-neutral-300">
                                            <Phone className="h-4 w-4 mr-2 text-neutral-500" />
                                            {patient.phone}
                                        </div>
                                        <div className="flex items-center text-sm text-neutral-700 dark:text-neutral-300">
                                            <Mail className="h-4 w-4 mr-2 text-neutral-500" />
                                            {patient.email}
                                        </div>
                                        <div className="flex items-center text-sm text-neutral-700 dark:text-neutral-300">
                                            <Heart className="h-4 w-4 mr-2 text-neutral-500" />
                                            <span className="font-medium">Condition:</span>
                                            <span className="ml-2">{patient.condition}</span>
                                        </div>
                                        <div className="flex items-center text-sm text-neutral-700 dark:text-neutral-300">
                                            <Calendar className="h-4 w-4 mr-2 text-neutral-500" />
                                            <span className="font-medium">Next:</span>
                                            <span className="ml-2">{patient.nextAppointment}</span>
                                        </div>
                                    </div>

                                    <div className="flex items-center justify-between pt-4 border-t border-neutral-200 dark:border-neutral-600">
                                        <div className="flex items-center space-x-4 text-sm text-neutral-600 dark:text-neutral-400">
                                            <div className="flex items-center">
                                                <Calendar className="h-4 w-4 mr-1" />
                                                {patient.appointments} visits
                                            </div>
                                            <div className="flex items-center">
                                                <Pill className="h-4 w-4 mr-1" />
                                                {patient.prescriptions} prescriptions
                                            </div>
                                        </div>
                                        <div className="flex items-center space-x-2">
                                            <button className="btn-outline btn-sm flex items-center space-x-1">
                                                <Eye className="h-4 w-4" />
                                                <span>View Records</span>
                                            </button>
                                            <button className="btn-primary btn-sm flex items-center space-x-1">
                                                <Calendar className="h-4 w-4" />
                                                <span>Book Appointment</span>
                                            </button>
                                            <button className="btn-outline btn-sm p-2">
                                                <MoreVertical className="h-4 w-4" />
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </DashboardLayout>
    );
}

export default withAuth(MyPatientsPage, ["DOCTOR"]);
