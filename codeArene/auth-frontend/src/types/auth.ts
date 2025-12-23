export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  isEnabled: boolean;
  isAccountNonExpired: boolean;
  isAccountNonLocked: boolean;
  isCredentialsNonExpired: boolean;
  createdAt: string;
  updatedAt: string;
  lastLogin?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
}

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface PasswordChangeRequest {
  password: string;
}

export interface RoleUpdateRequest {
  role: UserRole;
}

export interface UserListResponse {
  users: User[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface UserStats {
  totalUsers: number;
  adminCount: number;
  moderatorCount: number;
  userCount: number;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  details?: Record<string, string>;
  timestamp: string;
}

export interface HealthCheck {
  status: string;
  service: string;
  timestamp: string;
  version: string;
}

export type UserRole = 'USER' | 'ADMIN' | 'MODERATOR';

export interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (credentials: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
  isAdmin: boolean;
  isModerator: boolean;
  loading: boolean;
}

export interface PaginationParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}
