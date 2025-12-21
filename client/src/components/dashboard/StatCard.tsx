"use client";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import { animate, motion } from "framer-motion";
import { ArrowDown, ArrowUp } from "lucide-react";
import { useEffect, useRef } from "react";

interface StatCardProps {
  title: string;
  value: string;
  subValue?: string;
  trend?: "up" | "down" | "neutral";
  trendValue?: string;
  icon: any;
  className?: string;
  loading?: boolean;
  delay?: number;
}

function Counter({ value, className }: { value: string, className?: string }) {
    const nodeRef = useRef<HTMLSpanElement>(null);
    const numericValue = parseFloat(value.replace(/[^0-9.-]+/g,"")) || 0;
    // Simple heuristic to detect prefix like "$"
    const prefix = value.match(/^[^0-9]*/)?.[0] || "";
    // Simple heuristic to detect suffix like "%" or "k"
    const suffix = value.match(/[^0-9]*$/)?.[0] || "";

    useEffect(() => {
        const node = nodeRef.current;
        if (!node) return;

        const controls = animate(0, numericValue, {
            duration: 1.5,
            delay: 0.2, // waits for card to enter
            ease: "easeOut",
            onUpdate(v) {
                // Determine decimals based on original input
                const decimals = value.includes(".") ? value.split(".")[1].length : 0;
                node.textContent = prefix + v.toFixed(decimals) + suffix;
            }
        });
        return () => controls.stop();
    }, [numericValue, prefix, suffix, value]);

    return <span ref={nodeRef} className={className}>{value}</span>; // Initial render
}

export function StatCard({
  title,
  value,
  subValue,
  trend,
  trendValue,
  icon: Icon,
  className,
  loading,
  delay = 0,
}: StatCardProps) {
  if (loading) {
    return (
      <Card className={cn("overflow-hidden rounded-2xl border-border/50", className)}>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <div className="h-4 w-20 animate-pulse rounded bg-muted"></div>
          <div className="h-8 w-8 animate-pulse rounded-full bg-muted"></div>
        </CardHeader>
        <CardContent>
          <div className="h-8 w-32 animate-pulse rounded bg-muted"></div>
          <div className="mt-2 h-4 w-16 animate-pulse rounded bg-muted"></div>
        </CardContent>
      </Card>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay, duration: 0.4 }}
    >
        <Card
        className={cn(
            "glass-panel transition-all duration-300 hover:shadow-xl hover:shadow-primary/10 hover:-translate-y-1 relative group overflow-hidden h-full",
            className
        )}
        >
        {/* Simple gradient background effect */}
        <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500" />

      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2 relative z-10">
        <CardTitle className="text-sm font-medium text-muted-foreground">
          {title}
        </CardTitle>
        <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-primary/10 text-primary transition-colors group-hover:bg-primary group-hover:text-primary-foreground">
             <Icon className="h-4 w-4" />
        </div>
      </CardHeader>
      <CardContent className="relative z-10">
        <div className="flex items-baseline gap-2">
            <span className="text-2xl font-bold tracking-tight">
                <Counter value={value} />
            </span>
            {subValue && <span className="text-xs text-muted-foreground">{subValue}</span>}
        </div>

        {(trend && trendValue) && (
            <div className="mt-2 flex items-center text-xs">
            {trend === "up" ? (
                <span className="flex items-center gap-1 font-medium text-emerald-500 bg-emerald-500/10 px-2 py-0.5 rounded-full">
                <ArrowUp className="h-3 w-3" />
                {trendValue}
                </span>
            ) : trend === "down" ? (
                <span className="flex items-center gap-1 font-medium text-rose-500 bg-rose-500/10 px-2 py-0.5 rounded-full">
                <ArrowDown className="h-3 w-3" />
                {trendValue}
                </span>
            ) : (
                <span className="text-muted-foreground">{trendValue}</span>
            )}
            <span className="ml-2 text-muted-foreground">from last month</span>
            </div>
        )}
      </CardContent>
        </Card>
    </motion.div>
  );
}
