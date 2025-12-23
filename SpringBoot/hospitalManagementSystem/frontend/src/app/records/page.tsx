"use client";

import React, { useState, useEffect } from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  FileText,
  Plus,
  Search,
  Filter,
  Eye,
  Edit,
  Trash2,
  Upload,
  Download,
  X,
  Calendar,
  User,
  FileType,
  Save,
  Loader2,
} from "lucide-react";
import { medicalRecordsApi, patientsApi } from "@/services/api";
import toast from "react-hot-toast";

interface MedicalRecord {
  id: number;
  recordType: string;
  title: string;
  content: string;
  filePath?: string;
  createdAt: string;
  patientId: number;
  patientName?: string;
}

interface Patient {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

function MedicalRecordsPage() {
  const { user } = useAuth();
  const [records, setRecords] = useState<MedicalRecord[]>([]);
  const [patients, setPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterType, setFilterType] = useState("ALL");
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showViewModal, setShowViewModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedRecord, setSelectedRecord] = useState<MedicalRecord | null>(
    null,
  );
  const [uploadingFile, setUploadingFile] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const [formData, setFormData] = useState({
    recordType: "",
    title: "",
    content: "",
    patientId: "",
  });

  const recordTypes = [
    "Diagnosis",
    "Treatment Plan",
    "Progress Note",
    "Lab Result",
    "Radiology Report",
    "Surgical Report",
    "Consultation Note",
    "Discharge Summary",
    "Prescription",
    "Other",
  ];

  useEffect(() => {
    fetchRecords();
    fetchPatients();
  }, []);

  const fetchRecords = async () => {
    try {
      setLoading(true);
      const data = await medicalRecordsApi.getAll();
      setRecords(Array.isArray(data) ? data : []);
    } catch (error: any) {
      console.error("Failed to fetch medical records:", error);
      toast.error("Failed to load medical records");
      setRecords([]);
    } finally {
      setLoading(false);
    }
  };

  const fetchPatients = async () => {
    try {
      const data = await patientsApi.getAll();
      setPatients(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error("Failed to fetch patients:", error);
      setPatients([]);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.recordType || !formData.title || !formData.patientId) {
      toast.error("Please fill in all required fields");
      return;
    }

    try {
      setSubmitting(true);
      const newRecord = await medicalRecordsApi.create({
        recordType: formData.recordType,
        title: formData.title,
        content: formData.content,
        patientId: parseInt(formData.patientId),
      });
      toast.success("Medical record created successfully");
      setRecords([newRecord, ...records]);
      setShowCreateModal(false);
      resetForm();
    } catch (error: any) {
      console.error("Failed to create record:", error);
      toast.error(error.response?.data?.message || "Failed to create record");
    } finally {
      setSubmitting(false);
    }
  };

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedRecord) return;

    try {
      setSubmitting(true);
      const updated = await medicalRecordsApi.update(selectedRecord.id, {
        recordType: formData.recordType,
        title: formData.title,
        content: formData.content,
      });
      toast.success("Record updated successfully");
      setRecords(
        records.map((r) => (r.id === selectedRecord.id ? updated : r)),
      );
      setShowEditModal(false);
      setSelectedRecord(null);
      resetForm();
    } catch (error: any) {
      console.error("Failed to update record:", error);
      toast.error(error.response?.data?.message || "Failed to update record");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Are you sure you want to delete this record?")) return;

    try {
      await medicalRecordsApi.delete(id);
      toast.success("Record deleted successfully");
      setRecords(records.filter((r) => r.id !== id));
    } catch (error: any) {
      console.error("Failed to delete record:", error);
      toast.error(error.response?.data?.message || "Failed to delete record");
    }
  };

  const handleFileUpload = async (id: number, file: File) => {
    try {
      setUploadingFile(true);
      await medicalRecordsApi.uploadFile(id, file);
      toast.success("File uploaded successfully");
      fetchRecords();
    } catch (error: any) {
      console.error("Failed to upload file:", error);
      toast.error(error.response?.data?.message || "Failed to upload file");
    } finally {
      setUploadingFile(false);
    }
  };

  const resetForm = () => {
    setFormData({
      recordType: "",
      title: "",
      content: "",
      patientId: "",
    });
  };

  const openViewModal = (record: MedicalRecord) => {
    setSelectedRecord(record);
    setShowViewModal(true);
  };

  const openEditModal = (record: MedicalRecord) => {
    setSelectedRecord(record);
    setFormData({
      recordType: record.recordType,
      title: record.title,
      content: record.content,
      patientId: record.patientId.toString(),
    });
    setShowEditModal(true);
  };

