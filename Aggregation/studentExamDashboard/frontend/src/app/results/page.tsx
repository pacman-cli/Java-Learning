"use client";

import { useEffect, useState } from "react";
import DashboardLayout from "@/components/DashboardLayout";
import { ResultsTable } from "@/components/ResultsTable";
import { fetchDetailedExamResults } from "@/services/api";
import { ExamResultDto } from "@/types";

export default function ResultsPage() {
  const [data, setData] = useState<ExamResultDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadData = async () => {
      try {
        const results = await fetchDetailedExamResults();
        setData(results);
      } catch (err) {
        console.error("Failed to load exam results:", err);
        setError("Failed to load exam results.");
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  return (
    <DashboardLayout>
      <div className="space-y-6">
         <div>
          <h2 className="text-2xl font-bold tracking-tight">Detailed Exam Results</h2>
          <p className="text-muted-foreground">View and manage student exam performance.</p>
        </div>

        {loading ? (
           <div className="flex h-32 items-center justify-center">
             <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
           </div>
        ) : error ? (
           <div className="p-4 rounded-md bg-destructive/10 text-destructive">{error}</div>
        ) : (
           <ResultsTable data={data} />
        )}
      </div>
    </DashboardLayout>
  );
}
