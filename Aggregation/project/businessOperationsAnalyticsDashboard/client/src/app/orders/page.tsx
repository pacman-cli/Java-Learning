"use client"

import { DashboardLayout } from "@/components/layout/DashboardLayout"
import { PageHeader } from "@/components/layout/PageHeader"
import { OrdersDataTable } from "@/components/orders/OrdersDataTable"
import { getRecentOrders } from "@/services/api"
import { useQuery } from "@tanstack/react-query"
import { Loader2 } from "lucide-react"

export default function OrdersPage() {
  const { data: orders, isLoading, isError } = useQuery({
    queryKey: ["recentOrders"],
    queryFn: getRecentOrders,
  })

  return (
    <DashboardLayout>
    <PageHeader
        title= "Orders"
  description = "Manage and track your recent customer orders."
    >
    <button className="inline-flex items-center justify-center rounded-lg bg-primary px-4 py-2 text-sm font-medium text-primary-foreground shadow transition-colors hover:bg-primary/90 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50" >
      Export List
        </button>
        </PageHeader>

  {
    isLoading ? (
      <div className= "h-[400px] w-full flex flex-col items-center justify-center gap-4 rounded-xl border border-border/50 bg-card/50" >
      <Loader2 className="h-8 w-8 animate-spin text-primary" />
        <p className="text-sm text-muted-foreground" > Loading orders...</p>
          </div>
      ) : isError ? (
      <div className= "h-[200px] w-full flex items-center justify-center rounded-xl border border-rose-500/20 bg-rose-500/5 text-rose-500" >
      Failed to load orders.Please try again later.
        </div>
      ) : (
      <OrdersDataTable data= { orders || []
  } />
      )
}
</DashboardLayout>
  );
}
