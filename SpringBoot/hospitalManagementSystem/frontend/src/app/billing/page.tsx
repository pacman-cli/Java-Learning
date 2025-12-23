"use client";

import React, { useState, useEffect } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import { withAuth } from "@/providers/AuthProvider";
import { billingApi } from "@/services/api";
import toast from "react-hot-toast";
import { DollarSign, Search } from "lucide-react";

interface Billing {
  id: number;
  patientId: number;
  amount: number;
  status: string;
  billingDate: string;
}

function BillingPage() {
  const [billings, setBillings] = useState<Billing[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBillings();
  }, []);

  const fetchBillings = async () => {
    try {
      setLoading(true);
      const data = await billingApi.getAll();
      setBillings(data);
    } catch (error) {
      console.error("Error fetching billings:", error);
      toast.error("Failed to load billing data");
    } finally {
      setLoading(false);
    }
  };

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
            Billing Management
          </h1>
          <p className="text-neutral-600 dark:text-neutral-400 mt-1">
            Manage billing and invoices
          </p>
        </div>

        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow overflow-hidden">
          {loading ? (
            <div className="p-12 text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-600 mx-auto"></div>
              <p className="text-neutral-600 dark:text-neutral-400 mt-4">Loading billings...</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-neutral-50 dark:bg-neutral-900">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 uppercase">ID</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 uppercase">Patient ID</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 uppercase">Amount</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 uppercase">Status</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 uppercase">Date</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-neutral-200 dark:divide-neutral-700">
                  {billings.map((bill) => (
                    <tr key={bill.id}>
                      <td className="px-6 py-4">#{bill.id}</td>
                      <td className="px-6 py-4">{bill.patientId}</td>
                      <td className="px-6 py-4">${bill.amount.toFixed(2)}</td>
                      <td className="px-6 py-4">{bill.status}</td>
                      <td className="px-6 py-4">{new Date(bill.billingDate).toLocaleDateString()}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </DashboardLayout>
  );
}

export default withAuth(BillingPage, ["ADMIN"]);
