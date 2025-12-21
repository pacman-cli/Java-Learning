// DTOs
export interface DashboardSummaryDto {
  totalOrders: number
  totalRevenue: number
  avgOrderValue: number
  completedOrders: number
  canceledOrders: number
}

export interface MonthlyRevenueDto {
  month: string
  revenue: number
}

export interface CategorySalesDto {
  categoryName: string
  totalRevenue: number
}

// Entities
// Using a simplified version of Order as returned by the API
export interface Order {
  id: number
  customer: Customer
  region: Region
  status: string
  totalAmount: number
  createdAt: string
}

export interface Customer {
  id: number
  firstName: string
  lastName: string
  email: string
  phone: string
}

export interface Region {
  id: number
  name: string
}

export interface CustomerLifetimeValueDto {
  customerName: string
  lifetimeRevenue: number
  rank: number
}

export interface RollingRevenueDto {
  date: string
  dailyRevenue: number
  rolling7DayAvg: number
}

export interface RegionConversionDto {
  region: string
  completedOrders: number
  canceledOrders: number
  conversionRate: number
}

export interface DashboardAdvanceDto {
  rollingRevenue: RollingRevenueDto[]
  conversionRateByRegion: RegionConversionDto[]
}
