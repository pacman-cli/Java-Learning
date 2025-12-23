"use client";

import React, { useState, useEffect } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import { withAuth } from "@/providers/AuthProvider";
import { patientsApi } from "@/services/api";
import toast from "react-hot-toast";
import { UserCheck, Search, Plus } from "lucide-react";

interface Patient {
  id: number;
  fullName: string;
  email?: string;
  phoneNumber?: string;
  dateOfBirth?: string;
}

function PatientsPage() {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    fetchPatients();
  }, []);

  const fetchPatients = async () => {
    try {
      setLoading(true);
      const data = await patientsApi.getAll();
      setPatients(data);
    } catch (error) {
      console.error("Error fetching patients:", error);
      toast.error("Failed to load patients");
    } finally {
      setLoading(false);
    }
  };

  const filteredPatients = patients.filter((patient) =>
    patient.fullName.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
              Patients
            </h1>
            <p className="text-neutral-600 dark:text-neutral-400 mt-1">
              Manage patient information
            </p>
          </div>
          <button
            onClick={() => toast.info("Add patient feature coming soon")}
            className="flex items-center space-x-2 px-4 py-2 bg-purple-600 hover:bg-purple-700 text-white rounded-lg"
          >
            <Plus className="h-5 w-5" />
            <span>Add Patient</span>
          </button>
        </div>

        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
            <input
              type="text"
              placeholder="Search patients..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg"
            />
          </div>
        </div>

        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow overflow-hidden">
          {loading ? (
            <div className="p-12 text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600 mx-auto"></div>
              <p className="text-neutral-600 dark:text-neutral-400 mt-4">Loading patients...</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-neutral-50 dark:bg-neutral-900">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 uppercase">Name</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 uppercase">Contact</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-neutral-500 uppercase">DOB</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-neutral-200 dark:divide-neutral-700">
                  {filteredPatients.map((patient) => (
                    <tr key={patient.id}>
                      <td className="px-6 py-4">{patient.fullName}</td>
                      <td className="px-6 py-4">{patient.email || patient.phoneNumber || "N/A"}</td>
                      <td className="px-6 py-4">{patient.dateOfBirth ? new Date(patient.dateOfBirth).toLocaleDateString() : "N/A"}</td>
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

export default withAuth(PatientsPage, ["ADMIN"]);
