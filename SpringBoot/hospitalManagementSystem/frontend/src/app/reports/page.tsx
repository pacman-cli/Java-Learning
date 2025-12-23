"use client";

import React from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import { withAuth } from "@/providers/AuthProvider";
import { BarChart3, Download } from "lucide-react";
import toast from "react-hot-toast";

function ReportsPage() {
  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
            Reports & Analytics
          </h1>
          <p className="text-neutral-600 dark:text-neutral-400 mt-1">
            View system reports and analytics
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[
            { title: "Patient Report", icon: "👥", desc: "Generate patient statistics" },
            { title: "Appointment Report", icon: "📅", desc: "View appointment analytics" },
            { title: "Revenue Report", icon: "💰", desc: "Financial overview" },
            { title: "Doctor Performance", icon: "⭐", desc: "Doctor statistics" },
            { title: "Department Report", icon: "🏥", desc: "Department-wise data" },
            { title: "Custom Report", icon: "📊", desc: "Create custom reports" },
          ].map((report, index) => (
            <div
              key={index}
              className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6 hover:shadow-lg transition-shadow cursor-pointer"
              onClick={() => toast.info(`${report.title} coming soon`)}
            >
              <div className="text-4xl mb-3">{report.icon}</div>
              <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                {report.title}
              </h3>
              <p className="text-sm text-neutral-600 dark:text-neutral-400 mb-4">
                {report.desc}
              </p>
              <button className="flex items-center space-x-2 text-blue-600 dark:text-blue-400 text-sm font-medium">
                <Download className="h-4 w-4" />
                <span>Generate</span>
              </button>
            </div>
          ))}
        </div>
      </div>
    </DashboardLayout>
  );
}

export default withAuth(ReportsPage, ["ADMIN"]);
