"use client";

import React from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import { withAuth } from "@/providers/AuthProvider";
import { Building2, Plus } from "lucide-react";
import toast from "react-hot-toast";

function DepartmentsPage() {
  const departments = [
    { name: "Cardiology", head: "Dr. Sarah Smith", staff: 15, patients: 45 },
    { name: "Neurology", head: "Dr. Michael Jones", staff: 12, patients: 38 },
    { name: "Pediatrics", head: "Dr. Emily Davis", staff: 18, patients: 52 },
    { name: "Orthopedics", head: "Dr. James Wilson", staff: 10, patients: 30 },
    { name: "Emergency", head: "Dr. Lisa Brown", staff: 25, patients: 100 },
  ];

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
              Departments
            </h1>
            <p className="text-neutral-600 dark:text-neutral-400 mt-1">
              Manage hospital departments
            </p>
          </div>
          <button
            onClick={() => toast.info("Add department feature coming soon")}
            className="flex items-center space-x-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg"
          >
            <Plus className="h-5 w-5" />
            <span>Add Department</span>
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {departments.map((dept, index) => (
            <div
              key={index}
              className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow"
            >
              <div className="flex items-center space-x-3 mb-4">
                <div className="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-lg">
                  <Building2 className="h-6 w-6 text-blue-600 dark:text-blue-400" />
                </div>
                <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                  {dept.name}
                </h3>
              </div>
              <div className="space-y-2 text-sm text-neutral-600 dark:text-neutral-400">
                <p><strong>Head:</strong> {dept.head}</p>
                <p><strong>Staff:</strong> {dept.staff} members</p>
                <p><strong>Patients:</strong> {dept.patients} active</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </DashboardLayout>
  );
}

export default withAuth(DepartmentsPage, ["ADMIN"]);
