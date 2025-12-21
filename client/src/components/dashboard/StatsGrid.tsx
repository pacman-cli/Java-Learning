import { StatCard } from "@/components/dashboard/StatCard";
import { DashboardSummaryDto } from "@/types";
import { Activity, DollarSign, ShoppingCart, Users } from "lucide-react";

interface StatsProps {
  data: DashboardSummaryDto | undefined;
  isLoading: boolean;
}

export function StatsGrid({ data, isLoading }: StatsProps) {
  return (
    <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
      <StatCard
        title="Total Revenue"
        value={`$${data?.totalRevenue?.toLocaleString() ?? "0"}`}
        trend="up"
        trendValue="20.1%"
        icon={DollarSign}
        loading={isLoading}
        delay={0}
      />
      <StatCard
        title="Total Orders"
        value={data?.totalOrders?.toString() ?? "0"}
        trend="up"
        trendValue="180.1%"
        icon={ShoppingCart}
        loading={isLoading}
        delay={0.1}
      />
      <StatCard
        title="Avg. Order Value"
        value={`$${data?.avgOrderValue?.toFixed(2) ?? "0.00"}`}
        trend="up"
        trendValue="19%"
        icon={Activity}
        loading={isLoading}
        delay={0.2}
      />
      <StatCard
        title="Active Customers"
        value={data?.completedOrders?.toString() ?? "0"}
        subValue={`${data?.canceledOrders ?? 0} inactive`}
        trend="neutral"
        trendValue="+2 this hour"
        icon={Users}
        loading={isLoading}
        delay={0.3}
      />
    </div>
  );
}
