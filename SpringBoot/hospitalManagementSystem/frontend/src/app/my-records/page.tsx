"use client";

import React, { useState, useEffect } from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  FileText,
  Download,
  Eye,
  Calendar,
  User,
  Stethoscope,
  Search,
  Filter,
  X,
  AlertCircle,
  Clipboard,
  Heart,
  Activity,
  Pill,
} from "lucide-react";
import { medicalRecordsApi } from "@/services/api";

interface MedicalRecord {
  id: number;
  patientId: number;
  doctorId: number;
  appointmentId?: number;
  recordType: string;
  diagnosis: string;
  symptoms?: string;
  treatment?: string;
  notes?: string;
  recordDate: string;
  doctorName?: string;
  patientName?: string;
}

function MyRecordsPage() {
  const { user } = useAuth();
  const [records, setRecords] = useState<MedicalRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [typeFilter, setTypeFilter] = useState<string>("ALL");
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [selectedRecord, setSelectedRecord] = useState<MedicalRecord | null>(
    null,
  );
  const [error, setError] = useState("");

  useEffect(() => {
    fetchRecords();
  }, []);

  const fetchRecords = async () => {
    try {
      setLoading(true);
      setError("");
      if (!user?.id) {
        setError("User not found");
        return;
      }
      const data = await medicalRecordsApi.getByPatient(user.id);
      setRecords(data);
    } catch (err) {
      console.error("Error fetching medical records:", err);
      setError("Failed to load medical records");
    } finally {
      setLoading(false);
    }
  };

  const openDetailsModal = (record: MedicalRecord) => {
    setSelectedRecord(record);
    setShowDetailsModal(true);
  };

  const handleDownload = (record: MedicalRecord) => {
    // Create a text representation of the record
    const content = `
MEDICAL RECORD
==============

Record ID: ${record.id}
Date: ${formatDate(record.recordDate)}
Type: ${record.recordType}

Doctor: ${record.doctorName || `Doctor #${record.doctorId}`}

DIAGNOSIS:
${record.diagnosis}

${record.symptoms ? `SYMPTOMS:\n${record.symptoms}\n` : ""}
${record.treatment ? `TREATMENT:\n${record.treatment}\n` : ""}
${record.notes ? `NOTES:\n${record.notes}\n` : ""}

Generated on: ${new Date().toLocaleString()}
    `.trim();

    const blob = new Blob([content], { type: "text/plain" });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `medical-record-${record.id}-${record.recordDate}.txt`;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  };

  const filteredRecords = records.filter((record) => {
    const matchesSearch =
      record.diagnosis?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      record.doctorName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      record.symptoms?.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesType =
      typeFilter === "ALL" || record.recordType === typeFilter;

    return matchesSearch && matchesType;
  });

  const getRecordTypeColor = (type: string) => {
    switch (type) {
      case "CONSULTATION":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200";
      case "FOLLOW_UP":
        return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200";
      case "EMERGENCY":
        return "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200";
      case "ROUTINE_CHECKUP":
        return "bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200";
      case "LAB_RESULT":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200";
    }
  };

  const getRecordTypeIcon = (type: string) => {
    switch (type) {
      case "CONSULTATION":
        return <Stethoscope className="h-4 w-4" />;
      case "FOLLOW_UP":
        return <Clipboard className="h-4 w-4" />;
      case "EMERGENCY":
        return <AlertCircle className="h-4 w-4" />;
      case "ROUTINE_CHECKUP":
        return <Heart className="h-4 w-4" />;
      case "LAB_RESULT":
        return <Activity className="h-4 w-4" />;
      default:
        return <FileText className="h-4 w-4" />;
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

  const RecordCard = ({ record }: { record: MedicalRecord }) => (
    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
      <div className="flex items-start justify-between mb-4">
        <div className="flex-1">
          <div className="flex items-center space-x-2 mb-2">
            {getRecordTypeIcon(record.recordType)}
            <span
              className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getRecordTypeColor(
                record.recordType,
              )}`}
            >
              {record.recordType.replace(/_/g, " ")}
            </span>
          </div>
          <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-1">
            {record.diagnosis}
          </h3>
          <div className="flex items-center space-x-4 text-sm text-neutral-600 dark:text-neutral-400">
            <div className="flex items-center space-x-1">
              <Calendar className="h-4 w-4" />
              <span>{formatDate(record.recordDate)}</span>
            </div>
            <div className="flex items-center space-x-1">
              <Stethoscope className="h-4 w-4" />
              <span>{record.doctorName || `Dr. ${record.doctorId}`}</span>
            </div>
          </div>
        </div>
      </div>

      {record.symptoms && (
        <div className="mb-4">
          <p className="text-sm text-neutral-600 dark:text-neutral-400">
            <span className="font-medium">Symptoms: </span>
            {record.symptoms.length > 100
              ? `${record.symptoms.substring(0, 100)}...`
              : record.symptoms}
          </p>
        </div>
      )}

      <div className="flex items-center space-x-2 pt-4 border-t border-neutral-200 dark:border-neutral-700">
        <button
          onClick={() => openDetailsModal(record)}
          className="flex-1 flex items-center justify-center space-x-2 px-4 py-2 text-sm font-medium text-primary-600 dark:text-primary-400 bg-primary-50 dark:bg-primary-900/20 rounded-lg hover:bg-primary-100 dark:hover:bg-primary-900/30 transition-colors"
        >
          <Eye className="h-4 w-4" />
          <span>View Details</span>
        </button>
        <button
          onClick={() => handleDownload(record)}
          className="flex items-center justify-center space-x-2 px-4 py-2 text-sm font-medium text-green-600 dark:text-green-400 bg-green-50 dark:bg-green-900/20 rounded-lg hover:bg-green-100 dark:hover:bg-green-900/30 transition-colors"
        >
          <Download className="h-4 w-4" />
          <span>Download</span>
        </button>
      </div>
    </div>
  );

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
            My Medical Records
          </h1>
          <p className="text-neutral-600 dark:text-neutral-400 mt-1">
            View and download your complete medical history
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
                  Total Records
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {records.length}
                </p>
              </div>
              <div className="bg-blue-100 dark:bg-blue-900 p-3 rounded-lg">
                <FileText className="h-6 w-6 text-blue-600 dark:text-blue-400" />
              </div>
            </div>
          </div>

          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                  This Year
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {
                    records.filter((r) => {
                      const year = new Date(r.recordDate).getFullYear();
                      return year === new Date().getFullYear();
                    }).length
                  }
                </p>
              </div>
              <div className="bg-green-100 dark:bg-green-900 p-3 rounded-lg">
                <Calendar className="h-6 w-6 text-green-600 dark:text-green-400" />
              </div>
            </div>
          </div>

          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                  Last 30 Days
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {
                    records.filter((r) => {
                      const date = new Date(r.recordDate);
                      const thirtyDaysAgo = new Date();
                      thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
                      return date >= thirtyDaysAgo;
                    }).length
                  }
                </p>
              </div>
              <div className="bg-purple-100 dark:bg-purple-900 p-3 rounded-lg">
                <Activity className="h-6 w-6 text-purple-600 dark:text-purple-400" />
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
                placeholder="Search records by diagnosis, doctor, or symptoms..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-2 bg-neutral-50 dark:bg-neutral-700 border border-neutral-200 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 text-neutral-900 dark:text-neutral-100"
              />
            </div>

            <div className="flex items-center space-x-2">
              <Filter className="h-5 w-5 text-neutral-400" />
              <select
                value={typeFilter}
                onChange={(e) => setTypeFilter(e.target.value)}
                className="flex-1 px-4 py-2 bg-neutral-50 dark:bg-neutral-700 border border-neutral-200 dark:border-neutral-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 text-neutral-900 dark:text-neutral-100"
              >
                <option value="ALL">All Types</option>
                <option value="CONSULTATION">Consultation</option>
                <option value="FOLLOW_UP">Follow Up</option>
                <option value="EMERGENCY">Emergency</option>
                <option value="ROUTINE_CHECKUP">Routine Checkup</option>
                <option value="LAB_RESULT">Lab Result</option>
              </select>
            </div>
          </div>
        </div>

        {loading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto"></div>
            <p className="text-neutral-600 dark:text-neutral-400 mt-4">
              Loading medical records...
            </p>
          </div>
        ) : filteredRecords.length === 0 ? (
          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-12 text-center">
            <FileText className="h-16 w-16 text-neutral-400 mx-auto mb-4" />
            <h3 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
              No Medical Records Found
            </h3>
            <p className="text-neutral-600 dark:text-neutral-400">
              {searchQuery || typeFilter !== "ALL"
                ? "Try adjusting your search or filters"
                : "Your medical records will appear here after consultations"}
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredRecords.map((record) => (
              <RecordCard key={record.id} record={record} />
            ))}
          </div>
        )}

        {/* Details Modal */}
        {showDetailsModal && selectedRecord && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100">
                  Medical Record Details
                </h2>
                <button
                  onClick={() => setShowDetailsModal(false)}
                  className="text-neutral-600 dark:text-neutral-400 hover:text-neutral-900 dark:hover:text-neutral-100"
                >
                  <X className="h-6 w-6" />
                </button>
              </div>

              <div className="p-6 space-y-6">
                {/* Header Info */}
                <div className="flex items-start justify-between">
                  <div>
                    <span
                      className={`inline-flex items-center space-x-1 px-3 py-1 rounded-full text-xs font-medium ${getRecordTypeColor(
                        selectedRecord.recordType,
                      )}`}
                    >
                      {getRecordTypeIcon(selectedRecord.recordType)}
                      <span>
                        {selectedRecord.recordType.replace(/_/g, " ")}
                      </span>
                    </span>
                    <h3 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mt-3">
                      {selectedRecord.diagnosis}
                    </h3>
                  </div>
                </div>

                {/* Meta Information */}
                <div className="grid grid-cols-2 gap-4 p-4 bg-neutral-50 dark:bg-neutral-700/50 rounded-lg">
                  <div>
                    <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Record ID
                    </p>
                    <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                      #{selectedRecord.id}
                    </p>
                  </div>
                  <div>
                    <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Date
                    </p>
                    <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                      {formatDate(selectedRecord.recordDate)}
                    </p>
                  </div>
                  <div>
                    <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Doctor
                    </p>
                    <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                      {selectedRecord.doctorName ||
                        `Dr. ${selectedRecord.doctorId}`}
                    </p>
                  </div>
                  {selectedRecord.appointmentId && (
                    <div>
                      <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                        Appointment ID
                      </p>
                      <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                        #{selectedRecord.appointmentId}
                      </p>
                    </div>
                  )}
                </div>

                {/* Diagnosis */}
                <div>
                  <h4 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-2 flex items-center space-x-2">
                    <Clipboard className="h-4 w-4" />
                    <span>Diagnosis</span>
                  </h4>
                  <p className="text-sm text-neutral-700 dark:text-neutral-300 bg-neutral-50 dark:bg-neutral-700/50 p-3 rounded-lg">
                    {selectedRecord.diagnosis}
                  </p>
                </div>

                {/* Symptoms */}
                {selectedRecord.symptoms && (
                  <div>
                    <h4 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-2 flex items-center space-x-2">
                      <AlertCircle className="h-4 w-4" />
                      <span>Symptoms</span>
                    </h4>
                    <p className="text-sm text-neutral-700 dark:text-neutral-300 bg-neutral-50 dark:bg-neutral-700/50 p-3 rounded-lg whitespace-pre-wrap">
                      {selectedRecord.symptoms}
                    </p>
                  </div>
                )}

                {/* Treatment */}
                {selectedRecord.treatment && (
                  <div>
                    <h4 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-2 flex items-center space-x-2">
                      <Pill className="h-4 w-4" />
                      <span>Treatment</span>
                    </h4>
                    <p className="text-sm text-neutral-700 dark:text-neutral-300 bg-neutral-50 dark:bg-neutral-700/50 p-3 rounded-lg whitespace-pre-wrap">
                      {selectedRecord.treatment}
                    </p>
                  </div>
                )}

                {/* Notes */}
                {selectedRecord.notes && (
                  <div>
                    <h4 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-2 flex items-center space-x-2">
                      <FileText className="h-4 w-4" />
                      <span>Additional Notes</span>
                    </h4>
                    <p className="text-sm text-neutral-700 dark:text-neutral-300 bg-neutral-50 dark:bg-neutral-700/50 p-3 rounded-lg whitespace-pre-wrap">
                      {selectedRecord.notes}
                    </p>
                  </div>
                )}
              </div>

              <div className="p-6 border-t border-neutral-200 dark:border-neutral-700 flex space-x-3">
                <button
                  onClick={() => handleDownload(selectedRecord)}
                  className="flex-1 flex items-center justify-center space-x-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                >
                  <Download className="h-4 w-4" />
                  <span>Download Record</span>
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

export default withAuth(MyRecordsPage, ["PATIENT"]);
