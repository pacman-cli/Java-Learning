"use client";

import React, { useEffect, useState } from "react";
import { withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  DollarSign,
  CreditCard,
  FileText,
  Clock,
  CheckCircle,
  XCircle,
  Search,
  Calendar,
  Download,
  Eye,
  AlertCircle,
} from "lucide-react";
import { billingApi, patientsApi } from "@/services/api";
import toast from "react-hot-toast";
import { useAuth } from "@/providers/AuthProvider";

interface Billing {
  id: number;
  amount: number;
  billingDate: string;
  paymentMethod?: string;
  patientId: number;
  appointmentId?: number;
  status: "PENDING" | "PAID" | "OVERDUE" | "CANCELLED";
  description?: string;
  paidAt?: string;
  insuranceId?: number;
  coveredAmount?: number;
  patientPayable?: number;
}

function MyBillingPage() {
  const { user } = useAuth();
  const [billings, setBillings] = useState<Billing[]>([]);
  const [loading, setLoading] = useState(true);
  const [patientId, setPatientId] = useState<number | null>(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [selectedBilling, setSelectedBilling] = useState<Billing | null>(null);
  const [showDetailModal, setShowDetailModal] = useState(false);

  // First, fetch patient ID from user ID
  useEffect(() => {
    const fetchPatientId = async () => {
      if (!user?.id) return;

      try {
        const patientData = await patientsApi.getByUserId(user.id);
        setPatientId(patientData.id);
      } catch (error) {
        console.error("Error fetching patient data:", error);
        toast.error("Failed to load patient information");
      }
    };

    fetchPatientId();
  }, [user?.id]);

  // Then fetch billings when we have patient ID
  useEffect(() => {
    if (patientId) {
      fetchBillings();
    }
  }, [patientId]);

  const fetchBillings = async () => {
    if (!patientId) return;

    try {
      setLoading(true);
      const data = await billingApi.getByPatient(patientId);
      setBillings(data);
    } catch (error) {
      console.error("Error fetching billings:", error);
      toast.error("Failed to load billing information");
    } finally {
      setLoading(false);
    }
  };

  const handlePayBill = async (billId: number) => {
    if (!confirm("Mark this bill as paid?")) return;

    try {
      await billingApi.markAsPaid(billId, "ONLINE");
      toast.success("Bill marked as paid successfully");
      fetchBillings();
    } catch (error) {
      console.error("Error marking bill as paid:", error);
      toast.error("Failed to process payment");
    }
  };

  const handleViewDetails = (billing: Billing) => {
    setSelectedBilling(billing);
    setShowDetailModal(true);
  };

  const formatCurrency = (amount?: number) => {
    if (amount === undefined || amount === null) return "$0.00";
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
    }).format(amount);
  };

  const formatDate = (dateString?: string) => {
    if (!dateString) return "N/A";
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "PAID":
        return <CheckCircle className="h-5 w-5 text-green-600" />;
      case "PENDING":
        return <Clock className="h-5 w-5 text-yellow-600" />;
      case "OVERDUE":
        return <AlertCircle className="h-5 w-5 text-red-600" />;
      case "CANCELLED":
        return <XCircle className="h-5 w-5 text-gray-600" />;
      default:
        return <FileText className="h-5 w-5 text-gray-600" />;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "PAID":
        return "bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400";
      case "PENDING":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/30 dark:text-yellow-400";
      case "OVERDUE":
        return "bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-400";
      case "CANCELLED":
        return "bg-gray-100 text-gray-800 dark:bg-gray-900/30 dark:text-gray-400";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-900/30 dark:text-gray-400";
    }
  };

  const filteredBillings = billings.filter((billing) => {
    const matchesSearch =
      searchTerm === "" ||
      billing.description?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      billing.id.toString().includes(searchTerm);

    const matchesStatus =
      statusFilter === "ALL" || billing.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const totalBalance = billings
    .filter((b) => b.status === "PENDING" || b.status === "OVERDUE")
    .reduce((sum, b) => sum + (b.patientPayable || b.amount), 0);

  const pendingCount = billings.filter(
    (b) => b.status === "PENDING" || b.status === "OVERDUE",
  ).length;

  const paidThisYear = billings
    .filter((b) => {
      if (b.status !== "PAID" || !b.paidAt) return false;
      const paidDate = new Date(b.paidAt);
      return paidDate.getFullYear() === new Date().getFullYear();
    })
    .reduce((sum, b) => sum + (b.patientPayable || b.amount), 0);

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
            My Billing
          </h1>
          <p className="text-neutral-600 dark:text-neutral-400 mt-1">
            View and manage your medical bills and payments
          </p>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                  Total Balance
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {formatCurrency(totalBalance)}
                </p>
              </div>
              <div className="bg-blue-100 dark:bg-blue-900 p-3 rounded-lg">
                <DollarSign className="h-6 w-6 text-blue-600 dark:text-blue-400" />
              </div>
            </div>
          </div>

          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                  Pending Bills
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {pendingCount}
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
                  Paid This Year
                </p>
                <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                  {formatCurrency(paidThisYear)}
                </p>
              </div>
              <div className="bg-green-100 dark:bg-green-900 p-3 rounded-lg">
                <CreditCard className="h-6 w-6 text-green-600 dark:text-green-400" />
              </div>
            </div>
          </div>
        </div>

        {/* Filters */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
              <input
                type="text"
                placeholder="Search by description or ID..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
              />
            </div>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
            >
              <option value="ALL">All Status</option>
              <option value="PENDING">Pending</option>
              <option value="PAID">Paid</option>
              <option value="OVERDUE">Overdue</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
          </div>
        </div>

        {/* Billing List */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow">
          {loading ? (
            <div className="p-12 text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600 mx-auto"></div>
              <p className="text-neutral-600 dark:text-neutral-400 mt-4">
                Loading billings...
              </p>
            </div>
          ) : filteredBillings.length === 0 ? (
            <div className="p-12 text-center">
              <FileText className="h-12 w-12 text-neutral-400 mx-auto mb-4" />
              <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                No Bills Found
              </h3>
              <p className="text-neutral-600 dark:text-neutral-400">
                {searchTerm || statusFilter !== "ALL"
                  ? "Try adjusting your filters"
                  : "You don't have any billing records yet"}
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-neutral-50 dark:bg-neutral-900">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                      ID
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                      Date
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                      Description
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                      Amount
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                      Status
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 dark:text-neutral-400 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-neutral-200 dark:divide-neutral-700">
                  {filteredBillings.map((billing) => (
                    <tr
                      key={billing.id}
                      className="hover:bg-neutral-50 dark:hover:bg-neutral-700/50"
                    >
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-neutral-900 dark:text-neutral-100">
                        #{billing.id}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-neutral-600 dark:text-neutral-400">
                        <div className="flex items-center space-x-2">
                          <Calendar className="h-4 w-4" />
                          <span>{formatDate(billing.billingDate)}</span>
                        </div>
                      </td>
                      <td className="px-6 py-4 text-sm text-neutral-600 dark:text-neutral-400">
                        {billing.description || "Medical Services"}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-neutral-900 dark:text-neutral-100">
                        {formatCurrency(
                          billing.patientPayable || billing.amount,
                        )}
                        {billing.coveredAmount && billing.coveredAmount > 0 && (
                          <div className="text-xs text-green-600 dark:text-green-400">
                            Insurance: {formatCurrency(billing.coveredAmount)}
                          </div>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span
                          className={`inline-flex items-center space-x-1 px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(
                            billing.status,
                          )}`}
                        >
                          {getStatusIcon(billing.status)}
                          <span>{billing.status}</span>
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        <div className="flex items-center space-x-2">
                          <button
                            onClick={() => handleViewDetails(billing)}
                            className="text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300"
                            title="View Details"
                          >
                            <Eye className="h-5 w-5" />
                          </button>
                          {billing.status === "PENDING" && (
                            <button
                              onClick={() => handlePayBill(billing.id)}
                              className="text-green-600 hover:text-green-800 dark:text-green-400 dark:hover:text-green-300"
                              title="Mark as Paid"
                            >
                              <CreditCard className="h-5 w-5" />
                            </button>
                          )}
                          <button
                            onClick={() =>
                              toast.success("Download feature coming soon!")
                            }
                            className="text-neutral-600 hover:text-neutral-800 dark:text-neutral-400 dark:hover:text-neutral-300"
                            title="Download Invoice"
                          >
                            <Download className="h-5 w-5" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Detail Modal */}
        {showDetailModal && selectedBilling && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
              <div className="p-6">
                <div className="flex items-center justify-between mb-6">
                  <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                    Billing Details
                  </h2>
                  <button
                    onClick={() => setShowDetailModal(false)}
                    className="text-neutral-500 hover:text-neutral-700 dark:text-neutral-400 dark:hover:text-neutral-200"
                  >
                    <XCircle className="h-6 w-6" />
                  </button>
                </div>

                <div className="space-y-4">
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="text-sm text-neutral-600 dark:text-neutral-400">
                        Bill ID
                      </p>
                      <p className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                        #{selectedBilling.id}
                      </p>
                    </div>
                    <span
                      className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(
                        selectedBilling.status,
                      )}`}
                    >
                      {selectedBilling.status}
                    </span>
                  </div>

                  <div className="border-t border-neutral-200 dark:border-neutral-700 pt-4">
                    <h3 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-3">
                      Bill Information
                    </h3>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <p className="text-sm text-neutral-600 dark:text-neutral-400">
                          Billing Date
                        </p>
                        <p className="text-neutral-900 dark:text-neutral-100">
                          {formatDate(selectedBilling.billingDate)}
                        </p>
                      </div>
                      {selectedBilling.paidAt && (
                        <div>
                          <p className="text-sm text-neutral-600 dark:text-neutral-400">
                            Paid Date
                          </p>
                          <p className="text-neutral-900 dark:text-neutral-100">
                            {formatDate(selectedBilling.paidAt)}
                          </p>
                        </div>
                      )}
                      {selectedBilling.paymentMethod && (
                        <div>
                          <p className="text-sm text-neutral-600 dark:text-neutral-400">
                            Payment Method
                          </p>
                          <p className="text-neutral-900 dark:text-neutral-100">
                            {selectedBilling.paymentMethod}
                          </p>
                        </div>
                      )}
                    </div>
                  </div>

                  <div className="border-t border-neutral-200 dark:border-neutral-700 pt-4">
                    <h3 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-3">
                      Amount Breakdown
                    </h3>
                    <div className="space-y-2">
                      <div className="flex justify-between">
                        <span className="text-neutral-600 dark:text-neutral-400">
                          Total Amount
                        </span>
                        <span className="font-semibold text-neutral-900 dark:text-neutral-100">
                          {formatCurrency(selectedBilling.amount)}
                        </span>
                      </div>
                      {selectedBilling.coveredAmount &&
                        selectedBilling.coveredAmount > 0 && (
                          <div className="flex justify-between text-green-600 dark:text-green-400">
                            <span>Insurance Coverage</span>
                            <span className="font-semibold">
                              -{formatCurrency(selectedBilling.coveredAmount)}
                            </span>
                          </div>
                        )}
                      <div className="flex justify-between text-lg border-t border-neutral-200 dark:border-neutral-700 pt-2">
                        <span className="font-bold text-neutral-900 dark:text-neutral-100">
                          Patient Payable
                        </span>
                        <span className="font-bold text-neutral-900 dark:text-neutral-100">
                          {formatCurrency(
                            selectedBilling.patientPayable ||
                              selectedBilling.amount,
                          )}
                        </span>
                      </div>
                    </div>
                  </div>

                  {selectedBilling.description && (
                    <div className="border-t border-neutral-200 dark:border-neutral-700 pt-4">
                      <h3 className="text-sm font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                        Description
                      </h3>
                      <p className="text-neutral-600 dark:text-neutral-400 text-sm">
                        {selectedBilling.description}
                      </p>
                    </div>
                  )}

                  <div className="border-t border-neutral-200 dark:border-neutral-700 pt-4 flex justify-end space-x-3">
                    <button
                      onClick={() => setShowDetailModal(false)}
                      className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg text-neutral-700 dark:text-neutral-300 hover:bg-neutral-50 dark:hover:bg-neutral-700"
                    >
                      Close
                    </button>
                    {selectedBilling.status === "PENDING" && (
                      <button
                        onClick={() => {
                          handlePayBill(selectedBilling.id);
                          setShowDetailModal(false);
                        }}
                        className="px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg flex items-center space-x-2"
                      >
                        <CreditCard className="h-4 w-4" />
                        <span>Mark as Paid</span>
                      </button>
                    )}
                    <button
                      onClick={() =>
                        toast.success("Download feature coming soon!")
                      }
                      className="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg flex items-center space-x-2"
                    >
                      <Download className="h-4 w-4" />
                      <span>Download</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}

export default withAuth(MyBillingPage, ["PATIENT"]);
