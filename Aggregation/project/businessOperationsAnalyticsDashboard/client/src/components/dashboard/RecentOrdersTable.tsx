"use client";

import { ChartWrapper } from "@/components/dashboard/ChartWrapper";
import { Button } from "@/components/ui/button";
import { Order } from "@/types";
import { format } from "date-fns";
import { ArrowUpRight } from "lucide-react";

interface RecentOrdersTableProps {
  orders: Order[];
}

export function RecentOrdersTable({ orders }: RecentOrdersTableProps) {
  return (
    <ChartWrapper
        title="Recent Transactions"
        description="Latest customer orders and status updates."
        className="col-span-4 lg:col-span-6"
        action={
            <Button variant="ghost" size="sm" className="h-8 gap-1 text-primary">
                View All <ArrowUpRight className="h-3.5 w-3.5" />
            </Button>
        }
    >
        <div className="relative w-full overflow-auto">
            <table className="w-full caption-bottom text-sm text-left">
                <thead className="[&_tr]:border-b border-border/30">
                    <tr className="border-b border-border/30 transition-colors hover:bg-white/40 data-[state=selected]:bg-muted">
                        <th className="h-10 px-4 text-left align-middle font-medium text-muted-foreground">Customer</th>
                        <th className="h-10 px-4 text-left align-middle font-medium text-muted-foreground hidden md:table-cell">Date</th>
                        <th className="h-10 px-4 text-left align-middle font-medium text-muted-foreground">Status</th>
                        <th className="h-10 px-4 text-right align-middle font-medium text-muted-foreground">Amount</th>
                    </tr>
                </thead>
                <tbody className="[&_tr:last-child]:border-0">
                    {orders.map((order) => (
                        <tr key={order.id} className="border-b border-border/30 transition-colors hover:bg-white/40">
                            <td className="p-4 align-middle">
                                <div className="flex flex-col">
                                    <span className="font-medium text-foreground">{order.customer.firstName} {order.customer.lastName}</span>
                                    <span className="text-xs text-muted-foreground hidden sm:inline">{order.customer.email}</span>
                                </div>
                            </td>
                            <td className="p-4 align-middle hidden md:table-cell">
                                <span className="text-muted-foreground">{format(new Date(order.createdAt), "MMM dd, yyyy")}</span>
                            </td>
                            <td className="p-4 align-middle">
                                <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold ring-1 ring-inset ${
                                    order.status === 'COMPLETED' ? 'bg-emerald-50 text-emerald-700 ring-emerald-600/20 dark:bg-emerald-900/20 dark:text-emerald-400 dark:ring-emerald-500/20' :
                                    order.status === 'PENDING' ? 'bg-amber-50 text-amber-700 ring-amber-600/20 dark:bg-amber-900/20 dark:text-amber-400 dark:ring-amber-500/20' :
                                    'bg-rose-50 text-rose-700 ring-rose-600/20 dark:bg-rose-900/20 dark:text-rose-400 dark:ring-rose-500/20'
                                }`}>
                                    {order.status}
                                </span>
                            </td>
                            <td className="p-4 align-middle text-right font-semibold">
                                +${order.totalAmount.toFixed(2)}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    </ChartWrapper>
  );
}
