"use client";

import React, { useState } from "react";
import {
  Calendar,
  Users,
  FileText,
  Clock,
  CheckCircle,
  AlertCircle,
  Activity,
  Clipboard,
  Heart,
  Pill,
  Video,
  Phone,
  Mail,
  ChevronRight,
  Plus,
  Search,
  Filter,
  Bell,
  Stethoscope,
  User,
  TrendingUp,
} from "lucide-react";

interface StatCardProps {
  title: string;
  value: string | number;
  subtitle?: string;
  icon: React.ElementType;
  color: string;
  badge?: string;
}

const StatCard: React.FC<StatCardProps> = ({
  title,
  value,
  subtitle,
  icon: Icon,
  color,
  badge,
}) => {
  return (
    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
      <div className="flex items-center justify-between mb-4">
        <div className={`${color} p-3 rounded-lg`}>
          <Icon className="h-6 w-6 text-white" />
        </div>
        {badge && (
          <span className="bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 text-xs font-semibold px-2.5 py-1 rounded">
            {badge}
          </span>
        )}
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
    patientName: string;
    time: string;
    type: string;
    status: "SCHEDULED" | "IN_PROGRESS" | "COMPLETED";
    reason: string;
    isUrgent?: boolean;
  };
}

const AppointmentCard: React.FC<AppointmentCardProps> = ({ appointment }) => {
  const getStatusColor = () => {
    switch (appointment.status) {
      case "SCHEDULED":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200";
      case "IN_PROGRESS":
        return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200";
      case "COMPLETED":
        return "bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200";
    }
  };

  const getStatusText = () => {
    switch (appointment.status) {
      case "SCHEDULED":
        return "Scheduled";
      case "IN_PROGRESS":
        return "In Progress";
      case "COMPLETED":
        return "Completed";
    }
  };

  return (
    <div className="bg-white dark:bg-neutral-800 border border-neutral-200 dark:border-neutral-700 rounded-lg p-4 hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between mb-3">
        <div className="flex-1">
          <div className="flex items-center space-x-2 mb-1">
            <h3 className="font-semibold text-neutral-900 dark:text-neutral-100">
              {appointment.patientName}
            </h3>
            {appointment.isUrgent && (
              <span className="bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200 text-xs font-semibold px-2 py-0.5 rounded">
                URGENT
              </span>
            )}
          </div>
          <div className="flex items-center text-sm text-neutral-600 dark:text-neutral-400 space-x-3">
            <span className="flex items-center">
              <Clock className="h-3 w-3 mr-1" />
              {appointment.time}
            </span>
            <span className="flex items-center">
              <Clipboard className="h-3 w-3 mr-1" />
              {appointment.type}
            </span>
          </div>
        </div>
        <span
          className={`text-xs font-medium px-2.5 py-1 rounded ${getStatusColor()}`}
        >
          {getStatusText()}
        </span>
      </div>
      <p className="text-sm text-neutral-700 dark:text-neutral-300 mb-3">
        <span className="font-medium">Reason:</span> {appointment.reason}
      </p>
      <div className="flex items-center space-x-2">
        <button className="btn-primary btn-sm flex-1 text-xs">
          View Details
        </button>
        {appointment.status === "SCHEDULED" && (
          <button className="btn-outline btn-sm text-xs">
            Start Consultation
          </button>
        )}
        {appointment.status === "IN_PROGRESS" && (
          <button className="btn-success btn-sm text-xs">Complete</button>
        )}
      </div>
    </div>
  );
};

interface PatientCardProps {
  patient: {
    id: number;
    name: string;
    age: number;
    lastVisit: string;
    condition: string;
    priority: "high" | "medium" | "low";
  };
}

