"use client";

import { Bar, BarChart, CartesianGrid, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer } from "recharts";
import { CategoryWiseStats } from "@/types";

// import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"; 

interface SubjectBarChartProps {
  data: CategoryWiseStats[];
}

export function SubjectBarChart({ data }: SubjectBarChartProps) {
  return (
    <div className="rounded-xl border bg-card text-card-foreground shadow h-full">
      <div className="flex flex-col space-y-1.5 p-6">
        <h3 className="font-semibold leading-none tracking-tight">Subject Performance</h3>
        <p className="text-sm text-muted-foreground">Pass rate and average marks by subject</p>
      </div>
      <div className="p-6 pt-0 pl-0">
        <ResponsiveContainer width="100%" height={350}>
          <BarChart data={data}>
            <CartesianGrid strokeDasharray="3 3" vertical={false} />
            <XAxis 
                dataKey="subjectNames" 
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
                tickFormatter={(value) => `${value}`}
            />
            <Tooltip 
                cursor={{ fill: 'transparent' }}
                contentStyle={{ borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)' }}
            />
            <Legend />
            <Bar dataKey="averageMarks" name="Average Marks" fill="var(--primary)" radius={[4, 4, 0, 0]} />
            <Bar dataKey="passRate" name="Pass Rate (%)" fill="var(--muted-foreground)" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
