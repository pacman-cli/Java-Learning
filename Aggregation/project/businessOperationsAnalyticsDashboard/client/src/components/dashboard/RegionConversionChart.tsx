"use client"

import { ChartWrapper } from "@/components/dashboard/ChartWrapper"
import { RegionConversionDto } from "@/types"
import {
    Bar,
    BarChart,
    CartesianGrid,
    Cell,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts"

interface RegionConversionChartProps {
    data: RegionConversionDto[]
}

export function RegionConversionChart({ data }: RegionConversionChartProps) {
    // Sort by conversion rate for better visualization
    const sortedData = [...data].sort((a, b) => b.conversionRate - a.conversionRate)

    return (
        <ChartWrapper
            title="Regional Conversion"
            description="Order completion rate by region."
            className="col-span-4 lg:col-span-3 min-h-[400px]"
        >
            <div className="h-[300px] w-full mt-4">
                <ResponsiveContainer width="100%" height="100%">
                    <BarChart
                        data={sortedData}
                        layout="vertical"
                        margin={{ top: 5, right: 30, left: 40, bottom: 5 }}
                    >
                        <CartesianGrid strokeDasharray="3 3" horizontal={true} vertical={false} stroke="hsl(var(--border))" />
                        <XAxis
                            type="number"
                            hide
                        />
                        <YAxis
                            dataKey="region"
                            type="category"
                            stroke="#888888"
                            fontSize={12}
                            tickLine={false}
                            axisLine={false}
                            width={80}
                        />
                        <Tooltip
                            cursor={{ fill: "hsl(var(--muted)/0.4)" }}
                            content={({ active, payload }) => {
                                if (active && payload && payload.length) {
                                    const data = payload[0].payload as RegionConversionDto
                                    return (
                                        <div className="rounded-xl border border-border/50 bg-popover/95 px-3 py-2 shadow-xl backdrop-blur-sm">
                                            <div className="flex items-center gap-2 mb-1">
                                                <span className="font-semibold">{data.region}</span>
                                            </div>
                                            <div className="flex flex-col gap-1">
                                                <div className="flex justify-between gap-4">
                                                    <span className="text-xs text-muted-foreground">Conversion Rate:</span>
                                                    <span className="font-bold">{(data.conversionRate * 100).toFixed(1)}%</span>
                                                </div>
                                                <div className="flex justify-between gap-4">
                                                    <span className="text-xs text-muted-foreground">Completed:</span>
                                                    <span>{data.completedOrders}</span>
                                                </div>
                                                <div className="flex justify-between gap-4">
                                                    <span className="text-xs text-muted-foreground">Canceled:</span>
                                                    <span>{data.canceledOrders}</span>
                                                </div>
                                            </div>
                                        </div>
                                    )
                                }
                                return null
                            }}
                        />
                        <Bar
                            dataKey="conversionRate"
                            name="Conversion Rate"
                            radius={[0, 4, 4, 0]}
                            barSize={20}
                        >
                            {sortedData.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={
                                    index === 0 ? "hsl(var(--primary))" :
                                        index === 1 ? "hsl(var(--primary)/0.8)" :
                                            index === 2 ? "hsl(var(--primary)/0.6)" :
                                                "hsl(var(--primary)/0.4)"
                                } />
                            ))}
                        </Bar>
                    </BarChart>
                </ResponsiveContainer>
            </div>
        </ChartWrapper>
    )
}
