"use client"

import { ChartWrapper } from "@/components/dashboard/ChartWrapper"
import { RollingRevenueDto } from "@/types"
import {
    CartesianGrid,
    Legend,
    Line,
    LineChart,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis,
} from "recharts"

interface RollingRevenueChartProps {
    data: RollingRevenueDto[]
}

export function RollingRevenueChart({ data }: RollingRevenueChartProps) {
    // Format date for display
    const formattedData = data.map((item) => ({
        ...item,
        formattedDate: new Date(item.date).toLocaleDateString("en-US", {
            month: "short",
            day: "numeric",
        }),
    }))

    return (
        <ChartWrapper
            title="Revenue Trends"
            description="Daily revenue vs 7-day moving average."
            className="col-span-4 lg:col-span-3 min-h-[400px]"
        >
            <div className="h-[300px] w-full mt-4">
                <ResponsiveContainer width="100%" height="100%">
                    <LineChart data={formattedData} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                        <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="hsl(var(--border))" />
                        <XAxis
                            dataKey="formattedDate"
                            stroke="#888888"
                            fontSize={12}
                            tickLine={false}
                            axisLine={false}
                        />
                        <YAxis
                            stroke="#888888"
                            fontSize={12}
                            tickLine={false}
                            axisLine={false}
                            tickFormatter={(value) => `$${value}`}
                        />
                        <Tooltip
                            contentStyle={{
                                backgroundColor: "hsl(var(--popover))",
                                borderColor: "hsl(var(--border))",
                                color: "hsl(var(--popover-foreground))",
                                borderRadius: "var(--radius)",
                            }}
                            labelStyle={{ color: "hsl(var(--muted-foreground))" }}
                        />
                        <Legend wrapperStyle={{ paddingTop: "20px" }} />
                        <Line
                            type="monotone"
                            dataKey="dailyRevenue"
                            name="Daily Revenue"
                            stroke="hsl(var(--primary))"
                            strokeWidth={2}
                            dot={false}
                            activeDot={{ r: 6 }}
                        />
                        <Line
                            type="monotone"
                            dataKey="rolling7DayAvg"
                            name="7-Day Avg"
                            stroke="hsl(24.6 95% 53.1%)"
                            strokeWidth={2}
                            strokeDasharray="5 5"
                            dot={false}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </ChartWrapper>
    )
}