  const filteredRecords = records.filter((record) => {
    const matchesSearch =
      record.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      record.recordType.toLowerCase().includes(searchQuery.toLowerCase()) ||
      record.content.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesFilter =
      filterType === "ALL" || record.recordType === filterType;

    return matchesSearch && matchesFilter;
  });

  const getPatientName = (patientId: number) => {
    const patient = patients.find((p) => p.id === patientId);
    return patient ? `${patient.firstName} ${patient.lastName}` : "Unknown";
  };

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 flex items-center">
              <FileText className="h-8 w-8 mr-3 text-teal-600" />
              Medical Records
            </h1>
            <p className="mt-2 text-neutral-600 dark:text-neutral-400">
              Manage patient medical records and documentation
            </p>
          </div>
          <button
            onClick={() => setShowCreateModal(true)}
            className="btn-primary btn-sm flex items-center space-x-2"
          >
            <Plus className="h-4 w-4" />
            <span>Add Record</span>
          </button>
        </div>

        {/* Filters */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                <input
                  type="text"
                  placeholder="Search records..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                />
              </div>
            </div>
            <div className="flex items-center space-x-2">
              <Filter className="h-5 w-5 text-neutral-400" />
              <select
                value={filterType}
                onChange={(e) => setFilterType(e.target.value)}
                className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
              >
                <option value="ALL">All Types</option>
                {recordTypes.map((type) => (
                  <option key={type} value={type}>
                    {type}
                  </option>
                ))}
              </select>
            </div>
          </div>
        </div>

        {/* Records List */}
        {loading ? (
          <div className="flex justify-center items-center h-64">
            <Loader2 className="h-8 w-8 animate-spin text-teal-600" />
          </div>
        ) : filteredRecords.length === 0 ? (
          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-8 text-center">
            <FileText className="h-16 w-16 mx-auto text-neutral-400 mb-4" />
            <h3 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
              No Records Found
            </h3>
            <p className="text-neutral-600 dark:text-neutral-400 mb-4">
              {searchQuery || filterType !== "ALL"
                ? "No records match your search criteria"
                : "Start by creating your first medical record"}
            </p>
            <button
              onClick={() => setShowCreateModal(true)}
              className="btn-primary"
            >
              <Plus className="h-4 w-4 mr-2" />
              Add Record
            </button>
          </div>
        ) : (
          <div className="grid gap-4">
            {filteredRecords.map((record) => (
              <div
                key={record.id}
                className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow"
              >
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center space-x-3 mb-2">
                      <span className="px-3 py-1 bg-teal-100 dark:bg-teal-900 text-teal-700 dark:text-teal-300 rounded-full text-sm font-medium">
                        {record.recordType}
                      </span>
                      {record.filePath && (
                        <span className="px-2 py-1 bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300 rounded text-xs flex items-center">
                          <Upload className="h-3 w-3 mr-1" />
                          File Attached
                        </span>
                      )}
                    </div>
                    <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                      {record.title}
                    </h3>
                    <p className="text-neutral-600 dark:text-neutral-400 mb-3 line-clamp-2">
                      {record.content}
                    </p>
                    <div className="flex items-center space-x-4 text-sm text-neutral-500 dark:text-neutral-400">
                      <span className="flex items-center">
                        <User className="h-4 w-4 mr-1" />
                        {getPatientName(record.patientId)}
                      </span>
                      <span className="flex items-center">
                        <Calendar className="h-4 w-4 mr-1" />
                        {new Date(record.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                  </div>
                  <div className="flex items-center space-x-2 ml-4">
                    <button
                      onClick={() => openViewModal(record)}
                      className="p-2 text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition"
                      title="View"
                    >
                      <Eye className="h-5 w-5" />
                    </button>
                    <button
                      onClick={() => openEditModal(record)}
                      className="p-2 text-amber-600 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition"
                      title="Edit"
                    >
                      <Edit className="h-5 w-5" />
                    </button>
                    <label
                      className="p-2 text-teal-600 hover:bg-teal-50 dark:hover:bg-teal-900/20 rounded-lg transition cursor-pointer"
                      title="Upload File"
                    >
                      <Upload className="h-5 w-5" />
                      <input
                        type="file"
                        className="hidden"
                        onChange={(e) => {
                          const file = e.target.files?.[0];
                          if (file) handleFileUpload(record.id, file);
                        }}
                      />
                    </label>
                    <button
                      onClick={() => handleDelete(record.id)}
                      className="p-2 text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition"
                      title="Delete"
                    >
                      <Trash2 className="h-5 w-5" />
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Create Modal */}
        {showCreateModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                  Create Medical Record
                </h2>
                <button
                  onClick={() => {
                    setShowCreateModal(false);
                    resetForm();
                  }}
                  className="p-2 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
                >
                  <X className="h-5 w-5" />
                </button>
              </div>
              <form onSubmit={handleCreate} className="p-6 space-y-4">
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Patient *
                  </label>
                  <select
                    value={formData.patientId}
                    onChange={(e) =>
                      setFormData({ ...formData, patientId: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  >
                    <option value="">Select Patient</option>
                    {patients.map((patient) => (
                      <option key={patient.id} value={patient.id}>
                        {patient.firstName} {patient.lastName}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Record Type *
                  </label>
                  <select
                    value={formData.recordType}
                    onChange={(e) =>
                      setFormData({ ...formData, recordType: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  >
                    <option value="">Select Type</option>
                    {recordTypes.map((type) => (
                      <option key={type} value={type}>
                        {type}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Title *
                  </label>
                  <input
                    type="text"
                    value={formData.title}
                    onChange={(e) =>
                      setFormData({ ...formData, title: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    placeholder="Enter record title"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Content
                  </label>
                  <textarea
                    value={formData.content}
                    onChange={(e) =>
                      setFormData({ ...formData, content: e.target.value })
                    }
                    rows={6}
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    placeholder="Enter record content..."
                  />
                </div>
                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => {
                      setShowCreateModal(false);
                      resetForm();
                    }}
                    className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    disabled={submitting}
                    className="btn-primary flex items-center"
                  >
                    {submitting ? (
                      <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                    ) : (
                      <Save className="h-4 w-4 mr-2" />
                    )}
                    Create Record
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* View Modal */}
        {showViewModal && selectedRecord && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                  Medical Record Details
                </h2>
                <button
                  onClick={() => {
                    setShowViewModal(false);
                    setSelectedRecord(null);
                  }}
                  className="p-2 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
                >
                  <X className="h-5 w-5" />
                </button>
              </div>
              <div className="p-6 space-y-4">
                <div>
                  <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                    Record Type
                  </label>
                  <span className="px-3 py-1 bg-teal-100 dark:bg-teal-900 text-teal-700 dark:text-teal-300 rounded-full text-sm font-medium">
                    {selectedRecord.recordType}
                  </span>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                    Title
                  </label>
                  <p className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                    {selectedRecord.title}
                  </p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                    Patient
                  </label>
                  <p className="text-neutral-900 dark:text-neutral-100">
                    {getPatientName(selectedRecord.patientId)}
                  </p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                    Created Date
                  </label>
                  <p className="text-neutral-900 dark:text-neutral-100">
                    {new Date(selectedRecord.createdAt).toLocaleDateString(
                      "en-US",
                      {
                        year: "numeric",
                        month: "long",
                        day: "numeric",
                      },
                    )}
                  </p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                    Content
                  </label>
                  <div className="bg-neutral-50 dark:bg-neutral-900 rounded-lg p-4">
                    <p className="text-neutral-900 dark:text-neutral-100 whitespace-pre-wrap">
                      {selectedRecord.content || "No content provided"}
                    </p>
                  </div>
                </div>
                {selectedRecord.filePath && (
                  <div>
                    <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Attached File
                    </label>
                    <div className="flex items-center space-x-2">
                      <FileType className="h-5 w-5 text-teal-600" />
                      <span className="text-neutral-900 dark:text-neutral-100">
                        {selectedRecord.filePath}
                      </span>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}

        {/* Edit Modal */}
        {showEditModal && selectedRecord && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                  Edit Medical Record
                </h2>
                <button
                  onClick={() => {
                    setShowEditModal(false);
                    setSelectedRecord(null);
                    resetForm();
                  }}
                  className="p-2 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
                >
                  <X className="h-5 w-5" />
                </button>
              </div>
              <form onSubmit={handleUpdate} className="p-6 space-y-4">
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Record Type *
                  </label>
                  <select
                    value={formData.recordType}
                    onChange={(e) =>
                      setFormData({ ...formData, recordType: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  >
                    <option value="">Select Type</option>
                    {recordTypes.map((type) => (
                      <option key={type} value={type}>
                        {type}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Title *
                  </label>
                  <input
                    type="text"
                    value={formData.title}
                    onChange={(e) =>
                      setFormData({ ...formData, title: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    placeholder="Enter record title"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Content
                  </label>
                  <textarea
                    value={formData.content}
                    onChange={(e) =>
                      setFormData({ ...formData, content: e.target.value })
                    }
                    rows={6}
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    placeholder="Enter record content..."
                  />
                </div>
                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => {
                      setShowEditModal(false);
                      setSelectedRecord(null);
                      resetForm();
                    }}
                    className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    disabled={submitting}
                    className="btn-primary flex items-center"
                  >
                    {submitting ? (
                      <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                    ) : (
                      <Save className="h-4 w-4 mr-2" />
                    )}
                    Update Record
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}

export default withAuth(MedicalRecordsPage, ["DOCTOR", "ADMIN"]);