const PatientCard: React.FC<PatientCardProps> = ({ patient }) => {
  const getPriorityColor = () => {
    switch (patient.priority) {
      case "high":
        return "border-l-4 border-l-red-500";
      case "medium":
        return "border-l-4 border-l-yellow-500";
      case "low":
        return "border-l-4 border-l-green-500";
    }
  };

  return (
    <div
      className={`bg-white dark:bg-neutral-800 rounded-lg p-4 hover:shadow-md transition-shadow ${getPriorityColor()}`}
    >
      <div className="flex items-start justify-between mb-2">
        <div className="flex items-center space-x-3">
          <div className="bg-blue-100 dark:bg-blue-900 p-2 rounded-full">
            <User className="h-5 w-5 text-blue-600 dark:text-blue-300" />
          </div>
          <div>
            <h4 className="font-semibold text-neutral-900 dark:text-neutral-100">
              {patient.name}
            </h4>
            <p className="text-sm text-neutral-600 dark:text-neutral-400">
              Age: {patient.age}
            </p>
          </div>
        </div>
        <span className="text-xs text-neutral-500 dark:text-neutral-400">
          {patient.lastVisit}
        </span>
      </div>
      <p className="text-sm text-neutral-700 dark:text-neutral-300 mb-3">
        <span className="font-medium">Condition:</span> {patient.condition}
      </p>
      <div className="flex items-center space-x-2">
        <button className="btn-outline btn-sm flex-1 text-xs">
          View Records
        </button>
        <button className="btn-sm bg-green-600 hover:bg-green-700 text-white text-xs">
          <Phone className="h-3 w-3" />
        </button>
        <button className="btn-sm bg-blue-600 hover:bg-blue-700 text-white text-xs">
          <Mail className="h-3 w-3" />
        </button>
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

const DoctorDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState<"today" | "upcoming" | "past">(
    "today",
  );

  // Mock data
  const stats = {
    todayAppointments: 8,
    pendingConsultations: 3,
    completedToday: 5,
    totalPatients: 142,
  };

  const todayAppointments = [
    {
      id: 1,
      patientName: "Sarah Johnson",
      time: "09:00 AM",
      type: "Follow-up",
      status: "COMPLETED" as const,
      reason: "Post-surgery checkup",
    },
    {
      id: 2,
      patientName: "Michael Brown",
      time: "10:30 AM",
      type: "Consultation",
      status: "IN_PROGRESS" as const,
      reason: "Chest pain and breathing difficulty",
      isUrgent: true,
    },
    {
      id: 3,
      patientName: "Emily Davis",
      time: "11:30 AM",
      type: "Routine Checkup",
      status: "SCHEDULED" as const,
      reason: "Annual health screening",
    },
    {
      id: 4,
      patientName: "James Wilson",
      time: "02:00 PM",
      type: "Consultation",
      status: "SCHEDULED" as const,
      reason: "Persistent headaches",
    },
    {
      id: 5,
      patientName: "Linda Martinez",
      time: "03:30 PM",
      type: "Follow-up",
      status: "SCHEDULED" as const,
      reason: "Diabetes management review",
    },
  ];

  const recentPatients = [
    {
      id: 1,
      name: "Robert Chen",
      age: 45,
      lastVisit: "2 days ago",
      condition: "Hypertension",
      priority: "high" as const,
    },
    {
      id: 2,
      name: "Maria Garcia",
      age: 32,
      lastVisit: "1 week ago",
      condition: "Migraine",
      priority: "medium" as const,
    },
    {
      id: 3,
      name: "David Kim",
      age: 58,
      lastVisit: "3 days ago",
      condition: "Type 2 Diabetes",
      priority: "high" as const,
    },
  ];

  const upcomingTasks = [
    { id: 1, task: "Review lab results for John Doe", time: "Today, 1:00 PM" },
    { id: 2, task: "Prepare surgery report", time: "Tomorrow, 9:00 AM" },
    {
      id: 3,
      task: "Team meeting - Cardiology Dept",
      time: "Tomorrow, 3:00 PM",
    },
    { id: 4, task: "Review patient discharge forms", time: "Friday, 10:00 AM" },
  ];

  const handleQuickAction = (action: string) => {
    console.log(`Quick action: ${action}`);
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-gradient-to-r from-teal-600 to-teal-800 rounded-lg shadow-lg p-6 text-white">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold flex items-center">
              <Stethoscope className="h-8 w-8 mr-3" />
              Doctor Dashboard
            </h1>
            <p className="mt-2 text-teal-100">
              Manage your appointments and patient care
            </p>
          </div>
          <div className="hidden lg:flex items-center space-x-4">
            <button className="bg-white/20 hover:bg-white/30 px-4 py-2 rounded-lg transition-colors flex items-center space-x-2">
              <Video className="h-4 w-4" />
              <span>Video Call</span>
            </button>
            <button className="bg-white/20 hover:bg-white/30 px-4 py-2 rounded-lg transition-colors flex items-center space-x-2">
              <Bell className="h-4 w-4" />
              <span>Notifications</span>
            </button>
          </div>
        </div>
      </div>

      {/* Key Statistics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="Today's Appointments"
          value={stats.todayAppointments}
          subtitle={`${stats.pendingConsultations} pending`}
          icon={Calendar}
          color="bg-blue-500"
          badge="Today"
        />
        <StatCard
          title="Completed Today"
          value={stats.completedToday}
          subtitle="Great progress!"
          icon={CheckCircle}
          color="bg-green-500"
        />
        <StatCard
          title="Active Patients"
          value={stats.totalPatients}
          subtitle="Under your care"
          icon={Users}
          color="bg-purple-500"
        />
        <StatCard
          title="Pending Reviews"
          value={12}
          subtitle="Lab reports & tests"
          icon={FileText}
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
            title="New Prescription"
            icon={Pill}
            color="bg-blue-500"
            onClick={() => handleQuickAction("new-prescription")}
          />
          <QuickAction
            title="Lab Request"
            icon={Activity}
            color="bg-green-500"
            onClick={() => handleQuickAction("lab-request")}
          />
          <QuickAction
            title="Write Notes"
            icon={FileText}
            color="bg-purple-500"
            onClick={() => handleQuickAction("write-notes")}
          />
          <QuickAction
            title="Patient Records"
            icon={Clipboard}
            color="bg-orange-500"
            onClick={() => handleQuickAction("patient-records")}
          />
          <QuickAction
            title="Video Call"
            icon={Video}
            color="bg-teal-500"
            onClick={() => handleQuickAction("video-call")}
          />
          <QuickAction
            title="Schedule"
            icon={Calendar}
            color="bg-pink-500"
            onClick={() => handleQuickAction("schedule")}
          />
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Today's Appointments */}
        <div className="lg:col-span-2 bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 flex items-center">
              <Calendar className="h-5 w-5 mr-2" />
              Today's Appointments
            </h2>
            <div className="flex items-center space-x-2">
              <button className="text-sm text-blue-600 hover:text-blue-700 dark:text-blue-400">
                View Calendar
              </button>
            </div>
          </div>

          {/* Tabs */}
          <div className="flex space-x-2 mb-4 border-b border-neutral-200 dark:border-neutral-700">
            <button
              onClick={() => setActiveTab("today")}
              className={`pb-2 px-3 text-sm font-medium transition-colors ${
                activeTab === "today"
                  ? "border-b-2 border-blue-600 text-blue-600"
                  : "text-neutral-600 dark:text-neutral-400"
              }`}
            >
              Today ({todayAppointments.length})
            </button>
            <button
              onClick={() => setActiveTab("upcoming")}
              className={`pb-2 px-3 text-sm font-medium transition-colors ${
                activeTab === "upcoming"
                  ? "border-b-2 border-blue-600 text-blue-600"
                  : "text-neutral-600 dark:text-neutral-400"
              }`}
            >
              Upcoming (4)
            </button>
            <button
              onClick={() => setActiveTab("past")}
              className={`pb-2 px-3 text-sm font-medium transition-colors ${
                activeTab === "past"
                  ? "border-b-2 border-blue-600 text-blue-600"
                  : "text-neutral-600 dark:text-neutral-400"
              }`}
            >
              Past
            </button>
          </div>

          <div className="space-y-3 max-h-[600px] overflow-y-auto">
            {todayAppointments.map((appointment) => (
              <AppointmentCard key={appointment.id} appointment={appointment} />
            ))}
          </div>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Recent Patients */}
          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 flex items-center">
                <Heart className="h-5 w-5 mr-2" />
                Recent Patients
              </h2>
              <button className="text-sm text-blue-600 hover:text-blue-700 dark:text-blue-400">
                View All
              </button>
            </div>
            <div className="space-y-3">
              {recentPatients.map((patient) => (
                <PatientCard key={patient.id} patient={patient} />
              ))}
            </div>
          </div>

          {/* Upcoming Tasks */}
          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <h2 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-4 flex items-center">
              <Clipboard className="h-5 w-5 mr-2" />
              Upcoming Tasks
            </h2>
            <div className="space-y-3">
              {upcomingTasks.map((task) => (
                <div
                  key={task.id}
                  className="flex items-start space-x-3 p-3 hover:bg-neutral-50 dark:hover:bg-neutral-700 rounded-lg transition-colors"
                >
                  <div className="flex-shrink-0 mt-1">
                    <div className="h-2 w-2 bg-blue-500 rounded-full"></div>
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-neutral-900 dark:text-neutral-100">
                      {task.task}
                    </p>
                    <p className="text-xs text-neutral-500 dark:text-neutral-400 mt-1">
                      {task.time}
                    </p>
                  </div>
                </div>
              ))}
            </div>
            <button className="btn-outline btn-sm w-full mt-4">
              View All Tasks
            </button>
          </div>
        </div>
      </div>

      {/* Performance Metrics */}
      <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
        <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4 flex items-center">
          <TrendingUp className="h-5 w-5 mr-2" />
          This Month's Performance
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="border border-neutral-200 dark:border-neutral-700 rounded-lg p-4">
            <p className="text-sm text-neutral-600 dark:text-neutral-400 mb-1">
              Total Consultations
            </p>
            <p className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
              156
            </p>
            <p className="text-xs text-green-600 mt-1">+12% vs last month</p>
          </div>
          <div className="border border-neutral-200 dark:border-neutral-700 rounded-lg p-4">
            <p className="text-sm text-neutral-600 dark:text-neutral-400 mb-1">
              Patient Satisfaction
            </p>
            <p className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
              4.8/5
            </p>
            <p className="text-xs text-green-600 mt-1">+0.3 vs last month</p>
          </div>
          <div className="border border-neutral-200 dark:border-neutral-700 rounded-lg p-4">
            <p className="text-sm text-neutral-600 dark:text-neutral-400 mb-1">
              Avg. Consultation Time
            </p>
            <p className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
              28 min
            </p>
            <p className="text-xs text-neutral-500 mt-1">
              Within optimal range
            </p>
          </div>
          <div className="border border-neutral-200 dark:border-neutral-700 rounded-lg p-4">
            <p className="text-sm text-neutral-600 dark:text-neutral-400 mb-1">
              Follow-up Rate
            </p>
            <p className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
              92%
            </p>
            <p className="text-xs text-green-600 mt-1">+5% vs last month</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DoctorDashboard;
