"use client";

import { Line, LineChart, CartesianGrid, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer } from "recharts";
import { MonthlyStat } from "@/types";

interface MonthlyTrendChartProps {
  data: MonthlyStat[];
}

export function MonthlyTrendChart({ data }: MonthlyTrendChartProps) {
  return (
    <div className="rounded-xl border bg-card text-card-foreground shadow h-full">
      <div className="flex flex-col space-y-1.5 p-6">
        <h3 className="font-semibold leading-none tracking-tight">Monthly Trends</h3>
        <p className="text-sm text-muted-foreground">Pass rate over time</p>
      </div>
      <div className="p-6 pt-0 pl-0">
        <ResponsiveContainer width="100%" height={350}>
          <LineChart data={data}>
            <CartesianGrid strokeDasharray="3 3" vertical={false} />
            <XAxis 
                dataKey="month" 
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
                tickFormatter={(value) => `${value}%`}
            />
            <Tooltip 
                 contentStyle={{ borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)' }}
            />
            <Legend />
            <Line 
                type="monotone" 
                dataKey="passRate" 
                name="Pass Rate" 
                stroke="var(--primary)" 
                strokeWidth={2} 
                activeDot={{ r: 6 }} 
            />
            <Line 
                type="monotone" 
                dataKey="averageMarks" 
                name="Avg Marks" 
                stroke="var(--destructive)" 
                strokeWidth={2} 
                activeDot={{ r: 6 }} 
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
