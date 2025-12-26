"use client";

import { cn } from "@/lib/utils";
import { Order } from "@/types";
import { format } from "date-fns";
import { AnimatePresence, motion } from "framer-motion";
import { ArrowUpDown, Search, SlidersHorizontal } from "lucide-react";
import { useMemo, useState } from "react";

// Fallback UI components if not present (inline for simplicity in this file for now if components folders are messy)
// But ideally we use what is available. I will use standard HTML/Tailwind for safety where generic UI components are unknown.

interface OrdersDataTableProps {
    data: Order[];
}

export function OrdersDataTable({ data }: OrdersDataTableProps) {
    const [search, setSearch] = useState("");
    const [statusFilter, setStatusFilter] = useState("ALL");
    const [sortConfig, setSortConfig] = useState<{ key: keyof Order; direction: 'asc' | 'desc' } | null>(null);

    // Filter & Sort Logic
    const filteredData = useMemo(() => {
        let result = [...data];

        // Search
        if (search) {
            const lowerSearch = search.toLowerCase();
            result = result.filter(order =>
                order.id.toString().includes(lowerSearch) ||
                (order.customer.firstName + " " + order.customer.lastName).toLowerCase().includes(lowerSearch) ||
                order.customer.email.toLowerCase().includes(lowerSearch)
            );
        }

        // Status Filter
        if (statusFilter !== "ALL") {
            result = result.filter(order => order.status === statusFilter);
        }

        // Sort
        if (sortConfig) {
            result.sort((a, b) => {
                let aValue: any = a[sortConfig.key];
                let bValue: any = b[sortConfig.key];

                // Handle nested sort keys manually for now or simplistic approach
                if (sortConfig.key === 'customer') {
                    aValue = a.customer.firstName;
                    bValue = b.customer.firstName;
                }

                if (typeof aValue === 'string' && typeof bValue === 'string') {
                    return sortConfig.direction === 'asc'
                        ? aValue.localeCompare(bValue)
                        : bValue.localeCompare(aValue);
                }
                if (typeof aValue === 'number' && typeof bValue === 'number') {
                     return sortConfig.direction === 'asc' ? aValue - bValue : bValue - aValue;
                }
                return 0;
            });
        }

        return result;
    }, [data, search, statusFilter, sortConfig]);

    // Pagination Logic
    const ITEMS_PER_PAGE = 10;
    const [currentPage, setCurrentPage] = useState(1);

    // Reset page when filters change
    useMemo(() => {
        setCurrentPage(1);
    }, [search, statusFilter]);

    const totalPages = Math.ceil(filteredData.length / ITEMS_PER_PAGE);
    const paginatedData = filteredData.slice(
        (currentPage - 1) * ITEMS_PER_PAGE,
        currentPage * ITEMS_PER_PAGE
    );

    const handlePageChange = (newPage: number) => {
        if (newPage >= 1 && newPage <= totalPages) {
            setCurrentPage(newPage);
        }
    };

    const requestSort = (key: keyof Order) => {
        let direction: 'asc' | 'desc' = 'asc';
        if (sortConfig && sortConfig.key === key && sortConfig.direction === 'asc') {
            direction = 'desc';
        }
        setSortConfig({ key, direction });
    };

    return (
        <div className="space-y-4">
             {/* Controls Bar */}
             <div className="flex flex-col sm:flex-row gap-4 items-center justify-between bg-card/50 p-4 rounded-xl border border-border/50 backdrop-blur-sm">
                <div className="relative w-full sm:w-72">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                    <input
                        type="text"
                        placeholder="Search orders..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        className="w-full bg-background/50 border border-border rounded-lg pl-9 pr-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-primary/20 transition-all"
                    />
                </div>
                <div className="flex items-center gap-2 w-full sm:w-auto">
                    <SlidersHorizontal className="h-4 w-4 text-muted-foreground" />
                    <select
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                        className="bg-background/50 border border-border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-primary/20 transition-all cursor-pointer min-w-[140px]"
                    >
                        <option value="ALL">All Status</option>
                        <option value="COMPLETED">Completed</option>
                        <option value="PENDING">Pending</option>
                        <option value="CANCELED">Canceled</option>
                    </select>
                </div>
             </div>

             {/* Table Card */}
             <div className="rounded-xl border border-border/50 bg-card/50 backdrop-blur-sm overflow-hidden shadow-sm">
                <div className="overflow-x-auto">
                    <table className="w-full text-sm text-left">
                        <thead className="bg-muted/50 text-muted-foreground font-medium border-b border-border/50">
                            <tr>
                                <th className="px-6 py-4 cursor-pointer hover:text-foreground transition-colors group" onClick={() => requestSort('id')}>
                                    <div className="flex items-center gap-1">
                                        Order ID
                                        <ArrowUpDown className="h-3 w-3 opacity-0 group-hover:opacity-100 transition-opacity" />
                                    </div>
                                </th>
                                <th className="px-6 py-4 cursor-pointer hover:text-foreground transition-colors group" onClick={() => requestSort('customer')}>
                                    <div className="flex items-center gap-1">
                                        Customer
                                        <ArrowUpDown className="h-3 w-3 opacity-0 group-hover:opacity-100 transition-opacity" />
                                    </div>
                                </th>
                                <th className="px-6 py-4 cursor-pointer hover:text-foreground transition-colors group" onClick={() => requestSort('createdAt')}>
                                    <div className="flex items-center gap-1">
                                        Date
                                        <ArrowUpDown className="h-3 w-3 opacity-0 group-hover:opacity-100 transition-opacity" />
                                    </div>
                                </th>
                                <th className="px-6 py-4">Status</th>
                                <th className="px-6 py-4 text-right cursor-pointer hover:text-foreground transition-colors group" onClick={() => requestSort('totalAmount')}>
                                    <div className="flex items-center justify-end gap-1">
                                        Amount
                                        <ArrowUpDown className="h-3 w-3 opacity-0 group-hover:opacity-100 transition-opacity" />
                                    </div>
                                </th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-border/30">
                            <AnimatePresence mode="wait">
                                {paginatedData.length > 0 ? (
                                    paginatedData.map((order, i) => (
                                        <motion.tr
                                            key={order.id}
                                            initial={{ opacity: 0, y: 10 }}
                                            animate={{ opacity: 1, y: 0 }}
                                            exit={{ opacity: 0 }}
                                            transition={{ delay: i * 0.05, duration: 0.3 }}
                                            className="group hover:bg-primary/5 transition-colors cursor-default"
                                        >
                                            <td className="px-6 py-4 font-medium text-foreground">
                                                #{order.id}
                                            </td>
                                            <td className="px-6 py-4">
                                                <div className="flex flex-col">
                                                    <span className="font-medium text-foreground">{order.customer.firstName} {order.customer.lastName}</span>
                                                    <span className="text-xs text-muted-foreground hidden sm:inline-block">{order.customer.email}</span>
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 text-muted-foreground whitespace-nowrap">
                                                {format(new Date(order.createdAt), "MMM dd, yyyy")}
                                            </td>
                                            <td className="px-6 py-4">
                                                <span className={cn(
                                                    "inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium border",
                                                    order.status === "COMPLETED" && "bg-emerald-500/10 text-emerald-600 border-emerald-500/20",
                                                    order.status === "PENDING" && "bg-amber-500/10 text-amber-600 border-amber-500/20",
                                                    order.status === "CANCELED" && "bg-rose-500/10 text-rose-600 border-rose-500/20",
                                                )}>
                                                    {order.status}
                                                </span>
                                            </td>
                                            <td className="px-6 py-4 text-right font-medium text-foreground font-mono">
                                                ${order.totalAmount.toLocaleString()}
                                            </td>
                                        </motion.tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan={5} className="px-6 py-12 text-center text-muted-foreground">
                                            No orders found matching your filters.
                                        </td>
                                    </tr>
                                )}
                            </AnimatePresence>
                        </tbody>
                    </table>
                </div>
                <div className="px-6 py-4 border-t border-border/30 bg-muted/20 text-xs text-muted-foreground flex flex-col sm:flex-row justify-between items-center gap-4">
                   <div className="flex items-center gap-1">
                       <span>Showing {paginatedData.length > 0 ? (currentPage - 1) * ITEMS_PER_PAGE + 1 : 0}</span>
                       <span>to {Math.min(currentPage * ITEMS_PER_PAGE, filteredData.length)}</span>
                       <span>of {filteredData.length} records</span>
                   </div>

                   <div className="flex items-center gap-2">
                       <button
                            onClick={() => handlePageChange(currentPage - 1)}
                            disabled={currentPage === 1}
                            className="px-3 py-1 rounded-lg border border-border bg-background hover:bg-muted disabled:opacity-50 disabled:cursor-not-allowed transition-colors font-medium text-foreground"
                       >
                           Previous
                       </button>
                       <span className="text-foreground font-medium px-2">
                           Page {currentPage} of {totalPages || 1}
                       </span>
                       <button
                            onClick={() => handlePageChange(currentPage + 1)}
                            disabled={currentPage === totalPages || totalPages === 0}
                            className="px-3 py-1 rounded-lg border border-border bg-background hover:bg-muted disabled:opacity-50 disabled:cursor-not-allowed transition-colors font-medium text-foreground"
                       >
                           Next
                       </button>
                   </div>
                </div>
             </div>
        </div>
    );
}
