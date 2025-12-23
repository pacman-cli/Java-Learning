"use client";

import React, { useState, useEffect } from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  Activity,
  Download,
  Eye,
  Calendar,
  FileText,
  Search,
  Filter,
  X,
  AlertCircle,
  CheckCircle,
  Clock,
  Beaker,
} from "lucide-react";
import { labOrdersApi } from "@/services/api";

interface LabOrder {
  id: number;
  patientId: number;
  doctorId: number;
  labTestId: number;
  orderDate: string;
  status: string;
  priority?: string;
  notes?: string;
  results?: string;
  reportUrl?: string;
  completedDate?: string;
  doctorName?: string;
  testName?: string;
}

function MyLabReportsPage() {
  const { user } = useAuth();
  const [labOrders, setLabOrders] = useState<LabOrder[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState<LabOrder | null>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchLabOrders();
  }, []);

  const fetchLabOrders = async () => {
    try {
      setLoading(true);
      setError("");
      if (!user?.id) {
        setError("User not found");
        return;
      }
      const data = await labOrdersApi.getByPatient(user.id);
      setLabOrders(data);
    } catch (err) {
      console.error("Error fetching lab orders:", err);
      setError("Failed to load lab reports");
    } finally {
      setLoading(false);
    }
  };

  const openDetailsModal = (order: LabOrder) => {
    setSelectedOrder(order);
    setShowDetailsModal(true);
  };

  const handleDownload = (order: LabOrder) => {
    const content = `
LAB REPORT
==========

Order ID: ${order.id}
Test: ${order.testName || `Test #${order.labTestId}`}
Order Date: ${formatDate(order.orderDate)}
${order.completedDate ? `Completed Date: ${formatDate(order.completedDate)}` : ""}

Status: ${order.status}
Priority: ${order.priority || "NORMAL"}

Doctor: ${order.doctorName || `Dr. ${order.doctorId}`}
Patient: ${user?.fullName || "Patient"}

${order.notes ? `NOTES:\n${order.notes}\n` : ""}
${order.results ? `RESULTS:\n${order.results}\n` : "Results pending..."}

---
Generated on: ${new Date().toLocaleString()}
    `.trim();

    const blob = new Blob([content], { type: "text/plain" });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `lab-report-${order.id}-${order.orderDate}.txt`;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  };

  const filteredOrders = labOrders.filter((order) => {
    const matchesSearch =
      order.testName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.doctorName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.notes?.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesStatus =
      statusFilter === "ALL" || order.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const pendingOrders = filteredOrders.filter(
    (order) => order.status === "PENDING" || order.status === "IN_PROGRESS",
  );

  const completedOrders = filteredOrders.filter(
    (order) => order.status === "COMPLETED",
  );

  const getStatusColor = (status: string) => {
    switch (status) {
      case "PENDING":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200";
      case "IN_PROGRESS":
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
      case "PENDING":
        return <Clock className="h-4 w-4" />;
      case "IN_PROGRESS":
        return <Activity className="h-4 w-4" />;
      case "COMPLETED":
        return <CheckCircle className="h-4 w-4" />;
      case "CANCELLED":
        return <X className="h-4 w-4" />;
      default:
        return <AlertCircle className="h-4 w-4" />;
    }
  };

  const getPriorityColor = (priority?: string) => {
    switch (priority) {
      case "URGENT":
        return "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200";
      case "HIGH":
        return "bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200";
      case "NORMAL":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200";
      case "LOW":
        return "bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200";
      default:
        return "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200";
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

  const LabOrderCard = ({ order }: { order: LabOrder }) => (
    <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
      <div className="flex items-start justify-between mb-4">
        <div className="flex-1">
          <div className="flex items-center space-x-2 mb-2">
            <Beaker className="h-5 w-5 text-primary-600 dark:text-primary-400" />
            <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
              {order.testName || `Lab Test #${order.labTestId}`}
            </h3>
          </div>
          <p className="text-sm text-neutral-600 dark:text-neutral-400 mb-2">
            Order #{order.id}
          </p>
        </div>
        <div className="flex flex-col items-end space-y-2">
          <span
            className={`inline-flex items-center space-x-1 px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
              order.status,
            )}`}
          >
            {getStatusIcon(order.status)}
            <span>{order.status}</span>
          </span>
          {order.priority && (
            <span
              className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getPriorityColor(
                order.priority,
              )}`}
            >
              {order.priority}
            </span>
          )}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4 mb-4">
        <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
          <Calendar className="h-4 w-4" />
          <span>{formatDate(order.orderDate)}</span>
        </div>
        {order.completedDate && (
          <div className="flex items-center space-x-2 text-sm text-neutral-600 dark:text-neutral-400">
            <CheckCircle className="h-4 w-4" />
            <span>{formatDate(order.completedDate)}</span>
          </div>
        )}
      </div>

      {order.notes && (
        <div className="mb-4">
          <p className="text-sm text-neutral-600 dark:text-neutral-400">
            {order.notes.length > 100
              ? `${order.notes.substring(0, 100)}...`
              : order.notes}
          </p>
        </div>
      )}

      <div className="flex items-center space-x-2 pt-4 border-t border-neutral-200 dark:border-neutral-700">
        <button
          onClick={() => openDetailsModal(order)}
          className="flex-1 flex items-center justify-center space-x-2 px-4 py-2 text-sm font-medium text-primary-600 dark:text-primary-400 bg-primary-50 dark:bg-primary-900/20 rounded-lg hover:bg-primary-100 dark:hover:bg-primary-900/30 transition-colors"
        >
          <Eye className="h-4 w-4" />
          <span>View Details</span>
        </button>
        {order.status === "COMPLETED" && (
          <button
            onClick={() => handleDownload(order)}
            className="flex items-center justify-center space-x-2 px-4 py-2 text-sm font-medium text-green-600 dark:text-green-400 bg-green-50 dark:bg-green-900/20 rounded-lg hover:bg-green-100 dark:hover:bg-green-900/30 transition-colors"
          >
            <Download className="h-4 w-4" />
            <span>Download</span>
          </button>
        )}
      </div>
    </div>
  );

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
            My Lab Reports
          </h1>
          <p className="text-neutral-600 dark:text-neutral-400 mt-1">
            View your lab test results and reports
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
                  Total Tests
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {labOrders.length}
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
                  Pending
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {pendingOrders.length}
                </p>
              </div>
              <div className="bg-yellow-100 dark:bg-yellow-900 p-3 rounded-lg">
                <Clock className="h-6 w-6 text-yellow-600 dark:text-yellow-400" />
              </div>
            </div>
          </div>

          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                  Completed
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {completedOrders.length}
                </p>
              </div>
              <div className="bg-green-100 dark:bg-green-900 p-3 rounded-lg">
                <CheckCircle className="h-6 w-6 text-green-600 dark:text-green-400" />
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
                placeholder="Search by test name, doctor, or notes..."
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
                <option value="PENDING">Pending</option>
                <option value="IN_PROGRESS">In Progress</option>
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
              Loading lab reports...
            </p>
          </div>
        ) : (
          <>
            {/* Pending Orders */}
            {pendingOrders.length > 0 && (
              <div>
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                  Pending Tests ({pendingOrders.length})
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {pendingOrders.map((order) => (
                    <LabOrderCard key={order.id} order={order} />
                  ))}
                </div>
              </div>
            )}

            {/* Completed Orders */}
            <div>
              <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                Completed Tests ({completedOrders.length})
              </h2>
              {completedOrders.length === 0 ? (
                <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-8 text-center">
                  <FileText className="h-12 w-12 text-neutral-400 mx-auto mb-3" />
                  <p className="text-neutral-600 dark:text-neutral-400">
                    No completed lab reports yet
                  </p>
                </div>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {completedOrders.map((order) => (
                    <LabOrderCard key={order.id} order={order} />
                  ))}
                </div>
              )}
            </div>
          </>
        )}

        {/* Details Modal */}
        {showDetailsModal && selectedOrder && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100">
                  Lab Report Details
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
                  <div>
                    <h3 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100">
                      {selectedOrder.testName ||
                        `Lab Test #${selectedOrder.labTestId}`}
                    </h3>
                    <p className="text-sm text-neutral-600 dark:text-neutral-400 mt-1">
                      Order #{selectedOrder.id}
                    </p>
                  </div>
                  <div className="flex flex-col items-end space-y-2">
                    <span
                      className={`inline-flex items-center space-x-1 px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(
                        selectedOrder.status,
                      )}`}
                    >
                      {getStatusIcon(selectedOrder.status)}
                      <span>{selectedOrder.status}</span>
                    </span>
                    {selectedOrder.priority && (
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getPriorityColor(
                          selectedOrder.priority,
                        )}`}
                      >
                        {selectedOrder.priority}
                      </span>
                    )}
                  </div>
                </div>

                {/* Meta Information */}
                <div className="grid grid-cols-2 gap-4 p-4 bg-neutral-50 dark:bg-neutral-700/50 rounded-lg">
                  <div>
                    <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Order Date
                    </p>
                    <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                      {formatDate(selectedOrder.orderDate)}
                    </p>
                  </div>
                  {selectedOrder.completedDate && (
                    <div>
                      <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                        Completed Date
                      </p>
                      <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                        {formatDate(selectedOrder.completedDate)}
                      </p>
                    </div>
                  )}
                  <div>
                    <p className="text-xs font-medium text-neutral-500 dark:text-neutral-400 mb-1">
                      Ordered By
                    </p>
                    <p className="text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                      {selectedOrder.doctorName ||
                        `Dr. ${selectedOrder.doctorId}`}
                    </p>
                  </div>
                </div>

                {/* Notes */}
                {selectedOrder.notes && (
                  <div>
                    <h4 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                      Notes
                    </h4>
                    <p className="text-sm text-neutral-700 dark:text-neutral-300 bg-neutral-50 dark:bg-neutral-700/50 p-3 rounded-lg whitespace-pre-wrap">
                      {selectedOrder.notes}
                    </p>
                  </div>
                )}

                {/* Results */}
                {selectedOrder.results ? (
                  <div>
                    <h4 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-2 flex items-center space-x-2">
                      <CheckCircle className="h-4 w-4 text-green-600" />
                      <span>Results</span>
                    </h4>
                    <p className="text-sm text-neutral-700 dark:text-neutral-300 bg-green-50 dark:bg-green-900/20 p-3 rounded-lg whitespace-pre-wrap border border-green-200 dark:border-green-800">
                      {selectedOrder.results}
                    </p>
                  </div>
                ) : (
                  <div className="bg-yellow-50 dark:bg-yellow-900/20 border border-yellow-200 dark:border-yellow-800 rounded-lg p-4">
                    <div className="flex items-center space-x-2">
                      <Clock className="h-5 w-5 text-yellow-600 dark:text-yellow-400" />
                      <p className="text-sm text-yellow-800 dark:text-yellow-200">
                        Results are not yet available. Please check back later.
                      </p>
                    </div>
                  </div>
                )}

                {selectedOrder.reportUrl && (
                  <div>
                    <h4 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                      Report File
                    </h4>
                    <a
                      href={selectedOrder.reportUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="inline-flex items-center space-x-2 text-sm text-primary-600 dark:text-primary-400 hover:underline"
                    >
                      <FileText className="h-4 w-4" />
                      <span>View Full Report</span>
                    </a>
                  </div>
                )}
              </div>

              <div className="p-6 border-t border-neutral-200 dark:border-neutral-700 flex space-x-3">
                {selectedOrder.status === "COMPLETED" && (
                  <button
                    onClick={() => handleDownload(selectedOrder)}
                    className="flex-1 flex items-center justify-center space-x-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                  >
                    <Download className="h-4 w-4" />
                    <span>Download Report</span>
                  </button>
                )}
                <button
                  onClick={() => setShowDetailsModal(false)}
                  className="flex-1 px-4 py-2 text-neutral-700 dark:text-neutral-300 bg-neutral-100 dark:bg-neutral-700 rounded-lg hover:bg-neutral-200 dark:hover:bg-neutral-600 transition-colors"
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

export default withAuth(MyLabReportsPage, ["PATIENT"]);
