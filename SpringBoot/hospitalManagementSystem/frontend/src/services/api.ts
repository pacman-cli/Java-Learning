import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from "axios";
import Cookies from "js-cookie";
import toast from "react-hot-toast";

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";

// Create axios instance
const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor to add auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = Cookies.get("authToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  (error) => {
    const { response } = error;

    if (response) {
      const { status, data } = response;

      switch (status) {
        case 401:
          // Unauthorized - clear auth data and redirect to login
          Cookies.remove("authToken");
          Cookies.remove("authUser");
          if (typeof window !== "undefined") {
            window.location.href = "/login";
          }
          toast.error("Session expired. Please login again.");
          break;

        case 403:
          toast.error("You don't have permission to perform this action.");
          break;

        case 404:
          toast.error("The requested resource was not found.");
          break;

        case 409:
          toast.error(
            data?.message ||
              "A conflict occurred. The resource may already exist.",
          );
          break;

        case 422:
          // Validation errors
          if (data?.validationErrors) {
            const errors = Object.values(data.validationErrors).join(", ");
            toast.error(`Validation error: ${errors}`);
          } else {
            toast.error(data?.message || "Validation failed.");
          }
          break;

        case 429:
          toast.error("Too many requests. Please try again later.");
          break;

        case 500:
          toast.error("Internal server error. Please try again later.");
          break;

        default:
          if (status >= 400) {
            toast.error(data?.message || "An unexpected error occurred.");
          }
          break;
      }
    } else if (error.code === "ECONNABORTED") {
      toast.error("Request timeout. Please check your connection.");
    } else if (error.message === "Network Error") {
      toast.error("Network error. Please check your connection.");
    } else {
      toast.error("An unexpected error occurred.");
    }

    return Promise.reject(error);
  },
);

// Generic API methods
export const api = {
  // GET request
  get: async <T = any>(
    url: string,
    config?: AxiosRequestConfig,
  ): Promise<T> => {
    const response = await apiClient.get<T>(url, config);
    return response.data;
  },

  // POST request
  post: async <T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig,
  ): Promise<T> => {
    const response = await apiClient.post<T>(url, data, config);
    return response.data;
  },

  // PUT request
  put: async <T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig,
  ): Promise<T> => {
    const response = await apiClient.put<T>(url, data, config);
    return response.data;
  },

  // PATCH request
  patch: async <T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig,
  ): Promise<T> => {
    const response = await apiClient.patch<T>(url, data, config);
    return response.data;
  },

  // DELETE request
  delete: async <T = any>(
    url: string,
    config?: AxiosRequestConfig,
  ): Promise<T> => {
    const response = await apiClient.delete<T>(url, config);
    return response.data;
  },

  // File upload
  upload: async <T = any>(
    url: string,
    formData: FormData,
    onUploadProgress?: (progressEvent: any) => void,
  ): Promise<T> => {
    const response = await apiClient.post<T>(url, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      onUploadProgress,
    });
    return response.data;
  },

  // File download
  download: async (
    url: string,
    filename?: string,
    config?: AxiosRequestConfig,
  ): Promise<void> => {
    const response = await apiClient.get(url, {
      ...config,
      responseType: "blob",
    });

    // Create blob link to download
    const blob = new Blob([response.data]);
    const link = document.createElement("a");
    link.href = window.URL.createObjectURL(blob);

    // Use provided filename or extract from Content-Disposition header
    const contentDisposition = response.headers["content-disposition"];
    let downloadFilename = filename;

    if (!downloadFilename && contentDisposition) {
      const filenameMatch = contentDisposition.match(/filename="(.+)"/);
      if (filenameMatch) {
        downloadFilename = filenameMatch[1];
      }
    }

    if (!downloadFilename) {
      downloadFilename = "download";
    }

    link.download = downloadFilename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(link.href);
  },
};

// Specialized API endpoints
export const authApi = {
  login: (credentials: { username: string; password: string }) =>
    api.post("/api/auth/login", credentials),

  register: (userData: {
    username: string;
    password: string;
    fullName: string;
    email: string;
    roles: string[];
  }) => api.post("/api/auth/register", userData),

  getCurrentUser: () => api.get("/api/auth/me"),

  logout: () => api.post("/api/auth/logout"),
};

