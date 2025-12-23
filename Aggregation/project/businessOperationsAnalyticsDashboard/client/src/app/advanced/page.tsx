"use client"

import { RegionConversionChart } from "@/components/dashboard/RegionConversionChart"
import { RollingRevenueChart } from "@/components/dashboard/RollingRevenueChart"
import { DashboardLayout } from "@/components/layout/DashboardLayout"
import { Button } from "@/components/ui/button"
import { getAdvancedDashboard } from "@/services/api"
import { useQuery } from "@tanstack/react-query"
import { BarChart3 } from "lucide-react"

export default function AdvancedDashboardPage() {
    const advancedQuery = useQuery({
        queryKey: ["advancedDashboard"],
        queryFn: getAdvancedDashboard,
    })

    return (
        <DashboardLayout>
            <div className="flex flex-col gap-6 md:gap-8">
                <div className="flex items-center justify-between">
                    <div className="space-y-1">
                        <h2 className="text-2xl font-bold tracking-tight">Advanced Analytics</h2>
                        <p className="text-sm text-muted-foreground">
                            Deep dive into revenue trends and regional performance.
                        </p>
                    </div>
                    <div className="flex items-center space-x-2">
                        <Button variant="outline" size="sm">
                            <BarChart3 className="mr-2 h-4 w-4" />
                            Export Data
                        </Button>
                    </div>
                </div>

                <div className="grid gap-6 md:grid-cols-1 lg:grid-cols-2">
                    <div className="col-span-1">
                        {advancedQuery.data && <RollingRevenueChart data={advancedQuery.data.rollingRevenue} />}
                    </div>
                    <div className="col-span-1">
                        {advancedQuery.data && <RegionConversionChart data={advancedQuery.data.conversionRateByRegion} />}
                    </div>
                </div>
            </div>
        </DashboardLayout>
    )
}
