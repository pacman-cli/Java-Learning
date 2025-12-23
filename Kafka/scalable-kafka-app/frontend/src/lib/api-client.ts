// API client for microservices backend
import { User, Order, Inventory } from "@/types";

const API_BASE_URL = "http://localhost:8080"; // API Gateway

export class ApiClient {
  private async request<T>(
    endpoint: string,
    options?: RequestInit
  ): Promise<T> {
    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        headers: {
          "Content-Type": "application/json",
          ...options?.headers,
        },
        ...options,
      });

      if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
      }

      return await response.json();
    } catch (error) {
      console.error(`API request failed for ${endpoint}:`, error);
      throw error;
    }
  }

  // User Service APIs
  async getUsers(): Promise<User[]> {
    return this.request<User[]>("/api/users");
  }

  async createUser(user: Omit<User, "id" | "createdAt">): Promise<User> {
    return this.request<User>("/api/users", {
      method: "POST",
      body: JSON.stringify(user),
    });
  }

  async getUserById(id: number): Promise<User> {
    return this.request<User>(`/api/users/${id}`);
  }

  async deleteUser(id: number): Promise<void> {
    return this.request<void>(`/api/users/${id}`, {
      method: "DELETE",
    });
  }

  // Order Service APIs
  async getOrders(): Promise<Order[]> {
    return this.request<Order[]>("/api/orders");
  }

  async createOrder(
    order: Omit<Order, "id" | "createdAt" | "status">
  ): Promise<Order> {
    return this.request<Order>("/api/orders", {
      method: "POST",
      body: JSON.stringify(order),
    });
  }

  // Inventory Service APIs
  async getInventory(): Promise<Inventory[]> {
    return this.request<Inventory[]>("/api/inventory");
  }

  async addInventoryItem(
    item: Omit<Inventory, "id" | "createdAt">
  ): Promise<Inventory> {
    return this.request<Inventory>("/api/inventory", {
      method: "POST",
      body: JSON.stringify(item),
    });
  }

  // Health check for services
  async checkServiceHealth(port: number): Promise<"online" | "offline"> {
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 5000);

      const response = await fetch(`http://localhost:${port}/actuator/health`, {
        method: "GET",
        signal: controller.signal,
      });

      clearTimeout(timeoutId);
      return response.ok ? "online" : "offline";
    } catch {
      return "offline";
    }
  }
}

export const apiClient = new ApiClient();