export const patientsApi = {
  getAll: () => api.get("/api/patients"),

  getPage: (params: {
    q?: string;
    page?: number;
    size?: number;
    sort?: string;
  }) => {
    const queryParams = new URLSearchParams();
    if (params.q) queryParams.append("q", params.q);
    if (params.page !== undefined)
      queryParams.append("page", params.page.toString());
    if (params.size !== undefined)
      queryParams.append("size", params.size.toString());
    if (params.sort) queryParams.append("sort", params.sort);

    return api.get(`/api/patients/page?${queryParams.toString()}`);
  },

  getById: (id: number) => api.get(`/api/patients/${id}`),

  getByUserId: (userId: number) => api.get(`/api/patients/by-user/${userId}`),

  create: (patient: any) => api.post("/api/patients", patient),

  update: (id: number, patient: any) => api.put(`/api/patients/${id}`, patient),

  delete: (id: number) => api.delete(`/api/patients/${id}`),
};

export const doctorsApi = {
  getAll: () => api.get("/api/doctors"),

  getPage: (params: {
    q?: string;
    page?: number;
    size?: number;
    sort?: string;
  }) => {
    const queryParams = new URLSearchParams();
    if (params.q) queryParams.append("q", params.q);
    if (params.page !== undefined)
      queryParams.append("page", params.page.toString());
    if (params.size !== undefined)
      queryParams.append("size", params.size.toString());
    if (params.sort) queryParams.append("sort", params.sort);

    return api.get(`/api/doctors/page?${queryParams.toString()}`);
  },

  getById: (id: number) => api.get(`/api/doctors/${id}`),

  create: (doctor: any) => api.post("/api/doctors", doctor),

  update: (id: number, doctor: any) => api.put(`/api/doctors/${id}`, doctor),

  delete: (id: number) => api.delete(`/api/doctors/${id}`),
};

export const appointmentsApi = {
  getAll: () => api.get("/api/appointments"),

  getPage: (params: {
    q?: string;
    page?: number;
    size?: number;
    sort?: string;
  }) => {
    const queryParams = new URLSearchParams();
    if (params.q) queryParams.append("q", params.q);
    if (params.page !== undefined)
      queryParams.append("page", params.page.toString());
    if (params.size !== undefined)
      queryParams.append("size", params.size.toString());
    if (params.sort) queryParams.append("sort", params.sort);

    return api.get(`/api/appointments/page?${queryParams.toString()}`);
  },

  getById: (id: number) => api.get(`/api/appointments/${id}`),

  create: (appointment: any) => api.post("/api/appointments", appointment),

  update: (id: number, appointment: any) =>
    api.put(`/api/appointments/${id}`, appointment),

  delete: (id: number) => api.delete(`/api/appointments/${id}`),

  getByPatient: (patientId: number) =>
    api.get(`/api/appointments/patient/${patientId}`),

  getByDoctor: (doctorId: number) =>
    api.get(`/api/appointments/doctor/${doctorId}`),

  getUpcoming: () => api.get("/api/appointments/upcoming"),

  getTodaysAppointments: () => api.get("/api/appointments/today"),

  updateStatus: (id: number, status: string) =>
    api.patch(`/api/appointments/${id}/status?status=${status}`),

  cancel: (id: number) => api.patch(`/api/appointments/${id}/cancel`),

  confirm: (id: number) => api.patch(`/api/appointments/${id}/confirm`),

  complete: (id: number) => api.patch(`/api/appointments/${id}/complete`),
};

export const documentsApi = {
  getAll: () => api.get("/api/documents"),

  getByPatient: (patientId: number) =>
    api.get(`/api/documents/patient/${patientId}`),

  getById: (id: number) => api.get(`/api/documents/${id}`),

  upload: (
    formData: FormData,
    onUploadProgress?: (progressEvent: any) => void,
  ) => api.upload("/api/documents/upload", formData, onUploadProgress),

  download: (id: number, filename?: string) =>
    api.download(`/api/documents/${id}/download`, filename),

  delete: (id: number) => api.delete(`/api/documents/${id}`),

  updateMetadata: (id: number, metadata: any) =>
    api.put(`/api/documents/${id}/metadata`, metadata),
};

export const billingApi = {
  getAll: () => api.get("/api/billings"),

  getById: (id: number) => api.get(`/api/billings/${id}`),

  create: (bill: any) => api.post("/api/billings", bill),

  update: (id: number, bill: any) => api.put(`/api/billings/${id}`, bill),

  delete: (id: number) => api.delete(`/api/billings/${id}`),

  getByPatient: (patientId: number) =>
    api.get(`/api/billings/patient/${patientId}`),

  generateInvoice: (billId: number) =>
    api.post(`/api/billings/${billId}/invoice`),

  markAsPaid: (billId: number, paymentMethod?: string) =>
    api.post(`/api/billings/${billId}/pay`, { paymentMethod }),
};

