import clsx from 'clsx';
import { LucideIcon } from 'lucide-react';

interface StatCardProps {
  label: string;
  value: string | number;
  icon: LucideIcon;
  trend?: string;
  trendUp?: boolean;
  className?: string;
  description?: string;
}

export function StatCard({ label, value, icon: Icon, trend, trendUp, className, description }: StatCardProps) {
  return (
    <div className={clsx("rounded-xl border bg-card text-card-foreground shadow p-6", className)}>
      <div className="flex flex-row items-center justify-between space-y-0 pb-2">
        <h3 className="tracking-tight text-sm font-medium text-muted-foreground">{label}</h3>
        <Icon className="h-4 w-4 text-muted-foreground" />
      </div>
      <div>
        <div className="text-2xl font-bold">{value}</div>
        {(trend || description) && (
          <p className="text-xs text-muted-foreground mt-1">
            {trend && (
              <span className={clsx("font-medium mr-1", trendUp ? "text-green-500" : "text-red-500")}>
                {trend}
              </span>
            )}
            {description}
          </p>
        )}
      </div>
    </div>
  );
}
