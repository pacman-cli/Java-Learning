"use client";

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { cn } from "@/lib/utils";

interface ChartWrapperProps {
  title: string;
  description?: string;
  children: React.ReactNode;
  className?: string;
  action?: React.ReactNode;
}

export function ChartWrapper({
  title,
  description,
  children,
  className,
  action,
}: ChartWrapperProps) {
  return (
    <Card className={cn("flex flex-col overflow-hidden glass-panel", className)}>
      <CardHeader className="flex flex-row items-start justify-between space-y-0 pb-4 border-b border-border/20">
        <div className="space-y-1">
          <CardTitle className="text-base font-semibold tracking-tight">{title}</CardTitle>
          {description && (
            <CardDescription className="text-xs">{description}</CardDescription>
          )}
        </div>
        {action && <div className="flex items-center">{action}</div>}
      </CardHeader>
      <CardContent className="flex-1 p-6">
        {children}
      </CardContent>
    </Card>
  );
}
