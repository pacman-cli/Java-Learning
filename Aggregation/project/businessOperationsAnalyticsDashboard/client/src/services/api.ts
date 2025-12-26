import axios from "axios"
import {
  CategorySalesDto,
  CustomerLifetimeValueDto,
  DashboardAdvanceDto,
  DashboardSummaryDto,
  MonthlyRevenueDto,
  Order,
} from "../types"

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080/api/dashboard"

const api = axios.create({
  baseURL: API_BASE_URL,
})

export const getDashboardSummary = async (): Promise<DashboardSummaryDto> => {
  const response = await api.get<DashboardSummaryDto>("/summary")
  // Ensure we handle potential nulls if backend sends them, though primitives shouldn't be null
  // But DTO has wrapper classes (Long, Double) so they could be null.
  // We can default them here or in the UI.
  return response.data
}

export const getMonthlyRevenue = async (): Promise<MonthlyRevenueDto[]> => {
  const response = await api.get<MonthlyRevenueDto[]>("/monthly-revenue")
  return response.data
}

export const getCategorySales = async (): Promise<CategorySalesDto[]> => {
  const response = await api.get<CategorySalesDto[]>("/category-sales")
  return response.data
}

export const getRecentOrders = async (): Promise<Order[]> => {
  const response = await api.get<Order[]>("/orders")
  return response.data
}

export const getTopCustomers = async (): Promise<CustomerLifetimeValueDto[]> => {
  const response = await api.get<CustomerLifetimeValueDto[]>("/top-customers")
  return response.data
}

export const getAdvancedDashboard = async (): Promise<DashboardAdvanceDto> => {
  const response = await api.get<DashboardAdvanceDto>("/advanced")
  return response.data
}
