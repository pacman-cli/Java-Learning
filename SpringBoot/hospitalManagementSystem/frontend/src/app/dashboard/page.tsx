"use client";

import React from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import AdminDashboard from "@/components/dashboards/AdminDashboard";
import DoctorDashboard from "@/components/dashboards/DoctorDashboard";
import PatientDashboard from "@/components/dashboards/PatientDashboard";
import DashboardLayout from "@/components/layout/DashboardLayout";

function DashboardPage() {
  const { user, hasRole } = useAuth();

  const getDashboard = () => {
    if (!user) return null;

    // Check roles in priority order
    if (hasRole("ADMIN") || hasRole("SUPER_ADMIN")) {
      return <AdminDashboard />;
    }

    if (hasRole("DOCTOR")) {
      return <DoctorDashboard />;
    }

    if (hasRole("PATIENT")) {
      return <PatientDashboard />;
    }

    // Default fallback - show patient dashboard for any other role
    return <PatientDashboard />;
  };

  return <DashboardLayout>{getDashboard()}</DashboardLayout>;
}

// Protect the dashboard route - require authentication
export default withAuth(DashboardPage);
