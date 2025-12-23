"use client";

import React, { useState } from "react";
import {
    Calendar,
    FileText,
    Clock,
    CheckCircle,
    AlertCircle,
    Heart,
    Pill,
    Activity,
    User,
    Phone,
    Mail,
    MapPin,
    Download,
    Plus,
    Video,
    MessageSquare,
    TrendingUp,
    Bell,
    ChevronRight,
    Clipboard,
    DollarSign,
    FileCheck,
    Stethoscope,
} from "lucide-react";

interface StatCardProps {
    title: string;
    value: string | number;
    subtitle?: string;
    icon: React.ElementType;
    color: string;
}

const StatCard: React.FC<StatCardProps> = ({
    title,
    value,
    subtitle,
    icon: Icon,
    color,
}) => {
    return (
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
            <div className="flex items-center justify-between mb-4">
                <div className={`${color} p-3 rounded-lg`}>
                    <Icon className="h-6 w-6 text-white" />
                </div>
            </div>
            <h3 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
                {value}
            </h3>
            <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400 mt-1">
                {title}
            </p>
            {subtitle && (
                <p className="text-xs text-neutral-500 dark:text-neutral-500 mt-1">
                    {subtitle}
                </p>
            )}
        </div>
    );
};

interface AppointmentCardProps {
    appointment: {
        id: number;
        doctorName: string;
        specialty: string;
        date: string;
        time: string;
        type: string;
        status: "SCHEDULED" | "CONFIRMED" | "COMPLETED" | "CANCELLED";
        location: string;
    };
}

const AppointmentCard: React.FC<AppointmentCardProps> = ({ appointment }) => {
    const getStatusColor = () => {
        switch (appointment.status) {
            case "SCHEDULED":
                return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200";
            case "CONFIRMED":
                return "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200";
            case "COMPLETED":
                return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200";
            case "CANCELLED":
                return "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200";
        }
    };

    const getStatusText = () => {
        switch (appointment.status) {
            case "SCHEDULED":
                return "Scheduled";
            case "CONFIRMED":
                return "Confirmed";
            case "COMPLETED":
                return "Completed";
            case "CANCELLED":
                return "Cancelled";
        }
    };

    const getStatusIcon = () => {
        switch (appointment.status) {
            case "SCHEDULED":
                return <Clock className="h-4 w-4" />;
            case "CONFIRMED":
                return <CheckCircle className="h-4 w-4" />;
            case "COMPLETED":
                return <CheckCircle className="h-4 w-4" />;
            case "CANCELLED":
                return <AlertCircle className="h-4 w-4" />;
        }
    };

    return (
        <div className="bg-white dark:bg-neutral-800 border border-neutral-200 dark:border-neutral-700 rounded-lg p-5 hover:shadow-md transition-shadow">
            <div className="flex items-start justify-between mb-4">
                <div className="flex-1">
                    <h3 className="font-semibold text-lg text-neutral-900 dark:text-neutral-100 mb-1">
                        {appointment.doctorName}
                    </h3>
                    <p className="text-sm text-neutral-600 dark:text-neutral-400">
                        {appointment.specialty}
                    </p>
                </div>
                <span
                    className={`flex items-center space-x-1 text-xs font-medium px-2.5 py-1.5 rounded ${getStatusColor()}`}
                >
                    {getStatusIcon()}
                    <span>{getStatusText()}</span>
                </span>
            </div>

            <div className="space-y-2 mb-4">
                <div className="flex items-center text-sm text-neutral-700 dark:text-neutral-300">
                    <Calendar className="h-4 w-4 mr-2 text-neutral-500" />
                    <span>{appointment.date}</span>
                    <Clock className="h-4 w-4 ml-4 mr-2 text-neutral-500" />
                    <span>{appointment.time}</span>
                </div>
                <div className="flex items-center text-sm text-neutral-700 dark:text-neutral-300">
                    <MapPin className="h-4 w-4 mr-2 text-neutral-500" />
                    <span>{appointment.location}</span>
                </div>
                <div className="flex items-center text-sm text-neutral-700 dark:text-neutral-300">
                    <Clipboard className="h-4 w-4 mr-2 text-neutral-500" />
                    <span>{appointment.type}</span>
                </div>
            </div>

            <div className="flex items-center space-x-2">
                {appointment.status === "SCHEDULED" && (
                    <>
                        <button className="btn-primary btn-sm flex-1 text-xs">
                            Join Video Call
                        </button>
                        <button className="btn-outline btn-sm text-xs">
                            Reschedule
                        </button>
                        <button className="btn-outline btn-sm text-xs text-red-600 hover:text-red-700">
                            Cancel
                        </button>
                    </>
                )}
                {appointment.status === "CONFIRMED" && (
                    <>
                        <button className="btn-primary btn-sm flex-1 text-xs">
                            <Video className="h-3 w-3 mr-1" />
                            Join Call
                        </button>
                        <button className="btn-outline btn-sm text-xs">
                            View Details
                        </button>
                    </>
                )}
                {appointment.status === "COMPLETED" && (
                    <>
                        <button className="btn-outline btn-sm flex-1 text-xs">
                            View Summary
                        </button>
                        <button className="btn-outline btn-sm text-xs">
                            Download
                        </button>
                    </>
                )}
                {appointment.status === "CANCELLED" && (
                    <button className="btn-primary btn-sm flex-1 text-xs">
                        Book New Appointment
                    </button>
                )}
            </div>
        </div>
    );
};

