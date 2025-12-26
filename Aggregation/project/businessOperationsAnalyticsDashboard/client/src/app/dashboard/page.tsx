"use client"

import { CategorySalesChart } from "@/components/dashboard/CategorySalesChart"
import { RecentOrdersTable } from "@/components/dashboard/RecentOrdersTable"
import { RegionConversionChart } from "@/components/dashboard/RegionConversionChart"
import { RevenueChart } from "@/components/dashboard/RevenueChart"
import { RollingRevenueChart } from "@/components/dashboard/RollingRevenueChart"
import { StatsGrid } from "@/components/dashboard/StatsGrid"
import { TopCustomersTable } from "@/components/dashboard/TopCustomersTable"
import { DashboardLayout } from "@/components/layout/DashboardLayout"
import { Button } from "@/components/ui/button"
import {
  getAdvancedDashboard,
  getCategorySales,
  getDashboardSummary,
  getMonthlyRevenue,
  getRecentOrders,
  getTopCustomers,
} from "@/services/api"
import { useQuery } from "@tanstack/react-query"

export default function DashboardPage() {
  const summaryQuery = useQuery({
    queryKey: ["dashboardSummary"],
    queryFn: getDashboardSummary,
  })

  const revenueQuery = useQuery({
    queryKey: ["monthlyRevenue"],
    queryFn: getMonthlyRevenue,
  })

  const categoryQuery = useQuery({
    queryKey: ["categorySales"],
    queryFn: getCategorySales,
  })

  const ordersQuery = useQuery({
    queryKey: ["recentOrders"],
    queryFn: getRecentOrders,
  })

  const topCustomersQuery = useQuery({
    queryKey: ["topCustomers"],
    queryFn: getTopCustomers,
  })

  const advancedQuery = useQuery({
    queryKey: ["advancedDashboard"],
    queryFn: getAdvancedDashboard,
  })

  return (
    <DashboardLayout>
      <div className="flex flex-col gap-6 md:gap-8">
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <h2 className="text-2xl font-bold tracking-tight">Overview</h2>
            <p className="text-sm text-muted-foreground">
              Here&apos;s what&apos;s happening with your store today.
            </p>
          </div>
          <div className="flex items-center space-x-2">
            <Button variant="outline" size="sm" className="hidden sm:flex">
              Last 30 Days
            </Button>
            <Button size="sm">Download Report</Button>
          </div>
        </div>

        <StatsGrid data={summaryQuery.data} isLoading={summaryQuery.isLoading} />

        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-7">
          <div className="col-span-4">
            {revenueQuery.data && <RevenueChart data={revenueQuery.data} />}
          </div>
          <div className="col-span-3">
            {advancedQuery.data && <RollingRevenueChart data={advancedQuery.data.rollingRevenue} />}
          </div>
        </div>

        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-7">
          <div className="col-span-3">
            {categoryQuery.data && <CategorySalesChart data={categoryQuery.data} />}
          </div>
          <div className="col-span-4">
            {advancedQuery.data && <RegionConversionChart data={advancedQuery.data.conversionRateByRegion} />}
          </div>
        </div>

        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-7">
          <div className="col-span-4">
            {ordersQuery.data && <RecentOrdersTable orders={ordersQuery.data} />}
          </div>
          <div className="col-span-3">
            {topCustomersQuery.data && <TopCustomersTable customers={topCustomersQuery.data} />}
          </div>
        </div>
      </div>
    </DashboardLayout>
  )
}
