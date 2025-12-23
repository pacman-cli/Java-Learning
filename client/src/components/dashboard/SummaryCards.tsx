import { DashboardSummaryDto } from "@/types";
import { CheckCircle, DollarSign, ShoppingBag, XCircle } from "lucide-react";

interface SummaryCardsProps {
  data: DashboardSummaryDto;
}

export function SummaryCards({ data }: SummaryCardsProps) {
  const cards = [
    {
      label: "Total Revenue",
      value: `$${data.totalRevenue?.toLocaleString() ?? 0}`,
      icon: DollarSign,
      color: "text-green-600",
      bg: "bg-green-50",
    },
    {
      label: "Total Orders",
      value: data.totalOrders?.toLocaleString() ?? 0,
      icon: ShoppingBag,
      color: "text-blue-600",
      bg: "bg-blue-50",
    },
    {
      label: "Completed",
      value: data.completedOrders?.toLocaleString() ?? 0,
      icon: CheckCircle,
      color: "text-indigo-600",
      bg: "bg-indigo-50",
    },
    {
      label: "Canceled",
      value: data.canceledOrders?.toLocaleString() ?? 0,
      icon: XCircle,
      color: "text-red-600",
      bg: "bg-red-50",
    },
  ];

  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
      {cards.map((card) => {
        const Icon = card.icon;
        return (
          <div
            key={card.label}
            className="rounded-xl border border-gray-100 bg-white p-6 shadow-sm transition-all hover:shadow-md"
          >
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-500">{card.label}</p>
                <p className="mt-2 text-3xl font-bold text-gray-900">
                  {card.value}
                </p>
              </div>
              <div className={`rounded-full p-3 ${card.bg}`}>
                <Icon className={`h-6 w-6 ${card.color}`} />
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}