interface MedicalRecordCardProps {
    record: {
        id: number;
        type: string;
        date: string;
        doctor: string;
        summary: string;
    };
}

const MedicalRecordCard: React.FC<MedicalRecordCardProps> = ({ record }) => {
    return (
        <div className="border-b border-neutral-200 dark:border-neutral-700 last:border-0 pb-4 last:pb-0">
            <div className="flex items-start justify-between mb-2">
                <div className="flex-1">
                    <h4 className="font-medium text-neutral-900 dark:text-neutral-100">
                        {record.type}
                    </h4>
                    <p className="text-sm text-neutral-600 dark:text-neutral-400 mt-1">
                        {record.doctor}
                    </p>
                </div>
                <span className="text-xs text-neutral-500 dark:text-neutral-400">
                    {record.date}
                </span>
            </div>
            <p className="text-sm text-neutral-700 dark:text-neutral-300 mb-2">
                {record.summary}
            </p>
            <button className="text-xs text-blue-600 hover:text-blue-700 dark:text-blue-400 flex items-center">
                View Full Report
                <ChevronRight className="h-3 w-3 ml-1" />
            </button>
        </div>
    );
};

interface PrescriptionCardProps {
    prescription: {
        id: number;
        medication: string;
        dosage: string;
        frequency: string;
        prescribedBy: string;
        startDate: string;
        endDate: string;
        status: "active" | "completed";
    };
}

const PrescriptionCard: React.FC<PrescriptionCardProps> = ({ prescription }) => {
    return (
        <div className="bg-white dark:bg-neutral-800 border border-neutral-200 dark:border-neutral-700 rounded-lg p-4">
            <div className="flex items-start justify-between mb-3">
                <div className="flex items-start space-x-3">
                    <div className="bg-purple-100 dark:bg-purple-900 p-2 rounded-lg">
                        <Pill className="h-5 w-5 text-purple-600 dark:text-purple-300" />
                    </div>
                    <div>
                        <h4 className="font-semibold text-neutral-900 dark:text-neutral-100">
                            {prescription.medication}
                        </h4>
                        <p className="text-sm text-neutral-600 dark:text-neutral-400 mt-1">
                            {prescription.dosage} - {prescription.frequency}
                        </p>
                    </div>
                </div>
                <span
                    className={`text-xs font-medium px-2 py-1 rounded ${
                        prescription.status === "active"
                            ? "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200"
                            : "bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200"
                    }`}
                >
                    {prescription.status === "active" ? "Active" : "Completed"}
                </span>
            </div>
            <div className="text-xs text-neutral-600 dark:text-neutral-400 space-y-1">
                <p>Prescribed by: {prescription.prescribedBy}</p>
                <p>
                    Duration: {prescription.startDate} - {prescription.endDate}
                </p>
            </div>
        </div>
    );
};

interface QuickActionProps {
    title: string;
    icon: React.ElementType;
    color: string;
    onClick: () => void;
}

