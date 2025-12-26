import { ChartWrapper } from "@/components/dashboard/ChartWrapper";
import { Button } from "@/components/ui/button";
import { MonthlyRevenueDto } from "@/types";
import { ArrowUpRight } from "lucide-react";
import {
    Bar,
    BarChart,
    CartesianGrid,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis,
} from "recharts";

interface RevenueChartProps {
  data: MonthlyRevenueDto[];
}

export function RevenueChart({ data }: RevenueChartProps) {
  return (
    <ChartWrapper
        title="Revenue Overview"
        description="Monthly revenue performance for the current year."
        className="col-span-4 lg:col-span-4"
        action={
            <Button variant="outline" size="sm" className="h-8 gap-1 hidden sm:flex">
                <span className="sr-only">View Details</span>
                Details <ArrowUpRight className="h-3.5 w-3.5 text-muted-foreground" />
            </Button>
        }
    >
        <div className="h-[350px] w-full">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={data} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
              <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="hsl(var(--muted-foreground))" opacity={0.1} />
              <XAxis
                dataKey="month"
                stroke="hsl(var(--muted-foreground))"
                fontSize={12}
                tickLine={false}
                axisLine={false}
                dy={10}
              />
              <YAxis
                stroke="hsl(var(--muted-foreground))"
                fontSize={12}
                tickLine={false}
                axisLine={false}
                tickFormatter={(value) => `$${value}`}
                dx={-10}
              />
              <Tooltip
                 cursor={{ fill: 'hsl(var(--primary) / 0.05)' }}
                 contentStyle={{
                     backgroundColor: "hsl(var(--popover))",
                     borderColor: "hsl(var(--border) / 0.5)",
                     color: "hsl(var(--popover-foreground))",
                     borderRadius: "12px",
                     boxShadow: "0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)"
                 }}
                 itemStyle={{ color: "hsl(var(--foreground))", fontSize: "14px", fontWeight: 500 }}
                 formatter={(value: any) => [`$${Number(value || 0).toLocaleString()}`, "Revenue"]}
              />
              <Bar
                dataKey="revenue"
                fill="hsl(var(--primary))"
                radius={[6, 6, 0, 0]}
                barSize={32}
                fillOpacity={0.9}
                activeBar={{ fillOpacity: 1 }}
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
    </ChartWrapper>
  );
}
