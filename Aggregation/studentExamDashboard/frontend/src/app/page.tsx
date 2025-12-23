"use client";

import { useEffect, useState } from "react";
import DashboardLayout from "@/components/DashboardLayout";
import { StatCard } from "@/components/StatCard";
import { SubjectBarChart } from "@/components/SubjectBarChart";
import { MonthlyTrendChart } from "@/components/MonthlyTrendChart";
import { fetchDashboardOverview } from "@/services/api";
import { DashboardData } from "@/types";
import { BookOpen, Calculator, GraduationCap, Users } from "lucide-react";

export default function Home() {
  const [data, setData] = useState<DashboardData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadData = async () => {
      try {
        const dashboardData = await fetchDashboardOverview();
        console.log("Fetched Dashboard Data:", dashboardData);
        setData(dashboardData);
      } catch (err) {
        console.error("Failed to load dashboard data:", err);
        setError("Failed to load dashboard data. Please ensure the backend is running.");
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  if (loading) {
    return (
      <DashboardLayout>
        <div className="flex h-[50vh] items-center justify-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
        </div>
      </DashboardLayout>
    );
  }

  if (error || !data) {
    return (
      <DashboardLayout>
         <div className="flex h-[50vh] flex-col items-center justify-center gap-4 text-center">
          <p className="text-destructive font-medium">{error || "No data available."}</p>
          <button 
            onClick={() => window.location.reload()}
            className="px-4 py-2 bg-primary text-primary-foreground rounded-md hover:opacity-90 transition-opacity"
          >
            Retry
          </button>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Stat Cards */}
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
          <StatCard
            label="Total Exams"
            value={data.summary.totalExams}
            icon={BookOpen}
            description="Total exams recorded"
          />
          <StatCard
            label="Overall Pass Rate"
            value={`${data.summary.passPercentage.toFixed(1)}%`}
            icon={GraduationCap}
            trend={data.summary.passPercentage >= 70 ? "Passing" : "Needs Improvement"}
            trendUp={data.summary.passPercentage >= 70}
            description="Students passing exams"
          />
           <StatCard
            label="Average Marks"
            value={data.summary.averageMarks.toFixed(1)}
            icon={Calculator}
            description="Across all subjects"
          />
           <StatCard 
            label="Total Students"
            value={data.summary.totalStudents} 
            icon={Users}
            description="Active students"
          />
        </div>

        {/* Charts */}
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-7">
          <div className="lg:col-span-4">
             <SubjectBarChart data={data.categoryStats} />
          </div>
          <div className="lg:col-span-3">
             <MonthlyTrendChart data={data.monthlyStats} />
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