export const reportsApi = {
  getDashboardStats: () => api.get("/api/reports/dashboard"),

  getPatientStats: () => api.get("/api/reports/patients"),

  getAppointmentStats: () => api.get("/api/reports/appointments"),

  getRevenueStats: () => api.get("/api/reports/revenue"),

  exportPatients: (format: "csv" | "xlsx" = "csv") =>
    api.download(
      `/api/reports/patients/export?format=${format}`,
      `patients.${format}`,
    ),

  exportAppointments: (format: "csv" | "xlsx" = "csv") =>
    api.download(
      `/api/reports/appointments/export?format=${format}`,
      `appointments.${format}`,
    ),
};

export const medicalRecordsApi = {
  getAll: () => api.get("/api/medical-records"),

  getById: (id: number) => api.get(`/api/medical-records/${id}`),

  getByPatient: (patientId: number) =>
    api.get(`/api/medical-records/patient/${patientId}`),

  create: (record: {
    recordType: string;
    title: string;
    content: string;
    patientId: number;
    filePath?: string;
  }) => api.post("/api/medical-records", record),

  update: (
    id: number,
    record: Partial<{
      recordType: string;
      title: string;
      content: string;
      filePath?: string;
    }>,
  ) => api.put(`/api/medical-records/${id}`, record),

  delete: (id: number) => api.delete(`/api/medical-records/${id}`),

  uploadFile: (
    id: number,
    file: File,
    onUploadProgress?: (progressEvent: any) => void,
  ) => {
    const formData = new FormData();
    formData.append("file", file);
    return api.upload(
      `/api/medical-records/${id}/upload`,
      formData,
      onUploadProgress,
    );
  },
};

export const labOrdersApi = {
  getAll: () => api.get("/api/lab-orders"),

  getById: (id: number) => api.get(`/api/lab-orders/${id}`),

  getByPatient: (patientId: number) =>
    api.get(`/api/lab-orders/patient/${patientId}`),

  create: (order: {
    labTestId: number;
    patientId: number;
    appointmentId?: number;
    notes?: string;
  }) => api.post("/api/lab-orders", order),

  update: (
    id: number,
    order: Partial<{
      labTestId: number;
      patientId: number;
      appointmentId?: number;
      status: string;
      notes?: string;
    }>,
  ) => api.put(`/api/lab-orders/${id}`, order),

  delete: (id: number) => api.delete(`/api/lab-orders/${id}`),

  changeStatus: (id: number, status: string) =>
    api.post(`/api/lab-orders/${id}/status`, { status }),

  attachReport: (
    id: number,
    file: File,
    onUploadProgress?: (progressEvent: any) => void,
  ) => {
    const formData = new FormData();
    formData.append("file", file);
    return api.upload(
      `/api/lab-orders/${id}/report`,
      formData,
      onUploadProgress,
    );
  },
};

export const labTestsApi = {
  getAll: () => api.get("/api/lab-tests"),

  getById: (id: number) => api.get(`/api/lab-tests/${id}`),

  create: (test: { testName: string; description?: string; price?: number }) =>
    api.post("/api/lab-tests", test),

  update: (
    id: number,
    test: Partial<{
      testName: string;
      description?: string;
      price?: number;
    }>,
  ) => api.put(`/api/lab-tests/${id}`, test),

  delete: (id: number) => api.delete(`/api/lab-tests/${id}`),
};

export const prescriptionsApi = {
  getAll: () => api.get("/api/prescriptions"),

  getById: (id: number) => api.get(`/api/prescriptions/${id}`),

  getByPatient: (patientId: number) =>
    api.get(`/api/prescriptions/patient/${patientId}`),

  create: (prescription: {
    patientId: number;
    doctorId: number;
    medicineId: number;
    notes?: string;
  }) => api.post("/api/prescriptions", prescription),

  update: (
    id: number,
    prescription: Partial<{
      patientId: number;
      doctorId: number;
      medicineId: number;
      notes?: string;
    }>,
  ) => api.put(`/api/prescriptions/${id}`, prescription),

  delete: (id: number) => api.delete(`/api/prescriptions/${id}`),
};

export const medicinesApi = {
  getAll: () => api.get("/api/medicines"),

  getById: (id: number) => api.get(`/api/medicines/${id}`),

  create: (medicine: {
    name: string;
    description?: string;
    manufacturer?: string;
    price?: number;
    stockQuantity?: number;
  }) => api.post("/api/medicines", medicine),

  update: (
    id: number,
    medicine: Partial<{
      name: string;
      description?: string;
      manufacturer?: string;
      price?: number;
      stockQuantity?: number;
    }>,
  ) => api.put(`/api/medicines/${id}`, medicine),

  delete: (id: number) => api.delete(`/api/medicines/${id}`),
};

export default apiClient;
