"use client";

import React, { useState, useEffect } from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  Pill,
  Download,
  Eye,
  Calendar,
  Clock,
  User,
  Stethoscope,
  Search,
  Filter,
  X,
  AlertCircle,
  CheckCircle,
  Activity,
} from "lucide-react";
import { prescriptionsApi } from "@/services/api";

interface Prescription {
  id: number;
  patientId: number;
  doctorId: number;
  medication: string;
  dosage: string;
  frequency: string;
  duration: string;
  instructions?: string;
  prescriptionDate: string;
  startDate?: string;
  endDate?: string;
  refills?: number;
  status?: string;
  doctorName?: string;
  patientName?: string;
}

function MyPrescriptionsPage() {
  const { user } = useAuth();
  const [prescriptions, setPrescriptions] = useState<Prescription[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [selectedPrescription, setSelectedPrescription] =
    useState<Prescription | null>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchPrescriptions();
  }, []);

  const fetchPrescriptions = async () => {
    try {
      setLoading(true);
      setError("");
      if (!user?.id) {
        setError("User not found");
        return;
      }
      const data = await prescriptionsApi.getByPatient(user.id);
      setPrescriptions(data);
    } catch (err) {
      console.error("Error fetching prescriptions:", err);
      setError("Failed to load prescriptions");
    } finally {
      setLoading(false);
    }
  };

  const openDetailsModal = (prescription: Prescription) => {
    setSelectedPrescription(prescription);
    setShowDetailsModal(true);
  };

  const handleDownload = (prescription: Prescription) => {
    const content = `
PRESCRIPTION
============

Prescription ID: ${prescription.id}
Date Issued: ${formatDate(prescription.prescriptionDate)}
${prescription.startDate ? `Start Date: ${formatDate(prescription.startDate)}` : ""}
${prescription.endDate ? `End Date: ${formatDate(prescription.endDate)}` : ""}

Prescribed by: ${prescription.doctorName || `Dr. ${prescription.doctorId}`}
Patient: ${user?.fullName || "Patient"}

MEDICATION DETAILS:
-------------------
Medication: ${prescription.medication}
Dosage: ${prescription.dosage}
Frequency: ${prescription.frequency}
Duration: ${prescription.duration}
${prescription.refills !== undefined ? `Refills Remaining: ${prescription.refills}` : ""}

INSTRUCTIONS:
${prescription.instructions || "No special instructions"}

Status: ${prescription.status || "ACTIVE"}

---
This is a computer-generated prescription.
Generated on: ${new Date().toLocaleString()}
    `.trim();

    const blob = new Blob([content], { type: "text/plain" });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `prescription-${prescription.id}-${prescription.medication.replace(/\s+/g, "-")}.txt`;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  };

  const filteredPrescriptions = prescriptions.filter((prescription) => {
    const matchesSearch =
      prescription.medication
        ?.toLowerCase()
        .includes(searchQuery.toLowerCase()) ||
      prescription.doctorName
        ?.toLowerCase()
        .includes(searchQuery.toLowerCase()) ||
      prescription.instructions
        ?.toLowerCase()
        .includes(searchQuery.toLowerCase());

    const matchesStatus =
      statusFilter === "ALL" || prescription.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const activePrescriptions = filteredPrescriptions.filter((rx) => {
    if (rx.status === "ACTIVE" || !rx.status) return true;
    if (rx.endDate) {
      const endDate = new Date(rx.endDate);
      return endDate >= new Date();
    }
    return false;
  });

  const expiredPrescriptions = filteredPrescriptions.filter((rx) => {
    if (rx.status === "EXPIRED" || rx.status === "COMPLETED") return true;
    if (rx.endDate) {
      const endDate = new Date(rx.endDate);
      return endDate < new Date();
    }
    return false;
  });

  const getStatusColor = (status?: string) => {
    switch (status) {
      case "ACTIVE":
        return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200";
      case "COMPLETED":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200";
      case "EXPIRED":
        return "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200";
      case "CANCELLED":
        return "bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200";
      default:
        return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200";
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  };

  const PrescriptionCard = ({
    prescription,
  }: {
    prescription: Prescription;
  }) => {
    const isExpiringSoon =
      prescription.endDate &&
      new Date(prescription.endDate) <=
        new Date(Date.now() + 7 * 24 * 60 * 60 * 1000) &&
      new Date(prescription.endDate) >= new Date();

    return (
      <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
        <div className="flex items-start justify-between mb-4">
          <div className="flex-1">
            <div className="flex items-center space-x-2 mb-2">
              <Pill className="h-5 w-5 text-primary-600 dark:text-primary-400" />
              <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                {prescription.medication}
              </h3>
            </div>
            <p className="text-sm text-neutral-600 dark:text-neutral-400 mb-1">
              {prescription.dosage} - {prescription.frequency}
            </p>
            <p className="text-sm text-neutral-600 dark:text-neutral-400">
              Duration: {prescription.duration}
            </p>
          </div>
          <span
            className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
              prescription.status,
            )}`}
          >
            {prescription.status || "ACTIVE"}
          </span>
        </div>

        {isExpiringSoon && (
          <div className="mb-4 p-2 bg-yellow-50 dark:bg-yellow-900/20 border border-yellow-200 dark:border-yellow-800 rounded-lg flex items-center space-x-2">
            <AlertCircle className="h-4 w-4 text-yellow-600 dark:text-yellow-400" />
            <span className="text-xs text-yellow-800 dark:text-yellow-200">
              Expiring soon
            </span>
          </div>
        )}

        <div className="grid grid-cols-2 gap-4 mb-4">
          <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
            <Calendar className="h-4 w-4" />
            <span>{formatDate(prescription.prescriptionDate)}</span>
          </div>
          <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
            <Stethoscope className="h-4 w-4" />
            <span>
              {prescription.doctorName || `Dr. ${prescription.doctorId}`}
            </span>
          </div>
          {prescription.refills !== undefined && (
            <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
              <Activity className="h-4 w-4" />
              <span>{prescription.refills} refills left</span>
            </div>
          )}
          {prescription.endDate && (
            <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
              <Clock className="h-4 w-4" />
              <span>Until {formatDate(prescription.endDate)}</span>
            </div>
          )}
        </div>

        <div className="flex items-center space-x-2 pt-4 border-t border-neutral-200 dark:border-neutral-700">
          <button
            onClick={() => openDetailsModal(prescription)}
            className="flex-1 flex items-center justify-center space-x-2 px-4 py-2 text-sm font-medium text-primary-600 dark:text-primary-400 bg-primary-50 dark:bg-primary-900/20 rounded-lg hover:bg-primary-100 dark:hover:bg-primary-900/30 transition-colors"
          >
            <Eye className="h-4 w-4" />
            <span>View Details</span>
          </button>
          <button
            onClick={() => handleDownload(prescription)}
            className="flex items-center justify-center space-x-2 px-4 py-2 text-sm font-medium text-green-600 dark:text-green-400 bg-green-50 dark:bg-green-900/20 rounded-lg hover:bg-green-100 dark:hover:bg-green-900/30 transition-colors"
          >
            <Download className="h-4 w-4" />
            <span>Download</span>
          </button>
        </div>
      </div>
    );
  };

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
            My Prescriptions
          </h1>
          <p className="text-neutral-600 dark:text-neutral-400 mt-1">
            View and manage your prescribed medications
          </p>
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

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                  Active Prescriptions
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {activePrescriptions.length}
                </p>
              </div>
              <div className="bg-green-100 dark:bg-green-900 p-3 rounded-lg">
                <Pill className="h-6 w-6 text-green-600 dark:text-green-400" />
              </div>
            </div>
          </div>

          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                  Total Prescriptions
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {prescriptions.length}
                </p>
              </div>
              <div className="bg-blue-100 dark:bg-blue-900 p-3 rounded-lg">
                <Activity className="h-6 w-6 text-blue-600 dark:text-blue-400" />
              </div>
            </div>
          </div>

          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                  Refills Available
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {prescriptions.reduce(
                    (sum, rx) => sum + (rx.refills || 0),
                    0,
                  )}
                </p>
              </div>
              <div className="bg-purple-100 dark:bg-purple-900 p-3 rounded-lg">
                <CheckCircle className="h-6 w-6 text-purple-600 dark:text-purple-400" />
              </div>
            </div>
          </div>
        </div>

        {/* Search and Filters */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
              <input
                type="text"
                placeholder="Search by medication name or doctor..."
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
                <option value="ACTIVE">Active</option>
                <option value="COMPLETED">Completed</option>
                <option value="EXPIRED">Expired</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>
          </div>
        </div>

        {loading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto"></div>
            <p className="text-neutral-600 dark:text-neutral-400 mt-4">
              Loading prescriptions...
            </p>
          </div>
        ) : (
          <>
            {/* Active Prescriptions */}
            <div>
              <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                Active Prescriptions ({activePrescriptions.length})
              </h2>
              {activePrescriptions.length === 0 ? (
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-8 text-center">
                  <Pill className="h-12 w-12 text-neutral-400 mx-auto mb-3" />
                  <p className="text-neutral-600 dark:text-neutral-400">
                    No active prescriptions
                  </p>
                </div>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {activePrescriptions.map((prescription) => (
                    <PrescriptionCard
                      key={prescription.id}
                      prescription={prescription}
                    />
                  ))}
                </div>
              )}
            </div>

            {/* Past Prescriptions */}
            {expiredPrescriptions.length > 0 && (
              <div>
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                  Past Prescriptions ({expiredPrescriptions.length})
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {expiredPrescriptions.map((prescription) => (
                    <PrescriptionCard
                      key={prescription.id}
                      prescription={prescription}
                    />
                  ))}
                </div>
              </div>
            )}
          </>
        )}

        {/* Details Modal */}
        {showDetailsModal && selectedPrescription && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100">
                  Prescription Details
                </h2>
                <button
                  onClick={() => setShowDetailsModal(false)}
                  className="text-neutral-600 dark:text-neutral-400 hover:text-neutral-900 dark:hover:text-neutral-100"
                >
                  <X className="h-6 w-6" />
                </button>
              </div>

              <div className="p-6 space-y-6">
                {/* Header */}
                <div className="flex items-start justify-between">
                  <div className="flex items-center space-x-3">
                    <div className="bg-primary-100 dark:bg-primary-900 p-3 rounded-lg">
                      <Pill className="h-6 w-6 text-primary-600 dark:text-primary-400" />
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100">
                        {selectedPrescription.medication}
                      </h3>
                      <p className="text-sm text-neutral-600 dark:text-neutral-400">
                        Prescription #{selectedPrescription.id}
                      </p>
                    </div>
                  </div>
                  <span
                    className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
                      selectedPrescription.status,
                    )}`}
                  >
                    {selectedPrescription.status || "ACTIVE"}
                  </span>
                </div>

                {/* Medication Details */}
                <div className="grid grid-cols-2 gap-4 p-4 bg-neutral-50 dark:bg-neutral-700/50 rounded-lg">
                  <div>
                    <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Dosage
                    </p>
                    <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                      {selectedPrescription.dosage}
                    </p>
                  </div>
                  <div>
                    <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Frequency
                    </p>
                    <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                      {selectedPrescription.frequency}
                    </p>
                  </div>
                  <div>
                    <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Duration
                    </p>
                    <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                      {selectedPrescription.duration}
                    </p>
                  </div>
                  {selectedPrescription.refills !== undefined && (
                    <div>
                      <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                        Refills Remaining
                      </p>
                      <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                        {selectedPrescription.refills}
                      </p>
                    </div>
                  )}
                </div>

                {/* Dates */}
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400 mb-1 flex items-center space-x-2">
                      <Calendar className="h-4 w-4" />
                      <span>Prescribed On</span>
                    </p>
                    <p className="text-base text-neutral-900 dark:text-neutral-100">
                      {formatDate(selectedPrescription.prescriptionDate)}
                    </p>
                  </div>
                  {selectedPrescription.endDate && (
                    <div>
                      <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400 mb-1 flex items-center space-x-2">
                        <Clock className="h-4 w-4" />
                        <span>Valid Until</span>
                      </p>
                      <p className="text-base text-neutral-900 dark:text-neutral-100">
                        {formatDate(selectedPrescription.endDate)}
                      </p>
                    </div>
                  )}
                </div>

                {/* Doctor */}
                <div>
                  <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400 mb-1 flex items-center space-x-2">
                    <Stethoscope className="h-4 w-4" />
                    <span>Prescribed By</span>
                  </p>
                  <p className="text-base text-neutral-900 dark:text-neutral-100">
                    {selectedPrescription.doctorName ||
                      `Dr. ${selectedPrescription.doctorId}`}
                  </p>
                </div>

                {/* Instructions */}
                {selectedPrescription.instructions && (
                  <div>
                    <h4 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                      Instructions
                    </h4>
                    <p className="text-sm text-neutral-700 dark:text-neutral-300 bg-neutral-50 dark:bg-neutral-700/50 p-3 rounded-lg whitespace-pre-wrap">
                      {selectedPrescription.instructions}
                    </p>
                  </div>
                )}
              </div>

              <div className="p-6 border-t border-neutral-200 dark:border-neutral-700 flex space-x-3">
                <button
                  onClick={() => handleDownload(selectedPrescription)}
                  className="flex-1 flex items-center justify-center space-x-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                >
                  <Download className="h-4 w-4" />
                  <span>Download Prescription</span>
                </button>
                <button
                  onClick={() => setShowDetailsModal(false)}
                  className="px-4 py-2 text-neutral-700 dark:text-neutral-300 bg-neutral-100 dark:bg-neutral-700 rounded-lg hover:bg-neutral-200 dark:hover:bg-neutral-600 transition-colors"
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

export default withAuth(MyPrescriptionsPage, ["PATIENT"]);