const QuickAction: React.FC<QuickActionProps> = ({
    title,
    icon: Icon,
    color,
    onClick,
}) => {
    return (
        <button
            onClick={onClick}
            className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4 hover:shadow-lg transition-all hover:scale-105 text-center"
        >
            <div className={`${color} p-3 rounded-lg mx-auto w-fit mb-2`}>
                <Icon className="h-6 w-6 text-white" />
            </div>
            <span className="text-sm font-medium text-neutral-900 dark:text-neutral-100">
                {title}
            </span>
        </button>
    );
};

const PatientDashboard: React.FC = () => {
    const [activeTab, setActiveTab] = useState<"upcoming" | "past">("upcoming");

    // Mock data
    const stats = {
        upcomingAppointments: 2,
        completedAppointments: 15,
        activePrescriptions: 3,
        medicalRecords: 8,
    };

    const upcomingAppointments = [
        {
            id: 1,
            doctorName: "Dr. Sarah Johnson",
            specialty: "Cardiologist",
            date: "Nov 25, 2024",
            time: "10:00 AM",
            type: "Follow-up Consultation",
            status: "CONFIRMED" as const,
            location: "Cardiology Dept, 3rd Floor",
        },
        {
            id: 2,
            doctorName: "Dr. Michael Chen",
            specialty: "General Physician",
            date: "Nov 28, 2024",
            time: "02:30 PM",
            type: "Routine Checkup",
            status: "SCHEDULED" as const,
            location: "OPD Block A, Room 203",
        },
    ];

    const pastAppointments = [
        {
            id: 3,
            doctorName: "Dr. Emily Davis",
            specialty: "Dermatologist",
            date: "Nov 15, 2024",
            time: "11:00 AM",
            type: "Consultation",
            status: "COMPLETED" as const,
            location: "Dermatology Wing",
        },
        {
            id: 4,
            doctorName: "Dr. Robert Wilson",
            specialty: "Orthopedic",
            date: "Nov 10, 2024",
            time: "03:00 PM",
            type: "Follow-up",
            status: "COMPLETED" as const,
            location: "Orthopedic Center",
        },
    ];

    const recentRecords = [
        {
            id: 1,
            type: "Blood Test Results",
            date: "Nov 18, 2024",
            doctor: "Dr. Sarah Johnson",
            summary: "All parameters within normal range. Cholesterol slightly elevated.",
        },
        {
            id: 2,
            type: "X-Ray Report",
            date: "Nov 12, 2024",
            doctor: "Dr. Robert Wilson",
            summary: "Knee joint showing signs of improvement. Continue physiotherapy.",
        },
        {
            id: 3,
            type: "ECG Report",
            date: "Nov 5, 2024",
            doctor: "Dr. Sarah Johnson",
            summary: "Normal sinus rhythm. Heart function stable.",
        },
    ];

    const activePrescriptions = [
        {
            id: 1,
            medication: "Aspirin",
            dosage: "75mg",
            frequency: "Once daily",
            prescribedBy: "Dr. Sarah Johnson",
            startDate: "Nov 1, 2024",
            endDate: "Dec 1, 2024",
            status: "active" as const,
        },
        {
            id: 2,
            medication: "Lisinopril",
            dosage: "10mg",
            frequency: "Once daily (morning)",
            prescribedBy: "Dr. Sarah Johnson",
            startDate: "Oct 15, 2024",
            endDate: "Dec 15, 2024",
            status: "active" as const,
        },
        {
            id: 3,
            medication: "Vitamin D3",
            dosage: "1000 IU",
            frequency: "Once daily",
            prescribedBy: "Dr. Emily Davis",
            startDate: "Nov 15, 2024",
            endDate: "Feb 15, 2025",
            status: "active" as const,
        },
    ];

    const healthMetrics = [
        { label: "Blood Pressure", value: "120/80 mmHg", status: "normal", icon: Heart },
        { label: "Heart Rate", value: "72 bpm", status: "normal", icon: Activity },
        { label: "Blood Sugar", value: "95 mg/dL", status: "normal", icon: Activity },
        { label: "Weight", value: "70 kg", status: "normal", icon: TrendingUp },
    ];

    const handleQuickAction = (action: string) => {
        console.log(`Quick action: ${action}`);
    };

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="bg-gradient-to-r from-purple-600 to-purple-800 rounded-lg shadow-lg p-6 text-white">
                <div className="flex items-center justify-between">
                    <div>
                        <h1 className="text-3xl font-bold flex items-center">
                            <User className="h-8 w-8 mr-3" />
                            My Health Dashboard
                        </h1>
                        <p className="mt-2 text-purple-100">
                            Track your appointments, prescriptions, and medical records
                        </p>
                    </div>
                    <div className="hidden lg:flex items-center space-x-4">
                        <button className="bg-white/20 hover:bg-white/30 px-4 py-2 rounded-lg transition-colors flex items-center space-x-2">
                            <MessageSquare className="h-4 w-4" />
                            <span>Message Doctor</span>
                        </button>
                        <button className="bg-white/20 hover:bg-white/30 px-4 py-2 rounded-lg transition-colors flex items-center space-x-2">
                            <Bell className="h-4 w-4" />
                            <span>Reminders</span>
                        </button>
                    </div>
                </div>
            </div>

            {/* Key Statistics */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <StatCard
                    title="Upcoming Appointments"
                    value={stats.upcomingAppointments}
                    subtitle="Next on Nov 25"
                    icon={Calendar}
                    color="bg-blue-500"
                />
                <StatCard
                    title="Active Prescriptions"
                    value={stats.activePrescriptions}
                    subtitle="Don't forget to take your meds"
                    icon={Pill}
                    color="bg-purple-500"
                />
                <StatCard
                    title="Medical Records"
                    value={stats.medicalRecords}
                    subtitle="View your health history"
                    icon={FileText}
                    color="bg-green-500"
                />
                <StatCard
                    title="Completed Visits"
                    value={stats.completedAppointments}
                    subtitle="This year"
                    icon={CheckCircle}
                    color="bg-orange-500"
                />
            </div>

            {/* Quick Actions */}
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4 flex items-center">
                    <Plus className="h-5 w-5 mr-2" />
                    Quick Actions
                </h2>
                <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
                    <QuickAction
                        title="Book Appointment"
                        icon={Calendar}
                        color="bg-blue-500"
                        onClick={() => handleQuickAction("book-appointment")}
                    />
                    <QuickAction
                        title="View Records"
                        icon={FileText}
                        color="bg-green-500"
                        onClick={() => handleQuickAction("view-records")}
                    />
                    <QuickAction
                        title="Prescriptions"
                        icon={Pill}
                        color="bg-purple-500"
                        onClick={() => handleQuickAction("prescriptions")}
                    />
                    <QuickAction
                        title="Lab Reports"
                        icon={Activity}
                        color="bg-orange-500"
                        onClick={() => handleQuickAction("lab-reports")}
                    />
                    <QuickAction
                        title="Pay Bills"
                        icon={DollarSign}
                        color="bg-teal-500"
                        onClick={() => handleQuickAction("pay-bills")}
                    />
                    <QuickAction
                        title="Contact Support"
                        icon={Phone}
                        color="bg-pink-500"
                        onClick={() => handleQuickAction("contact-support")}
                    />
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Appointments */}
                <div className="lg:col-span-2 bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                    <div className="flex items-center justify-between mb-4">
                        <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 flex items-center">
                            <Calendar className="h-5 w-5 mr-2" />
                            My Appointments
                        </h2>
                        <button className="btn-primary btn-sm flex items-center space-x-1">
                            <Plus className="h-4 w-4" />
                            <span>Book New</span>
                        </button>
                    </div>

                    {/* Tabs */}
                    <div className="flex space-x-2 mb-4 border-b border-neutral-200 dark:border-neutral-700">
                        <button
                            onClick={() => setActiveTab("upcoming")}
                            className={`pb-2 px-3 text-sm font-medium transition-colors ${
                                activeTab === "upcoming"
                                    ? "border-b-2 border-blue-600 text-blue-600"
                                    : "text-neutral-600 dark:text-neutral-400"
                            }`}
                        >
                            Upcoming ({upcomingAppointments.length})
                        </button>
                        <button
                            onClick={() => setActiveTab("past")}
                            className={`pb-2 px-3 text-sm font-medium transition-colors ${
                                activeTab === "past"
                                    ? "border-b-2 border-blue-600 text-blue-600"
                                    : "text-neutral-600 dark:text-neutral-400"
                            }`}
                        >
                            Past ({pastAppointments.length})
                        </button>
                    </div>

                    <div className="space-y-4">
                        {activeTab === "upcoming" &&
                            upcomingAppointments.map((appointment) => (
                                <AppointmentCard
                                    key={appointment.id}
                                    appointment={appointment}
                                />
                            ))}
                        {activeTab === "past" &&
                            pastAppointments.map((appointment) => (
                                <AppointmentCard
                                    key={appointment.id}
                                    appointment={appointment}
                                />
                            ))}
                    </div>
                </div>

                {/* Sidebar */}
                <div className="space-y-6">
                    {/* Health Metrics */}
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <h2 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-4 flex items-center">
                            <Heart className="h-5 w-5 mr-2" />
                            Health Metrics
                        </h2>
                        <div className="space-y-3">
                            {healthMetrics.map((metric, index) => (
                                <div
                                    key={index}
                                    className="flex items-center justify-between p-3 bg-neutral-50 dark:bg-neutral-700 rounded-lg"
                                >
                                    <div className="flex items-center space-x-3">
                                        <metric.icon className="h-4 w-4 text-neutral-600 dark:text-neutral-400" />
                                        <div>
                                            <p className="text-sm font-medium text-neutral-900 dark:text-neutral-100">
                                                {metric.label}
                                            </p>
                                            <p className="text-xs text-neutral-600 dark:text-neutral-400">
                                                {metric.value}
                                            </p>
                                        </div>
                                    </div>
                                    <span className="text-xs font-medium text-green-600">
                                        Normal
                                    </span>
                                </div>
                            ))}
                        </div>
                        <button className="btn-outline btn-sm w-full mt-4">
                            Update Metrics
                        </button>
                    </div>

                    {/* Contact Info */}
                    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                        <h2 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                            Emergency Contact
                        </h2>
                        <div className="space-y-3">
                            <div className="flex items-center space-x-3 p-3 bg-red-50 dark:bg-red-900/20 rounded-lg">
                                <Phone className="h-4 w-4 text-red-600" />
                                <div>
                                    <p className="text-sm font-medium text-neutral-900 dark:text-neutral-100">
                                        Emergency Hotline
                                    </p>
                                    <p className="text-xs text-neutral-600 dark:text-neutral-400">
                                        +1 (555) 911-1234
                                    </p>
                                </div>
                            </div>
                            <div className="flex items-center space-x-3 p-3 bg-blue-50 dark:bg-blue-900/20 rounded-lg">
                                <Stethoscope className="h-4 w-4 text-blue-600" />
                                <div>
                                    <p className="text-sm font-medium text-neutral-900 dark:text-neutral-100">
                                        Primary Care Doctor
                                    </p>
                                    <p className="text-xs text-neutral-600 dark:text-neutral-400">
                                        Dr. Sarah Johnson
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {/* Active Prescriptions */}
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                    <div className="flex items-center justify-between mb-4">
                        <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 flex items-center">
                            <Pill className="h-5 w-5 mr-2" />
                            Active Prescriptions
                        </h2>
                        <button className="text-sm text-blue-600 hover:text-blue-700 dark:text-blue-400 flex items-center">
                            View All
                            <ChevronRight className="h-4 w-4 ml-1" />
                        </button>
                    </div>
                    <div className="space-y-3">
                        {activePrescriptions.map((prescription) => (
                            <PrescriptionCard
                                key={prescription.id}
                                prescription={prescription}
                            />
                        ))}
                    </div>
                </div>

                {/* Recent Medical Records */}
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
                    <div className="flex items-center justify-between mb-4">
                        <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 flex items-center">
                            <FileText className="h-5 w-5 mr-2" />
                            Recent Records
                        </h2>
                        <button className="text-sm text-blue-600 hover:text-blue-700 dark:text-blue-400 flex items-center">
                            View All
                            <ChevronRight className="h-4 w-4 ml-1" />
                        </button>
                    </div>
                    <div className="space-y-4">
                        {recentRecords.map((record) => (
                            <MedicalRecordCard key={record.id} record={record} />
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default PatientDashboard;
