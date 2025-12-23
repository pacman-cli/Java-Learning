"use client";

import React, { useState, useEffect } from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  Activity,
  Plus,
  Search,
  Filter,
  Eye,
  Edit,
  Trash2,
  Upload,
  X,
  Calendar,
  User,
  FileText,
  Save,
  Loader2,
  CheckCircle,
  Clock,
  XCircle,
  AlertCircle,
} from "lucide-react";
import { labOrdersApi, labTestsApi, patientsApi } from "@/services/api";
import toast from "react-hot-toast";

interface LabOrder {
  id: number;
  labTestId: number;
  patientId: number;
  appointmentId?: number;
  orderedAt: string;
  status: string;
  reportPath?: string;
  notes?: string;
  testName?: string;
  patientName?: string;
}

interface LabTest {
  id: number;
  testName: string;
  description?: string;
  price?: number;
}

interface Patient {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

function LabRequestsPage() {
  const { user } = useAuth();
  const [orders, setOrders] = useState<LabOrder[]>([]);
  const [labTests, setLabTests] = useState<LabTest[]>([]);
  const [patients, setPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [filterStatus, setFilterStatus] = useState("ALL");
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showViewModal, setShowViewModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState<LabOrder | null>(null);
  const [uploadingFile, setUploadingFile] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const [formData, setFormData] = useState({
    labTestId: "",
    patientId: "",
    appointmentId: "",
    notes: "",
  });

  const statuses = [
    { value: "PENDING", label: "Pending", color: "yellow" },
    { value: "IN_PROGRESS", label: "In Progress", color: "blue" },
    { value: "COMPLETED", label: "Completed", color: "green" },
    { value: "CANCELLED", label: "Cancelled", color: "red" },
  ];

  useEffect(() => {
    fetchOrders();
    fetchLabTests();
    fetchPatients();
  }, []);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const data = await labOrdersApi.getAll();
      setOrders(Array.isArray(data) ? data : []);
    } catch (error: any) {
      console.error("Failed to fetch lab orders:", error);
      toast.error("Failed to load lab orders");
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  const fetchLabTests = async () => {
    try {
      const data = await labTestsApi.getAll();
      setLabTests(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error("Failed to fetch lab tests:", error);
      setLabTests([]);
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
    if (!formData.labTestId || !formData.patientId) {
      toast.error("Please fill in all required fields");
      return;
    }

    try {
      setSubmitting(true);
      const newOrder = await labOrdersApi.create({
        labTestId: parseInt(formData.labTestId),
        patientId: parseInt(formData.patientId),
        appointmentId: formData.appointmentId
          ? parseInt(formData.appointmentId)
          : undefined,
        notes: formData.notes,
      });
      toast.success("Lab request created successfully");
      setOrders([newOrder, ...orders]);
      setShowCreateModal(false);
      resetForm();
    } catch (error: any) {
      console.error("Failed to create lab order:", error);
      toast.error(
        error.response?.data?.message || "Failed to create lab order",
      );
    } finally {
      setSubmitting(false);
    }
  };

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedOrder) return;

    try {
      setSubmitting(true);
      const updated = await labOrdersApi.update(selectedOrder.id, {
        labTestId: parseInt(formData.labTestId),
        patientId: parseInt(formData.patientId),
        appointmentId: formData.appointmentId
          ? parseInt(formData.appointmentId)
          : undefined,
        notes: formData.notes,
      });
      toast.success("Lab request updated successfully");
      setOrders(orders.map((o) => (o.id === selectedOrder.id ? updated : o)));
      setShowEditModal(false);
      setSelectedOrder(null);
      resetForm();
    } catch (error: any) {
      console.error("Failed to update lab order:", error);
      toast.error(
        error.response?.data?.message || "Failed to update lab order",
      );
    } finally {
      setSubmitting(false);
    }
  };

  const handleStatusChange = async (id: number, newStatus: string) => {
    try {
      const updated = await labOrdersApi.changeStatus(id, newStatus);
      toast.success("Status updated successfully");
      setOrders(orders.map((o) => (o.id === id ? updated : o)));
    } catch (error: any) {
      console.error("Failed to update status:", error);
      toast.error(error.response?.data?.message || "Failed to update status");
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Are you sure you want to delete this lab request?")) return;

    try {
      await labOrdersApi.delete(id);
      toast.success("Lab request deleted successfully");
      setOrders(orders.filter((o) => o.id !== id));
    } catch (error: any) {
      console.error("Failed to delete lab order:", error);
      toast.error(
        error.response?.data?.message || "Failed to delete lab order",
      );
    }
  };

  const handleFileUpload = async (id: number, file: File) => {
    try {
      setUploadingFile(true);
      await labOrdersApi.attachReport(id, file);
      toast.success("Report uploaded successfully");
      fetchOrders();
    } catch (error: any) {
      console.error("Failed to upload report:", error);
      toast.error(error.response?.data?.message || "Failed to upload report");
    } finally {
      setUploadingFile(false);
    }
  };

  const resetForm = () => {
    setFormData({
      labTestId: "",
      patientId: "",
      appointmentId: "",
      notes: "",
    });
  };

  const openViewModal = (order: LabOrder) => {
    setSelectedOrder(order);
    setShowViewModal(true);
  };

  const openEditModal = (order: LabOrder) => {
    setSelectedOrder(order);
    setFormData({
      labTestId: order.labTestId.toString(),
      patientId: order.patientId.toString(),
      appointmentId: order.appointmentId?.toString() || "",
      notes: order.notes || "",
    });
    setShowEditModal(true);
  };

  const filteredOrders = orders.filter((order) => {
    const matchesSearch =
      order.testName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.patientName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.notes?.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesFilter =
      filterStatus === "ALL" || order.status === filterStatus;

    return matchesSearch && matchesFilter;
  });

  const getPatientName = (patientId: number) => {
    const patient = patients.find((p) => p.id === patientId);
    return patient ? `${patient.firstName} ${patient.lastName}` : "Unknown";
  };

  const getTestName = (testId: number) => {
    const test = labTests.find((t) => t.id === testId);
    return test ? test.testName : "Unknown Test";
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "PENDING":
        return <Clock className="h-5 w-5" />;
      case "IN_PROGRESS":
        return <AlertCircle className="h-5 w-5" />;
      case "COMPLETED":
        return <CheckCircle className="h-5 w-5" />;
      case "CANCELLED":
        return <XCircle className="h-5 w-5" />;
      default:
        return <Clock className="h-5 w-5" />;
    }
  };

  const getStatusColor = (status: string) => {
    const statusObj = statuses.find((s) => s.value === status);
    return statusObj?.color || "gray";
  };

  const getStatusLabel = (status: string) => {
    const statusObj = statuses.find((s) => s.value === status);
    return statusObj?.label || status;
  };

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 flex items-center">
              <Activity className="h-8 w-8 mr-3 text-teal-600" />
              Lab Requests
            </h1>
            <p className="mt-2 text-neutral-600 dark:text-neutral-400">
              Manage laboratory test requests and results
            </p>
          </div>
          <button
            onClick={() => setShowCreateModal(true)}
            className="btn-primary btn-sm flex items-center space-x-2"
          >
            <Plus className="h-4 w-4" />
            <span>New Lab Request</span>
          </button>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          {statuses.map((status) => {
            const count = orders.filter(
              (o) => o.status === status.value,
            ).length;
            const colorClass =
              status.color === "yellow"
                ? "bg-yellow-100 dark:bg-yellow-900 text-yellow-700 dark:text-yellow-300"
                : status.color === "blue"
                  ? "bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300"
                  : status.color === "green"
                    ? "bg-green-100 dark:bg-green-900 text-green-700 dark:text-green-300"
                    : "bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-300";

            return (
              <div
                key={status.value}
                className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4"
              >
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-neutral-600 dark:text-neutral-400">
                      {status.label}
                    </p>
                    <p className="text-2xl font-bold text-neutral-900 dark:text-neutral-100 mt-1">
                      {count}
                    </p>
                  </div>
                  <div className={`p-3 rounded-lg ${colorClass}`}>
                    {getStatusIcon(status.value)}
                  </div>
                </div>
              </div>
            );
          })}
        </div>

        {/* Filters */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                <input
                  type="text"
                  placeholder="Search lab requests..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                />
              </div>
            </div>
            <div className="flex items-center space-x-2">
              <Filter className="h-5 w-5 text-neutral-400" />
              <select
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
                className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
              >
                <option value="ALL">All Status</option>
                {statuses.map((status) => (
                  <option key={status.value} value={status.value}>
                    {status.label}
                  </option>
                ))}
              </select>
            </div>
          </div>
        </div>

        {/* Orders List */}
        {loading ? (
          <div className="flex justify-center items-center h-64">
            <Loader2 className="h-8 w-8 animate-spin text-teal-600" />
          </div>
        ) : filteredOrders.length === 0 ? (
          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-8 text-center">
            <Activity className="h-16 w-16 mx-auto text-neutral-400 mb-4" />
            <h3 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
              No Lab Requests Found
            </h3>
            <p className="text-neutral-600 dark:text-neutral-400 mb-4">
              {searchQuery || filterStatus !== "ALL"
                ? "No lab requests match your search criteria"
                : "Start by creating your first lab request"}
            </p>
            <button
              onClick={() => setShowCreateModal(true)}
              className="btn-primary"
            >
              <Plus className="h-4 w-4 mr-2" />
              New Lab Request
            </button>
          </div>
        ) : (
          <div className="grid gap-4">
            {filteredOrders.map((order) => {
              const statusColor = getStatusColor(order.status);
              const statusColorClass =
                statusColor === "yellow"
                  ? "bg-yellow-100 dark:bg-yellow-900 text-yellow-700 dark:text-yellow-300"
                  : statusColor === "blue"
                    ? "bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300"
                    : statusColor === "green"
                      ? "bg-green-100 dark:bg-green-900 text-green-700 dark:text-green-300"
                      : "bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-300";

              return (
                <div
                  key={order.id}
                  className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow"
                >
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <div className="flex items-center space-x-3 mb-2">
                        <span
                          className={`px-3 py-1 rounded-full text-sm font-medium flex items-center space-x-1 ${statusColorClass}`}
                        >
                          {getStatusIcon(order.status)}
                          <span className="ml-1">
                            {getStatusLabel(order.status)}
                          </span>
                        </span>
                        {order.reportPath && (
                          <span className="px-2 py-1 bg-teal-100 dark:bg-teal-900 text-teal-700 dark:text-teal-300 rounded text-xs flex items-center">
                            <FileText className="h-3 w-3 mr-1" />
                            Report Available
                          </span>
                        )}
                      </div>
                      <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                        {getTestName(order.labTestId)}
                      </h3>
                      {order.notes && (
                        <p className="text-neutral-600 dark:text-neutral-400 mb-3 line-clamp-2">
                          {order.notes}
                        </p>
                      )}
                      <div className="flex items-center space-x-4 text-sm text-neutral-500 dark:text-neutral-400">
                        <span className="flex items-center">
                          <User className="h-4 w-4 mr-1" />
                          {getPatientName(order.patientId)}
                        </span>
                        <span className="flex items-center">
                          <Calendar className="h-4 w-4 mr-1" />
                          {new Date(order.orderedAt).toLocaleDateString()}
                        </span>
                      </div>
                    </div>
                    <div className="flex items-center space-x-2 ml-4">
                      <select
                        value={order.status}
                        onChange={(e) =>
                          handleStatusChange(order.id, e.target.value)
                        }
                        className="px-3 py-1 text-sm border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                      >
                        {statuses.map((status) => (
                          <option key={status.value} value={status.value}>
                            {status.label}
                          </option>
                        ))}
                      </select>
                      <button
                        onClick={() => openViewModal(order)}
                        className="p-2 text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition"
                        title="View"
                      >
                        <Eye className="h-5 w-5" />
                      </button>
                      <button
                        onClick={() => openEditModal(order)}
                        className="p-2 text-amber-600 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition"
                        title="Edit"
                      >
                        <Edit className="h-5 w-5" />
                      </button>
                      <label
                        className="p-2 text-teal-600 hover:bg-teal-50 dark:hover:bg-teal-900/20 rounded-lg transition cursor-pointer"
                        title="Upload Report"
                      >
                        <Upload className="h-5 w-5" />
                        <input
                          type="file"
                          className="hidden"
                          onChange={(e) => {
                            const file = e.target.files?.[0];
                            if (file) handleFileUpload(order.id, file);
                          }}
                        />
                      </label>
                      <button
                        onClick={() => handleDelete(order.id)}
                        className="p-2 text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition"
                        title="Delete"
                      >
                        <Trash2 className="h-5 w-5" />
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}

        {/* Create Modal */}
        {showCreateModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                  Create Lab Request
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
                    Lab Test *
                  </label>
                  <select
                    value={formData.labTestId}
                    onChange={(e) =>
                      setFormData({ ...formData, labTestId: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  >
                    <option value="">Select Lab Test</option>
                    {labTests.map((test) => (
                      <option key={test.id} value={test.id}>
                        {test.testName}
                        {test.price && ` - $${test.price}`}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Appointment ID (Optional)
                  </label>
                  <input
                    type="number"
                    value={formData.appointmentId}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        appointmentId: e.target.value,
                      })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    placeholder="Enter appointment ID"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Notes
                  </label>
                  <textarea
                    value={formData.notes}
                    onChange={(e) =>
                      setFormData({ ...formData, notes: e.target.value })
                    }
                    rows={4}
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    placeholder="Enter any additional notes..."
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
                    Create Request
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* View Modal */}
        {showViewModal && selectedOrder && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                  Lab Request Details
                </h2>
                <button
                  onClick={() => {
                    setShowViewModal(false);
                    setSelectedOrder(null);
                  }}
                  className="p-2 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
                >
                  <X className="h-5 w-5" />
                </button>
              </div>
              <div className="p-6 space-y-4">
                <div>
                  <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                    Status
                  </label>
                  <span
                    className={`px-3 py-1 rounded-full text-sm font-medium inline-flex items-center space-x-1 ${
                      getStatusColor(selectedOrder.status) === "yellow"
                        ? "bg-yellow-100 dark:bg-yellow-900 text-yellow-700 dark:text-yellow-300"
                        : getStatusColor(selectedOrder.status) === "blue"
                          ? "bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300"
                          : getStatusColor(selectedOrder.status) === "green"
                            ? "bg-green-100 dark:bg-green-900 text-green-700 dark:text-green-300"
                            : "bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-300"
                    }`}
                  >
                    {getStatusIcon(selectedOrder.status)}
                    <span className="ml-1">
                      {getStatusLabel(selectedOrder.status)}
                    </span>
                  </span>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                    Lab Test
                  </label>
                  <p className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                    {getTestName(selectedOrder.labTestId)}
                  </p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                    Patient
                  </label>
                  <p className="text-neutral-900 dark:text-neutral-100">
                    {getPatientName(selectedOrder.patientId)}
                  </p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                    Ordered Date
                  </label>
                  <p className="text-neutral-900 dark:text-neutral-100">
                    {new Date(selectedOrder.orderedAt).toLocaleDateString(
                      "en-US",
                      {
                        year: "numeric",
                        month: "long",
                        day: "numeric",
                        hour: "2-digit",
                        minute: "2-digit",
                      },
                    )}
                  </p>
                </div>
                {selectedOrder.appointmentId && (
                  <div>
                    <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Appointment ID
                    </label>
                    <p className="text-neutral-900 dark:text-neutral-100">
                      #{selectedOrder.appointmentId}
                    </p>
                  </div>
                )}
                {selectedOrder.notes && (
                  <div>
                    <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Notes
                    </label>
                    <div className="bg-neutral-50 dark:bg-neutral-900 rounded-lg p-4">
                      <p className="text-neutral-900 dark:text-neutral-100 whitespace-pre-wrap">
                        {selectedOrder.notes}
                      </p>
                    </div>
                  </div>
                )}
                {selectedOrder.reportPath && (
                  <div>
                    <label className="block text-sm font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Report
                    </label>
                    <div className="flex items-center space-x-2">
                      <FileText className="h-5 w-5 text-teal-600" />
                      <span className="text-neutral-900 dark:text-neutral-100">
                        {selectedOrder.reportPath}
                      </span>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}

        {/* Edit Modal */}
        {showEditModal && selectedOrder && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                  Edit Lab Request
                </h2>
                <button
                  onClick={() => {
                    setShowEditModal(false);
                    setSelectedOrder(null);
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
                    Lab Test *
                  </label>
                  <select
                    value={formData.labTestId}
                    onChange={(e) =>
                      setFormData({ ...formData, labTestId: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  >
                    <option value="">Select Lab Test</option>
                    {labTests.map((test) => (
                      <option key={test.id} value={test.id}>
                        {test.testName}
                        {test.price && ` - $${test.price}`}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Appointment ID (Optional)
                  </label>
                  <input
                    type="number"
                    value={formData.appointmentId}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        appointmentId: e.target.value,
                      })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    placeholder="Enter appointment ID"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Notes
                  </label>
                  <textarea
                    value={formData.notes}
                    onChange={(e) =>
                      setFormData({ ...formData, notes: e.target.value })
                    }
                    rows={4}
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    placeholder="Enter any additional notes..."
                  />
                </div>
                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => {
                      setShowEditModal(false);
                      setSelectedOrder(null);
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
                    Update Request
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

export default withAuth(LabRequestsPage, ["DOCTOR", "ADMIN"]);
