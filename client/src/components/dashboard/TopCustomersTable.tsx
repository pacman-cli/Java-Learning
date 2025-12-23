"use client"

import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import { CustomerLifetimeValueDto } from "@/types"
import { Trophy } from "lucide-react"

interface TopCustomersTableProps {
    customers: CustomerLifetimeValueDto[]
}

export function TopCustomersTable({ customers }: TopCustomersTableProps) {
    return (
        <div className="rounded-xl border bg-card text-card-foreground shadow-sm h-full">
            <div className="flex flex-col space-y-1.5 p-6 pb-4">
                <h3 className="font-semibold leading-none tracking-tight flex items-center gap-2">
                    <Trophy className="h-5 w-5 text-yellow-500" />
                    Top Customers
                </h3>
                <p className="text-sm text-muted-foreground">
                    Highest value customers by lifetime revenue.
                </p>
            </div>
            <div className="p-0">
                <div className="relative w-full overflow-auto">
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHead className="w-[60px] text-center">Rank</TableHead>
                                <TableHead>Customer</TableHead>
                                <TableHead className="text-right">Lifetime Value</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {customers.map((customer) => (
                                <TableRow key={customer.rank}>
                                    <TableCell className="font-medium text-center">
                                        {customer.rank === 1 ? (
                                            <span className="text-xl">🥇</span>
                                        ) : customer.rank === 2 ? (
                                            <span className="text-xl">🥈</span>
                                        ) : customer.rank === 3 ? (
                                            <span className="text-xl">🥉</span>
                                        ) : (
                                            <span className="text-muted-foreground">#{customer.rank}</span>
                                        )}
                                    </TableCell>
                                    <TableCell className="font-medium">{customer.customerName}</TableCell>
                                    <TableCell className="text-right font-bold">
                                        ${customer.lifetimeRevenue.toLocaleString(undefined, {
                                            minimumFractionDigits: 2,
                                            maximumFractionDigits: 2,
                                        })}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </div>
            </div>
        </div>
    )
}
