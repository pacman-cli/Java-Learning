"use client";

import { ChartWrapper } from "@/components/dashboard/ChartWrapper";
import { cn } from "@/lib/utils";
import { CategorySalesDto } from "@/types";
import { motion } from "framer-motion";
import { useMemo, useState } from "react";
import {
    Cell,
    Pie,
    PieChart,
    ResponsiveContainer,
    Tooltip,
} from "recharts";

interface CategorySalesChartProps {
  data: CategorySalesDto[];
}

// Premium color palette (Apple-style / Modern)
const COLORS = [
  "hsl(var(--primary))",            // Primary Brand
  "hsl(262.1 83.3% 57.8%)",         // Violet
  "hsl(191.6 91.4% 36.5%)",         // Cyan
  "hsl(346.8 77.2% 49.8%)",         // Rose
  "hsl(24.6 95% 53.1%)",            // Orange
];

export function CategorySalesChart({ data }: CategorySalesChartProps) {
  const [activeIndex, setActiveIndex] = useState<number | null>(null);

  // Calculate generic stats
  const totalRevenue = useMemo(() =>
    data.reduce((acc, curr) => acc + (Number(curr.totalRevenue) || 0), 0),
  [data]);

  const processedData = useMemo(() =>
    data.map((item, index) => ({
      ...item,
      value: Number(item.totalRevenue),
      percentage: totalRevenue > 0 ? (Number(item.totalRevenue) / totalRevenue * 100).toFixed(1) : "0",
      fill: COLORS[index % COLORS.length]
    })),
  [data, totalRevenue]);

  return (
    <ChartWrapper
        title="Category Distribution"
        description="Revenue breakdown by product category."
        className="col-span-4 lg:col-span-2 flex flex-col h-full min-h-[400px]"
    >
        <div className="flex flex-col sm:flex-row items-center justify-between gap-8 h-full flex-1">

          {/* Left: Chart Area */}
          <div className="relative h-[220px] w-full sm:w-1/2 flex-shrink-0">
             <ResponsiveContainer width="100%" height="100%">
               <PieChart>
                 <Pie
                   data={processedData}
                   cx="50%"
                   cy="50%"
                   innerRadius={65}
                   outerRadius={85}
                   paddingAngle={4}
                   dataKey="value"
                   nameKey="categoryName"
                   stroke="none"
                   onMouseEnter={(_, index) => setActiveIndex(index)}
                   onMouseLeave={() => setActiveIndex(null)}
                 >
                   {processedData.map((entry, index) => (
                     <Cell
                       key={`cell-${index}`}
                       fill={entry.fill}
                       className={cn(
                         "transition-all duration-300 stroke-background stroke-2",
                         activeIndex === index ? "opacity-100 scale-[1.02]" : (activeIndex !== null ? "opacity-30" : "opacity-100")
                       )}
                     />
                   ))}
                 </Pie>
                 <Tooltip
                    cursor={false}
                    content={({ active, payload }) => {
                        if (active && payload && payload.length) {
                        const data = payload[0].payload;
                        return (
                            <div className="rounded-xl border border-border/50 bg-popover/95 px-3 py-2 shadow-xl backdrop-blur-sm">
                                <div className="flex items-center gap-2 mb-1">
                                    <div className="h-2 w-2 rounded-full" style={{ backgroundColor: data.fill }} />
                                    <span className="text-xs font-semibold">{data.categoryName}</span>
                                </div>
                                <div className="flex flex-col">
                                    <span className="text-lg font-bold tabular-nums">
                                        ${data.value.toLocaleString()}
                                    </span>
                                    <span className="text-xs text-muted-foreground tabular-nums">
                                        {data.percentage}% of total
                                    </span>
                                </div>
                            </div>
                        );
                        }
                        return null;
                    }}
                 />
               </PieChart>
             </ResponsiveContainer>

             {/* Centered Label */}
             <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
                <span className="text-xs font-medium text-muted-foreground text-center">Total Sales</span>
                <span className="text-lg font-bold tracking-tight text-foreground">
                    ${(totalRevenue / 1000).toFixed(1)}k
                </span>
             </div>
          </div>

          {/* Right: Legend & Stats */}
          <div className="flex-1 w-full sm:w-1/2 flex flex-col justify-center space-y-3 pb-2 sm:pb-0 font-sans">
                {processedData.map((item, index) => (
                    <div
                        key={item.categoryName}
                        onMouseEnter={() => setActiveIndex(index)}
                        onMouseLeave={() => setActiveIndex(null)}
                        className={cn(
                            "group flex items-center justify-between gap-2 rounded-lg px-2 py-1.5 transition-all duration-200 cursor-default",
                            activeIndex === index ? "bg-muted/60" : "hover:bg-muted/40"
                        )}
                    >
                        <div className="flex items-center gap-2.5 overflow-hidden">
                            <motion.div
                                className="h-2.5 w-2.5 rounded-full flex-shrink-0 shadow-sm"
                                style={{ backgroundColor: item.fill }}
                                layoutId={`indicator-${index}`}
                            />
                            <span className={cn(
                                "text-sm font-medium truncate transition-colors",
                                activeIndex === index ? "text-foreground" : "text-muted-foreground group-hover:text-foreground"
                            )}>
                                {item.categoryName}
                            </span>
                        </div>
                        <div className="flex items-center gap-3 text-right">
                             <span className="text-xs font-medium text-muted-foreground tabular-nums w-9 text-right">
                                {item.percentage}%
                             </span>
                             <span className="text-sm font-bold text-foreground tabular-nums">
                                ${item.value.toLocaleString()}
                             </span>
                        </div>
                    </div>
                ))}
          </div>

        </div>
    </ChartWrapper>
  );
}
