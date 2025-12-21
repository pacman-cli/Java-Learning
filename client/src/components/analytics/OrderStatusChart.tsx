"use client";

import { ChartWrapper } from "@/components/dashboard/ChartWrapper";
import { DashboardSummaryDto } from "@/types";
import {
    Bar,
    BarChart,
    CartesianGrid,
    Cell,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis,
} from "recharts";

interface OrderStatusChartProps {
  data: DashboardSummaryDto; // Uses summary DTO which has the counts
}

export function OrderStatusChart({ data }: OrderStatusChartProps) {
    const completed = Number(data.completedOrders);
    const canceled = Number(data.canceledOrders);
    // Derived pending based on assumption: Total = Completed + Canceled + Pending
    const pending = Math.max(0, Number(data.totalOrders) - completed - canceled);

    const chartData = [
        { name: "Completed", value: completed, color: "hsl(142.1 76.2% 36.3%)" }, // Emerald 600
        { name: "Pending", value: pending, color: "hsl(47.9 95.8% 53.1%)" },   // Amber 500
        { name: "Canceled", value: Number(data.canceledOrders), color: "hsl(346.8 77.2% 49.8%)" }, // Rose 500
    ];

  return (
    <ChartWrapper
        title="Order Status"
        description="Current distribution of order statuses."
        className="col-span-4 lg:col-span-2 min-h-[400px]"
    >
        <div className="h-[300px] w-full mt-4">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chartData} layout="vertical" margin={{ left: 0, right: 30 }}>
              <CartesianGrid strokeDasharray="3 3" horizontal={false} stroke="hsl(var(--muted-foreground))" opacity={0.1} />
              <XAxis type="number" hide />
              <YAxis
                type="category"
                dataKey="name"
                stroke="hsl(var(--muted-foreground))"
                fontSize={12}
                tickLine={false}
                axisLine={false}
                width={70}
              />
              <Tooltip
                 cursor={{ fill: 'hsl(var(--primary) / 0.05)' }}
                 contentStyle={{
                     backgroundColor: "hsl(var(--popover))",
                     borderColor: "hsl(var(--border) / 0.5)",
                     color: "hsl(var(--popover-foreground))",
                     borderRadius: "12px",
                     boxShadow: "0 4px 6px -1px rgb(0 0 0 / 0.1)"
                 }}
                 formatter={(value) => [value, "Orders"]}
              />
              <Bar dataKey="value" radius={[0, 4, 4, 0]} barSize={32}>
                {chartData.map((entry, index) => (
                    <Cell key={index} fill={entry.color} />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>
    </ChartWrapper>
  );
}
