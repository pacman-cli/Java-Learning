"use client";

import React, { useState } from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
    Pill,
    Search,
    Filter,
    Plus,
    Download,
    Edit,
    Trash2,
    Calendar,
    User,
    AlertCircle,
    CheckCircle,
    Clock,
    FileText,
    Printer,
} from "lucide-react";

interface Prescription {
    id: number;
    patientName: string;
    patientAge: number;
    medication: string;
    dosage: string;
    frequency: string;
    duration: string;
    startDate: string;
    endDate: string;
    prescribedDate: string;
    status: "active" | "completed" | "discontinued";
    instructions: string;
    refills: number;
    quantity: string;
}

function PrescriptionsPage() {
    const { user } = useAuth();
    const [searchQuery, setSearchQuery] = useState("");
    const [filterStatus, setFilterStatus] = useState<string>("all");

    // Mock prescriptions data
    const prescriptions: Prescription[] = [
        {
            id: 1,
            patientName: "Robert Chen",
            patientAge: 45,
            medication: "Lisinopril",
            dosage: "10mg",
            frequency: "Once daily (morning)",
            duration: "3 months",
            startDate: "Nov 1, 2024",
            endDate: "Feb 1, 2025",
            prescribedDate: "Nov 1, 2024",
            status: "active",
            instructions: "Take with water in the morning. Monitor blood pressure daily.",
            refills: 2,
            quantity: "90 tablets",
        },
        {
            id: 2,
            patientName: "Robert Chen",
            patientAge: 45,
            medication: "Metformin",
            dosage: "500mg",
            frequency: "Twice daily (with meals)",
            duration: "6 months",
            startDate: "Nov 1, 2024",
            endDate: "May 1, 2025",
            prescribedDate: "Nov 1, 2024",
            status: "active",
            instructions: "Take with meals. Monitor blood sugar levels regularly.",
            refills: 5,
            quantity: "180 tablets",
        },
        {
            id: 3,
            patientName: "Maria Garcia",
            patientAge: 32,
            medication: "Sumatriptan",
            dosage: "50mg",
            frequency: "As needed (max 2 per day)",
            duration: "6 months",
            startDate: "Oct 15, 2024",
            endDate: "Apr 15, 2025",
            prescribedDate: "Oct 15, 2024",
            status: "active",
            instructions: "Take at the first sign of migraine. Do not exceed 2 tablets in 24 hours.",
            refills: 3,
            quantity: "20 tablets",
        },
        {
            id: 4,
            patientName: "Sarah Johnson",
            patientAge: 28,
            medication: "Albuterol Inhaler",
            dosage: "90mcg",
            frequency: "2 puffs every 4-6 hours as needed",
            duration: "1 year",
            startDate: "Sep 1, 2024",
            endDate: "Sep 1, 2025",
            prescribedDate: "Sep 1, 2024",
            status: "active",
            instructions: "Use as needed for asthma symptoms. Shake well before use.",
            refills: 6,
            quantity: "1 inhaler (200 doses)",
        },
        {
            id: 5,
            patientName: "Michael Brown",
            patientAge: 62,
            medication: "Atorvastatin",
            dosage: "20mg",
            frequency: "Once daily (evening)",
            duration: "Ongoing",
            startDate: "Aug 1, 2024",
            endDate: "Ongoing",
            prescribedDate: "Aug 1, 2024",
            status: "active",
            instructions: "Take in the evening. Avoid grapefruit juice.",
            refills: 11,
            quantity: "90 tablets",
        },
        {
            id: 6,
            patientName: "Emily Davis",
            patientAge: 35,
            medication: "Amoxicillin",
            dosage: "500mg",
            frequency: "Three times daily",
            duration: "7 days",
            startDate: "Oct 20, 2024",
            endDate: "Oct 27, 2024",
            prescribedDate: "Oct 20, 2024",
            status: "completed",
            instructions: "Complete the full course. Take with food if upset stomach occurs.",
            refills: 0,
            quantity: "21 capsules",
        },
        {
            id: 7,
            patientName: "David Kim",
            patientAge: 58,
            medication: "Aspirin",
            dosage: "81mg",
            frequency: "Once daily",
            duration: "Ongoing",
            startDate: "Jul 1, 2024",
            endDate: "Ongoing",
            prescribedDate: "Jul 1, 2024",
            status: "active",
            instructions: "Take with food to prevent stomach upset.",
            refills: 11,
            quantity: "90 tablets",
        },
        {
            id: 8,
            patientName: "Linda Martinez",
            patientAge: 51,
            medication: "Omeprazole",
            dosage: "20mg",
            frequency: "Once daily (before breakfast)",
            duration: "3 months",
            startDate: "Oct 1, 2024",
            endDate: "Jan 1, 2025",
            prescribedDate: "Oct 1, 2024",
            status: "active",
            instructions: "Take 30 minutes before breakfast.",
            refills: 2,
            quantity: "90 capsules",
        },
    ];

    const filteredPrescriptions = prescriptions.filter((prescription) => {
        const matchesSearch =
            prescription.patientName.toLowerCase().includes(searchQuery.toLowerCase()) ||
            prescription.medication.toLowerCase().includes(searchQuery.toLowerCase());

        const matchesStatus = filterStatus === "all" || prescription.status === filterStatus;

        return matchesSearch && matchesStatus;
    });

    const stats = {
        total: prescriptions.length,
        active: prescriptions.filter((p) => p.status === "active").length,
        completed: prescriptions.filter((p) => p.status === "completed").length,
        patients: new Set(prescriptions.map((p) => p.patientName)).size,
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case "active":
                return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200";
            case "completed":
                return "bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200";
            case "discontinued":
                return "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200";
            default:
                return "bg-gray-100 text-gray-800";
        }
    };

    const getStatusIcon = (status: string) => {
        switch (status) {
            case "active":
                return <CheckCircle className="h-4 w-4" />;
            case "completed":
                return <Clock className="h-4 w-4" />;
            case "discontinued":
                return <AlertCircle className="h-4 w-4" />;
            default:
                return <Clock className="h-4 w-4" />;
        }
    };

    return (
        <DashboardLayout>
            <div className="space-y-6">
                {/* Header */}
                <div className="flex items-center justify-between">
                    <div>
                        <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 flex items-center">
                            <Pill className="h-8 w-8 mr-3 text-teal-600" />
                            Prescriptions
                        </h1>
                        <p className="mt-2 text-neutral-600 dark:text-neutral-400">
                            Manage patient medications and prescriptions
                        </p>
                    </div>
                    <div className="flex items-center space-x-3">
                        <button className="btn-outline btn-sm flex items-center space-x-2">
                            <Download className="h-4 w-4" />
                            <span>Export</span>
                        </button>
                        <button className="btn-primary btn-sm flex items-center space-x-2">
                            <Plus className="h-4 w-4" />
                            <span>New Prescription</span>
                        </button>
                    </div>
                </div>

                {/* Stats */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-neutral-600 dark:text-neutral-400">Total Prescriptions</p>
                                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-1">
                                    {stats.total}
                                </p>
                            </div>
                            <div className="bg-blue-500 p-3 rounded-lg">
                                <Pill className="h-6 w-6 text-white" />
                            </div>
                        </div>
                    </div>
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-neutral-600 dark:text-neutral-400">Active</p>
                                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-1">
                                    {stats.active}
                                </p>
                            </div>
                            <div className="bg-green-500 p-3 rounded-lg">
                                <CheckCircle className="h-6 w-6 text-white" />
                            </div>
                        </div>
                    </div>
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-neutral-600 dark:text-neutral-400">Completed</p>
                                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-1">
                                    {stats.completed}
                                </p>
                            </div>
                            <div className="bg-gray-500 p-3 rounded-lg">
                                <Clock className="h-6 w-6 text-white" />
                            </div>
                        </div>
                    </div>
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm text-neutral-600 dark:text-neutral-400">Patients</p>
                                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-1">
                                    {stats.patients}
                                </p>
                            </div>
                            <div className="bg-purple-500 p-3 rounded-lg">
                                <User className="h-6 w-6 text-white" />
                            </div>
                        </div>
                    </div>
                </div>

                {/* Filters */}
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4">
                    <div className="flex flex-col md:flex-row gap-4">
                        <div className="flex-1">
                            <div className="relative">
                                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                                <input
                                    type="text"
                                    placeholder="Search by patient name or medication..."
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                    className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                                />
                            </div>
                        </div>
                        <select
                            value={filterStatus}
                            onChange={(e) => setFilterStatus(e.target.value)}
                            className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                        >
                            <option value="all">All Status</option>
                            <option value="active">Active</option>
                            <option value="completed">Completed</option>
                            <option value="discontinued">Discontinued</option>
                        </select>
                    </div>
                </div>

                {/* Prescriptions List */}
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow">
                    <div className="p-6">
                        <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                            Prescription List ({filteredPrescriptions.length})
                        </h2>
                        <div className="space-y-4">
                            {filteredPrescriptions.map((prescription) => (
                                <div
                                    key={prescription.id}
                                    className="bg-neutral-50 dark:bg-neutral-700 rounded-lg p-6 hover:shadow-md transition-shadow"
                                >
                                    <div className="flex items-start justify-between mb-4">
                                        <div className="flex items-start space-x-4">
                                            <div className="bg-purple-100 dark:bg-purple-900 p-3 rounded-lg">
                                                <Pill className="h-6 w-6 text-purple-600 dark:text-purple-300" />
                                            </div>
                                            <div>
                                                <div className="flex items-center space-x-3 mb-2">
                                                    <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                                                        {prescription.medication}
                                                    </h3>
                                                    <span
                                                        className={`flex items-center space-x-1 text-xs font-semibold px-2.5 py-1 rounded ${getStatusColor(
                                                            prescription.status
                                                        )}`}
                                                    >
                                                        {getStatusIcon(prescription.status)}
                                                        <span>{prescription.status.toUpperCase()}</span>
                                                    </span>
                                                </div>
                                                <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400 mb-2">
                                                    <User className="h-4 w-4" />
                                                    <span className="font-medium">{prescription.patientName}</span>
                                                    <span>•</span>
                                                    <span>{prescription.patientAge} years</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
                                        <div>
                                            <p className="text-xs text-neutral-500 dark:text-neutral-400 mb-1">Dosage</p>
                                            <p className="text-sm font-medium text-neutral-900 dark:text-neutral-100">
                                                {prescription.dosage}
                                            </p>
                                        </div>
                                        <div>
                                            <p className="text-xs text-neutral-500 dark:text-neutral-400 mb-1">Frequency</p>
                                            <p className="text-sm font-medium text-neutral-900 dark:text-neutral-100">
                                                {prescription.frequency}
                                            </p>
                                        </div>
                                        <div>
                                            <p className="text-xs text-neutral-500 dark:text-neutral-400 mb-1">Duration</p>
                                            <p className="text-sm font-medium text-neutral-900 dark:text-neutral-100">
                                                {prescription.duration}
                                            </p>
                                        </div>
                                        <div>
                                            <p className="text-xs text-neutral-500 dark:text-neutral-400 mb-1">Quantity</p>
                                            <p className="text-sm font-medium text-neutral-900 dark:text-neutral-100">
                                                {prescription.quantity}
                                            </p>
                                        </div>
                                    </div>

                                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                                        <div className="flex items-center text-sm">
                                            <Calendar className="h-4 w-4 mr-2 text-neutral-500" />
                                            <span className="text-neutral-600 dark:text-neutral-400">
                                                Start: <span className="font-medium text-neutral-900 dark:text-neutral-100">{prescription.startDate}</span>
                                            </span>
                                        </div>
                                        <div className="flex items-center text-sm">
                                            <Calendar className="h-4 w-4 mr-2 text-neutral-500" />
                                            <span className="text-neutral-600 dark:text-neutral-400">
                                                End: <span className="font-medium text-neutral-900 dark:text-neutral-100">{prescription.endDate}</span>
                                            </span>
                                        </div>
                                        <div className="flex items-center text-sm">
                                            <FileText className="h-4 w-4 mr-2 text-neutral-500" />
                                            <span className="text-neutral-600 dark:text-neutral-400">
                                                Refills: <span className="font-medium text-neutral-900 dark:text-neutral-100">{prescription.refills} remaining</span>
                                            </span>
                                        </div>
                                    </div>

                                    <div className="bg-blue-50 dark:bg-blue-900/20 rounded-lg p-3 mb-4">
                                        <p className="text-sm text-neutral-700 dark:text-neutral-300">
                                            <span className="font-medium">Instructions:</span> {prescription.instructions}
                                        </p>
                                    </div>

                                    <div className="flex items-center justify-between pt-4 border-t border-neutral-200 dark:border-neutral-600">
                                        <div className="text-xs text-neutral-500 dark:text-neutral-400">
                                            Prescribed on {prescription.prescribedDate}
                                        </div>
                                        <div className="flex items-center space-x-2">
                                            <button className="btn-outline btn-sm flex items-center space-x-1">
                                                <Printer className="h-4 w-4" />
                                                <span>Print</span>
                                            </button>
                                            <button className="btn-outline btn-sm p-2">
                                                <Edit className="h-4 w-4" />
                                            </button>
                                            <button className="btn-outline btn-sm p-2 text-red-600 hover:text-red-700">
                                                <Trash2 className="h-4 w-4" />
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

export default withAuth(PrescriptionsPage, ["DOCTOR", "ADMIN"]);
