import axios, { AxiosInstance, AxiosResponse } from 'axios';
import {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
  User,
  UserListResponse,
  UserStats,
  PasswordChangeRequest,
  RoleUpdateRequest,
  PaginationParams,
  HealthCheck
} from '../types/auth';

class ApiService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: process.env.REACT_APP_API_URL || '/api',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Request interceptor to add auth token
    this.api.interceptors.request.use((config) => {
      const token = localStorage.getItem('auth_token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    });

    // Response interceptor to handle errors
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          // Token expired or invalid
          localStorage.removeItem('auth_token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  // Authentication endpoints
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response: AxiosResponse<AuthResponse> = await this.api.post('/auth/login', credentials);
    return response.data;
  }

  async register(userData: RegisterRequest): Promise<AuthResponse> {
    const response: AxiosResponse<AuthResponse> = await this.api.post('/auth/register', userData);
    return response.data;
  }

  // User profile endpoints
  async getCurrentUser(): Promise<User> {
    const response: AxiosResponse<User> = await this.api.get('/user/profile');
    return response.data;
  }

  async getUserById(id: number): Promise<User> {
    const response: AxiosResponse<User> = await this.api.get(`/user/${id}`);
    return response.data;
  }

  async getUserByUsername(username: string): Promise<User> {
    const response: AxiosResponse<User> = await this.api.get(`/user/username/${username}`);
    return response.data;
  }

  // User management endpoints
  async getAllUsers(params: PaginationParams = {}): Promise<UserListResponse> {
    const queryParams = new URLSearchParams();

    if (params.page !== undefined) queryParams.append('page', params.page.toString());
    if (params.size !== undefined) queryParams.append('size', params.size.toString());
    if (params.sortBy) queryParams.append('sortBy', params.sortBy);
    if (params.sortDir) queryParams.append('sortDir', params.sortDir);

    const response: AxiosResponse<UserListResponse> = await this.api.get(
      `/user/all${queryParams.toString() ? '?' + queryParams.toString() : ''}`
    );
    return response.data;
  }

  async getUsersByRole(role: string): Promise<User[]> {
    const response: AxiosResponse<User[]> = await this.api.get(`/user/role/${role}`);
    return response.data;
  }

  async getActiveUsers(): Promise<User[]> {
    const response: AxiosResponse<User[]> = await this.api.get('/user/active');
    return response.data;
  }

  async getRecentUsers(limit: number = 10): Promise<User[]> {
    const response: AxiosResponse<User[]> = await this.api.get(`/user/recent?limit=${limit}`);
    return response.data;
  }

  async getUserStats(): Promise<UserStats> {
    const response: AxiosResponse<UserStats> = await this.api.get('/user/stats');
    return response.data;
  }

  // User administration endpoints
  async updateUserRole(userId: number, roleData: RoleUpdateRequest): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put(`/user/${userId}/role`, roleData);
    return response.data;
  }

  async enableUser(userId: number): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put(`/user/${userId}/enable`);
    return response.data;
  }

  async disableUser(userId: number): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put(`/user/${userId}/disable`);
    return response.data;
  }

  async lockUser(userId: number): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put(`/user/${userId}/lock`);
    return response.data;
  }

  async unlockUser(userId: number): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put(`/user/${userId}/unlock`);
    return response.data;
  }

  async changeUserPassword(userId: number, passwordData: PasswordChangeRequest): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put(`/user/${userId}/password`, passwordData);
    return response.data;
  }

  async deleteUser(userId: number): Promise<void> {
    await this.api.delete(`/user/${userId}`);
  }

  // Health check
  async getHealthCheck(): Promise<HealthCheck> {
    const response: AxiosResponse<HealthCheck> = await this.api.get('/health');
    return response.data;
  }

  // Utility methods
  setToken(token: string): void {
    localStorage.setItem('auth_token', token);
  }

  removeToken(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user');
  }

  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }
}

export const apiService = new ApiService();
export default apiService;
