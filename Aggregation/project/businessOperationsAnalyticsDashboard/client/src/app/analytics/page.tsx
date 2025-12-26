"use client"

import { OrderStatusChart } from "@/components/analytics/OrderStatusChart"
import { CategorySalesChart } from "@/components/dashboard/CategorySalesChart"
import { RevenueChart } from "@/components/dashboard/RevenueChart"
import { StatsGrid } from "@/components/dashboard/StatsGrid"
import { DashboardLayout } from "@/components/layout/DashboardLayout"
import { PageHeader } from "@/components/layout/PageHeader"
import { getCategorySales, getDashboardSummary, getMonthlyRevenue } from "@/services/api"
import { useQuery } from "@tanstack/react-query"
import { Loader2 } from "lucide-react"

export default function AnalyticsPage() {
  // Fetch all necessary data
  const { data: revenueData } = useQuery({ queryKey: ["monthlyRevenue"], queryFn: getMonthlyRevenue })
  const { data: categoryData } = useQuery({ queryKey: ["categorySales"], queryFn: getCategorySales })
  const { data: summaryData, isLoading: isSummaryLoading } = useQuery({ queryKey: ["summary"], queryFn: getDashboardSummary })

  return (
    <DashboardLayout>
      <PageHeader
        title="Analytics"
        description="Deep dive into your business performance metrics."
      >
        <div className="flex items-center gap-2 text-sm text-muted-foreground bg-muted/50 px-3 py-1.5 rounded-md border border-border/50" >
          <span>Last 30 Days </span>

        </div>
      </PageHeader>

      < StatsGrid data={summaryData} isLoading={isSummaryLoading} />

      {/* Grid Layout for Charts */}
      < div className="grid gap-6 md:grid-cols-2 lg:grid-cols-6" >
        {/* Top Row: Revenue (4 cols) + Status (2 cols) */}
        < div className="col-span-full lg:col-span-4" >
          {revenueData ? <RevenueChart data={revenueData} /> : <SkeletonCard />}
        </div>
        < div className="col-span-full lg:col-span-2" >
          {summaryData ? <OrderStatusChart data={summaryData} /> : <SkeletonCard />}
        </div>

        {/* Bottom Row: Category (2 cols) + Expanded view placeholder or other data */}
        <div className="col-span-full lg:col-span-2" >
          {categoryData ? <CategorySalesChart data={categoryData} /> : <SkeletonCard />}
        </div>

        {/* Placeholder for future expansion or another metric */}
        <div className="col-span-full lg:col-span-4 flex items-center justify-center rounded-2xl border border-dashed border-border/50 bg-card/20 min-h-[400px]" >
          <p className="text-muted-foreground text-sm" > Additional advanced metrics coming soon...</p>
        </div>
      </div>
    </DashboardLayout>
  )
}

function SkeletonCard() {
  return (
    <div className="h-[400px] w-full rounded-2xl border border-border/50 bg-card/50 backdrop-blur-sm animate-pulse flex items-center justify-center" >
      <Loader2 className="h-8 w-8 animate-spin text-muted" />
    </div>
  )
}
