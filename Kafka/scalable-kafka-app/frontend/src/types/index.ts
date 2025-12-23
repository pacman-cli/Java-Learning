// Type definitions for the microservices backend
export interface User {
  id?: number;
  username: string;
  email: string;
  password: string;
  createdAt?: string;
}

export interface Order {
  id?: number;
  userId: number;
  productName: string;
  quantity: number;
  price: number;
  status?: string;
  createdAt?: string;
}

export interface Inventory {
  id?: number;
  productName: string;
  quantityAvailable: number;
  createdAt?: string;
}

export interface ServiceHealth {
  service: string;
  status: "online" | "offline" | "pending";
  port: number;
  lastChecked?: string;
}

export interface EventLog {
  id: string;
  timestamp: string;
  service: string;
  event: string;
  message: string;
  type: "info" | "success" | "warning" | "error";
}

export interface ApiResponse<T> {
  data?: T;
  error?: string;
  message?: string;
}
