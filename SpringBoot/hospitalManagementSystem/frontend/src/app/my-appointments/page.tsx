"use client";

import React, { useState, useEffect } from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  Calendar,
  Clock,
  Plus,
  Search,
  Filter,
  X,
  CheckCircle,
  AlertCircle,
  XCircle,
  Video,
  MapPin,
  User,
  Stethoscope,
  FileText,
  Edit,
  Trash2,
} from "lucide-react";
import { appointmentsApi } from "@/services/api";

interface Appointment {
  id: number;
  patientId: number;
  doctorId: number;
  patientName?: string;
  doctorName?: string;
  appointmentDateTime: string;
  status: string;
  reason: string;
  duration?: number;
  notes?: string;
  type?: string;
  location?: string;
}

function MyAppointmentsPage() {
  const { user } = useAuth();
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [selectedAppointment, setSelectedAppointment] =
    useState<Appointment | null>(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [formData, setFormData] = useState({
    doctorId: "",
    appointmentDateTime: "",
    reason: "",
    notes: "",
    type: "IN_PERSON",
  });

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      setLoading(true);
      setError("");
      if (!user?.id) {
        setError("User not found");
        return;
      }
      const data = await appointmentsApi.getByPatient(user.id);
      setAppointments(data);
    } catch (err) {
      console.error("Error fetching appointments:", err);
      setError("Failed to load appointments");
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (
      !formData.doctorId ||
      !formData.appointmentDateTime ||
      !formData.reason
    ) {
      setError("Please fill in all required fields");
      return;
    }

    try {
      const newAppointment = {
        patientId: user?.id || 0,
        doctorId: parseInt(formData.doctorId),
        appointmentDateTime: formData.appointmentDateTime,
        reason: formData.reason,
        notes: formData.notes,
        status: "SCHEDULED",
        type: formData.type,
      };

      await appointmentsApi.create(newAppointment);
      setSuccess("Appointment scheduled successfully!");
      setShowCreateModal(false);
      resetForm();
      fetchAppointments();
    } catch (err) {
      console.error("Error creating appointment:", err);
      setError("Failed to schedule appointment");
    }
  };

  const handleCancel = async (id: number) => {
    if (!window.confirm("Are you sure you want to cancel this appointment?")) {
      return;
    }

    try {
      setError("");
      setSuccess("");
      await appointmentsApi.cancel(id);
      setSuccess("Appointment cancelled successfully!");
      fetchAppointments();
    } catch (err) {
      console.error("Error cancelling appointment:", err);
      setError("Failed to cancel appointment");
    }
  };

  const resetForm = () => {
    setFormData({
      doctorId: "",
      appointmentDateTime: "",
      reason: "",
      notes: "",
      type: "IN_PERSON",
    });
  };

  const openDetailsModal = (appointment: Appointment) => {
    setSelectedAppointment(appointment);
    setShowDetailsModal(true);
  };

  const filteredAppointments = appointments.filter((appointment) => {
    const matchesSearch =
      appointment.doctorName
        ?.toLowerCase()
        .includes(searchQuery.toLowerCase()) ||
      appointment.reason?.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesStatus =
      statusFilter === "ALL" || appointment.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const upcomingAppointments = filteredAppointments.filter((apt) => {
    const aptDate = new Date(apt.appointmentDateTime);
    return (
      aptDate >= new Date() &&
      apt.status !== "CANCELLED" &&
      apt.status !== "COMPLETED"
    );
  });

  const pastAppointments = filteredAppointments.filter((apt) => {
    const aptDate = new Date(apt.appointmentDateTime);
    return aptDate < new Date() || apt.status === "COMPLETED";
  });

  const getStatusColor = (status: string) => {
    switch (status) {
      case "SCHEDULED":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200";
      case "CONFIRMED":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200";
      case "COMPLETED":
        return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200";
      case "CANCELLED":
        return "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200";
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "SCHEDULED":
        return <Clock className="h-4 w-4" />;
      case "CONFIRMED":
        return <CheckCircle className="h-4 w-4" />;
      case "COMPLETED":
        return <CheckCircle className="h-4 w-4" />;
      case "CANCELLED":
        return <XCircle className="h-4 w-4" />;
      default:
        return <AlertCircle className="h-4 w-4" />;
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("en-US", {
      weekday: "short",
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  const formatTime = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleTimeString("en-US", {
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const AppointmentCard = ({ appointment }: { appointment: Appointment }) => (
    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
      <div className="flex items-start justify-between mb-4">
        <div className="flex-1">
          <div className="flex items-center space-x-2 mb-2">
            <Stethoscope className="h-5 w-5 text-primary-600 dark:text-primary-400" />
            <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
              {appointment.doctorName || `Dr. ${appointment.doctorId}`}
            </h3>
          </div>
          <p className="text-sm text-neutral-600 dark:text-neutral-400 mb-3">
            {appointment.reason}
          </p>
        </div>
        <span
          className={`inline-flex items-center space-x-1 px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
            appointment.status,
          )}`}
        >
          {getStatusIcon(appointment.status)}
          <span>{appointment.status}</span>
        </span>
      </div>

      <div className="grid grid-cols-2 gap-4 mb-4">
        <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
          <Calendar className="h-4 w-4" />
          <span>{formatDate(appointment.appointmentDateTime)}</span>
        </div>
        <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
          <Clock className="h-4 w-4" />
          <span>{formatTime(appointment.appointmentDateTime)}</span>
        </div>
        {appointment.type && (
          <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
            {appointment.type === "VIDEO" ? (
              <Video className="h-4 w-4" />
            ) : (
              <MapPin className="h-4 w-4" />
            )}
            <span>{appointment.type}</span>
          </div>
        )}
        {appointment.duration && (
          <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
            <Clock className="h-4 w-4" />
            <span>{appointment.duration} mins</span>
          </div>
        )}
      </div>

      <div className="flex items-center space-x-2 pt-4 border-t border-neutral-200 dark:border-neutral-700">
        <button
          onClick={() => openDetailsModal(appointment)}
          className="flex-1 flex items-center justify-center space-x-2 px-4 py-2 text-sm font-medium text-primary-600 dark:text-primary-400 bg-primary-50 dark:bg-primary-900/20 rounded-lg hover:bg-primary-100 dark:hover:bg-primary-900/30 transition-colors"
        >
          <FileText className="h-4 w-4" />
          <span>View Details</span>
        </button>
        {(appointment.status === "SCHEDULED" ||
          appointment.status === "CONFIRMED") && (
          <button
            onClick={() => handleCancel(appointment.id)}
            className="flex items-center justify-center space-x-2 px-4 py-2 text-sm font-medium text-red-600 dark:text-red-400 bg-red-50 dark:bg-red-900/20 rounded-lg hover:bg-red-100 dark:hover:bg-red-900/30 transition-colors"
          >
            <XCircle className="h-4 w-4" />
            <span>Cancel</span>
          </button>
        )}
      </div>
    </div>
  );

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
              My Appointments
            </h1>
            <p className="text-neutral-600 dark:text-neutral-400 mt-1">
              Manage and view your medical appointments
            </p>
          </div>
          <button
            onClick={() => setShowCreateModal(true)}
            className="flex items-center space-x-2 px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors"
          >
            <Plus className="h-5 w-5" />
            <span>Book Appointment</span>
          </button>
        </div>

        {/* Alert Messages */}
        {error && (
          <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-800 dark:text-red-200 px-4 py-3 rounded-lg flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <AlertCircle className="h-5 w-5" />
              <span>{error}</span>
            </div>
            <button onClick={() => setError("")}>
              <X className="h-5 w-5" />
            </button>
          </div>
        )}

        {success && (
          <div className="bg-green-50 dark:bg-green-900/20 border border-green-200 dark:border-green-800 text-green-800 dark:text-green-200 px-4 py-3 rounded-lg flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <CheckCircle className="h-5 w-5" />
              <span>{success}</span>
            </div>
            <button onClick={() => setSuccess("")}>
              <X className="h-5 w-5" />
            </button>
          </div>
        )}

        {/* Search and Filters */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
              <input
                type="text"
                placeholder="Search by doctor or reason..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-2 bg-neutral-50 dark:bg-neutral-700 border border-neutral-200 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 text-neutral-900 dark:text-neutral-100"
              />
            </div>

            <div className="flex items-center space-x-2">
              <Filter className="h-5 w-5 text-neutral-400" />
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="flex-1 px-4 py-2 bg-neutral-50 dark:bg-neutral-700 border border-neutral-200 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 text-neutral-900 dark:text-neutral-100"
              >
                <option value="ALL">All Status</option>
                <option value="SCHEDULED">Scheduled</option>
                <option value="CONFIRMED">Confirmed</option>
                <option value="COMPLETED">Completed</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>
          </div>
        </div>

        {loading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto"></div>
            <p className="text-neutral-600 dark:text-neutral-400 mt-4">
              Loading appointments...
            </p>
          </div>
        ) : (
          <>
            {/* Upcoming Appointments */}
            <div>
              <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                Upcoming Appointments ({upcomingAppointments.length})
              </h2>
              {upcomingAppointments.length === 0 ? (
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-8 text-center">
                  <Calendar className="h-12 w-12 text-neutral-400 mx-auto mb-3" />
                  <p className="text-neutral-600 dark:text-neutral-400">
                    No upcoming appointments
                  </p>
                  <button
                    onClick={() => setShowCreateModal(true)}
                    className="mt-4 text-primary-600 dark:text-primary-400 hover:underline"
                  >
                    Book your first appointment
                  </button>
                </div>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {upcomingAppointments.map((appointment) => (
                    <AppointmentCard
                      key={appointment.id}
                      appointment={appointment}
                    />
                  ))}
                </div>
              )}
            </div>

            {/* Past Appointments */}
            {pastAppointments.length > 0 && (
              <div>
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                  Past Appointments ({pastAppointments.length})
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {pastAppointments.map((appointment) => (
                    <AppointmentCard
                      key={appointment.id}
                      appointment={appointment}
                    />
                  ))}
                </div>
              </div>
            )}
          </>
        )}

        {/* Create Modal */}
        {showCreateModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-md w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100">
                  Book New Appointment
                </h2>
                <button
                  onClick={() => {
                    setShowCreateModal(false);
                    resetForm();
                    setError("");
                  }}
                  className="text-neutral-600 dark:text-neutral-400 hover:text-neutral-900 dark:hover:text-neutral-100"
                >
                  <X className="h-6 w-6" />
                </button>
              </div>

              <form onSubmit={handleCreate} className="p-6 space-y-4">
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-1">
                    Doctor ID *
                  </label>
                  <input
                    type="number"
                    required
                    value={formData.doctorId}
                    onChange={(e) =>
                      setFormData({ ...formData, doctorId: e.target.value })
                    }
                    className="w-full px-3 py-2 bg-white dark:bg-neutral-700 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 text-neutral-900 dark:text-neutral-100"
                    placeholder="Enter doctor ID"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-1">
                    Date & Time *
                  </label>
                  <input
                    type="datetime-local"
                    required
                    value={formData.appointmentDateTime}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        appointmentDateTime: e.target.value,
                      })
                    }
                    className="w-full px-3 py-2 bg-white dark:bg-neutral-700 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 text-neutral-900 dark:text-neutral-100"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-1">
                    Appointment Type *
                  </label>
                  <select
                    required
                    value={formData.type}
                    onChange={(e) =>
                      setFormData({ ...formData, type: e.target.value })
                    }
                    className="w-full px-3 py-2 bg-white dark:bg-neutral-700 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 text-neutral-900 dark:text-neutral-100"
                  >
                    <option value="IN_PERSON">In Person</option>
                    <option value="VIDEO">Video Call</option>
                    <option value="PHONE">Phone Call</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-1">
                    Reason for Visit *
                  </label>
                  <input
                    type="text"
                    required
                    value={formData.reason}
                    onChange={(e) =>
                      setFormData({ ...formData, reason: e.target.value })
                    }
                    className="w-full px-3 py-2 bg-white dark:bg-neutral-700 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 text-neutral-900 dark:text-neutral-100"
                    placeholder="e.g., Regular checkup, Follow-up visit"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-1">
                    Additional Notes
                  </label>
                  <textarea
                    value={formData.notes}
                    onChange={(e) =>
                      setFormData({ ...formData, notes: e.target.value })
                    }
                    rows={3}
                    className="w-full px-3 py-2 bg-white dark:bg-neutral-700 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 text-neutral-900 dark:text-neutral-100"
                    placeholder="Any specific concerns or information"
                  />
                </div>

                <div className="flex space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => {
                      setShowCreateModal(false);
                      resetForm();
                      setError("");
                    }}
                    className="flex-1 px-4 py-2 text-neutral-700 dark:text-neutral-300 bg-neutral-100 dark:bg-neutral-700 rounded-lg hover:bg-neutral-200 dark:hover:bg-neutral-600 transition-colors"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="flex-1 px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors"
                  >
                    Book Appointment
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* Details Modal */}
        {showDetailsModal && selectedAppointment && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-md w-full">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100">
                  Appointment Details
                </h2>
                <button
                  onClick={() => setShowDetailsModal(false)}
                  className="text-neutral-600 dark:text-neutral-400 hover:text-neutral-900 dark:hover:text-neutral-100"
                >
                  <X className="h-6 w-6" />
                </button>
              </div>

              <div className="p-6 space-y-4">
                <div className="flex items-center justify-between">
                  <span className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                    Status
                  </span>
                  <span
                    className={`inline-flex items-center space-x-1 px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
                      selectedAppointment.status,
                    )}`}
                  >
                    {getStatusIcon(selectedAppointment.status)}
                    <span>{selectedAppointment.status}</span>
                  </span>
                </div>

                <div>
                  <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                    Doctor
                  </p>
                  <p className="text-base font-semibold text-neutral-900 dark:text-neutral-100 mt-1">
                    {selectedAppointment.doctorName ||
                      `Dr. ${selectedAppointment.doctorId}`}
                  </p>
                </div>

                <div>
                  <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                    Date & Time
                  </p>
                  <p className="text-base text-neutral-900 dark:text-neutral-100 mt-1">
                    {formatDate(selectedAppointment.appointmentDateTime)} at{" "}
                    {formatTime(selectedAppointment.appointmentDateTime)}
                  </p>
                </div>

                {selectedAppointment.type && (
                  <div>
                    <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                      Type
                    </p>
                    <p className="text-base text-neutral-900 dark:text-neutral-100 mt-1">
                      {selectedAppointment.type}
                    </p>
                  </div>
                )}

                <div>
                  <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                    Reason
                  </p>
                  <p className="text-base text-neutral-900 dark:text-neutral-100 mt-1">
                    {selectedAppointment.reason}
                  </p>
                </div>

                {selectedAppointment.notes && (
                  <div>
                    <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                      Notes
                    </p>
                    <p className="text-base text-neutral-900 dark:text-neutral-100 mt-1">
                      {selectedAppointment.notes}
                    </p>
                  </div>
                )}

                {selectedAppointment.duration && (
                  <div>
                    <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                      Duration
                    </p>
                    <p className="text-base text-neutral-900 dark:text-neutral-100 mt-1">
                      {selectedAppointment.duration} minutes
                    </p>
                  </div>
                )}
              </div>

              <div className="p-6 border-t border-neutral-200 dark:border-neutral-700">
                <button
                  onClick={() => setShowDetailsModal(false)}
                  className="w-full px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors"
                >
                  Close
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}

export default withAuth(MyAppointmentsPage, ["PATIENT"]);
