import React, { useState, useEffect, useCallback } from "react";
import { Link } from "react-router-dom";
import {
    MagnifyingGlassIcon,
    ChevronLeftIcon,
    ChevronRightIcon,
    TrashIcon,
    LockClosedIcon,
    LockOpenIcon,
    UserPlusIcon,
    CheckCircleIcon,
    XCircleIcon,
} from "@heroicons/react/24/outline";
import { toast } from "react-toastify";
import { useAuth } from "../context/AuthContext";
import {
    User,
    UserListResponse,
    UserRole,
    PaginationParams,
} from "../types/auth";
import apiService from "../services/api";
import LoadingSpinner from "../components/LoadingSpinner";
import {
    formatRelativeTime,
    getRoleBadgeColor,
    formatUserRole,
    getStatusBadgeColor,
    getStatusText,
    getInitials,
} from "../utils/formatters";

const Users: React.FC = () => {
    const { isAdmin } = useAuth();
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedRole, setSelectedRole] = useState<string>("all");
    const [statusFilter, setStatusFilter] = useState<string>("all");
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [sortBy, setSortBy] = useState("createdAt");
    const [sortDir, setSortDir] = useState<"asc" | "desc">("desc");
    const [actionLoading, setActionLoading] = useState<{
        [key: number]: string;
    }>({});

    const fetchUsers = useCallback(
        async (params: PaginationParams = {}) => {
            try {
                setLoading(true);
                setError(null);

                const paginationParams: PaginationParams = {
                    page: currentPage,
                    size: pageSize,
                    sortBy,
                    sortDir,
                    ...params,
                };

                const response: UserListResponse =
                    await apiService.getAllUsers(paginationParams);

                let filteredUsers = response.users;

                // Apply client-side filters
                if (searchTerm) {
                    filteredUsers = filteredUsers.filter(
                        (user) =>
                            user.username
                                .toLowerCase()
                                .includes(searchTerm.toLowerCase()) ||
                            user.email
                                .toLowerCase()
                                .includes(searchTerm.toLowerCase()) ||
                            user.firstName
                                .toLowerCase()
                                .includes(searchTerm.toLowerCase()) ||
                            user.lastName
                                .toLowerCase()
                                .includes(searchTerm.toLowerCase()),
                    );
                }

                if (selectedRole !== "all") {
                    filteredUsers = filteredUsers.filter(
                        (user) => user.role === selectedRole,
                    );
                }

                if (statusFilter !== "all") {
                    if (statusFilter === "active") {
                        filteredUsers = filteredUsers.filter(
                            (user) => user.isEnabled && user.isAccountNonLocked,
                        );
                    } else if (statusFilter === "disabled") {
                        filteredUsers = filteredUsers.filter(
                            (user) => !user.isEnabled,
                        );
                    } else if (statusFilter === "locked") {
                        filteredUsers = filteredUsers.filter(
                            (user) => !user.isAccountNonLocked,
                        );
                    }
                }

                setUsers(filteredUsers);
                setTotalPages(response.totalPages);
                setTotalElements(response.totalElements);
            } catch (err: any) {
                setError(err.response?.data?.message || "Failed to load users");
            } finally {
                setLoading(false);
            }
        },
        [
            currentPage,
            pageSize,
            sortBy,
            sortDir,
            searchTerm,
            selectedRole,
            statusFilter,
        ],
    );

    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    useEffect(() => {
        const timeoutId = setTimeout(() => {
            if (currentPage === 0) {
                fetchUsers();
            } else {
                setCurrentPage(0);
            }
        }, 500);

        return () => clearTimeout(timeoutId);
    }, [searchTerm, selectedRole, statusFilter, currentPage, fetchUsers]);

    const handleUserAction = async (userId: number, action: string) => {
        if (!isAdmin) {
            toast.error("You do not have permission to perform this action");
            return;
        }

        try {
            setActionLoading((prev) => ({ ...prev, [userId]: action }));

            let updatedUser: User;
            let successMessage: string;

            switch (action) {
                case "enable":
                    updatedUser = await apiService.enableUser(userId);
                    successMessage = "User enabled successfully";
                    break;
                case "disable":
                    updatedUser = await apiService.disableUser(userId);
                    successMessage = "User disabled successfully";
                    break;
                case "lock":
                    updatedUser = await apiService.lockUser(userId);
                    successMessage = "User locked successfully";
                    break;
                case "unlock":
                    updatedUser = await apiService.unlockUser(userId);
                    successMessage = "User unlocked successfully";
                    break;
                case "delete":
                    if (
                        window.confirm(
                            "Are you sure you want to delete this user? This action cannot be undone.",
                        )
                    ) {
                        await apiService.deleteUser(userId);
                        successMessage = "User deleted successfully";
                        setUsers((prev) =>
                            prev.filter((user) => user.id !== userId),
                        );
                        toast.success(successMessage);
                    }
                    return;
                default:
                    throw new Error("Invalid action");
            }

            // Update the user in the list
            setUsers((prev) =>
                prev.map((user) => (user.id === userId ? updatedUser : user)),
            );
            toast.success(successMessage);
        } catch (error: any) {
            toast.error(
                error.response?.data?.message || `Failed to ${action} user`,
            );
        } finally {
            setActionLoading((prev) => {
                const newState = { ...prev };
                delete newState[userId];
                return newState;
            });
        }
    };

    const handleRoleChange = async (userId: number, newRole: UserRole) => {
        if (!isAdmin) {
            toast.error("You do not have permission to perform this action");
            return;
        }

        try {
            setActionLoading((prev) => ({ ...prev, [userId]: "role" }));
            const updatedUser = await apiService.updateUserRole(userId, {
                role: newRole,
            });
            setUsers((prev) =>
                prev.map((user) => (user.id === userId ? updatedUser : user)),
            );
            toast.success("User role updated successfully");
        } catch (error: any) {
            toast.error(
                error.response?.data?.message || "Failed to update user role",
            );
        } finally {
            setActionLoading((prev) => {
                const newState = { ...prev };
                delete newState[userId];
                return newState;
            });
        }
    };

    const handleSort = (field: string) => {
        if (sortBy === field) {
            setSortDir(sortDir === "asc" ? "desc" : "asc");
        } else {
            setSortBy(field);
            setSortDir("asc");
        }
    };

    const getSortIcon = (field: string) => {
        if (sortBy !== field) return null;
        return sortDir === "asc" ? "↑" : "↓";
    };

    if (loading && users.length === 0) {
        return (
            <div className="flex items-center justify-center h-64">
                <LoadingSpinner size="large" />
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="md:flex md:items-center md:justify-between">
                <div className="flex-1 min-w-0">
                    <h1 className="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:truncate">
                        Users Management
                    </h1>
                    <p className="mt-1 text-sm text-gray-500">
                        Manage user accounts, roles, and permissions
                    </p>
                </div>
                <div className="mt-4 flex md:mt-0 md:ml-4">
                    <Link
                        to="/register"
                        className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
                    >
                        <UserPlusIcon className="h-4 w-4 mr-2" />
                        Add User
                    </Link>
                </div>
            </div>

            {error && (
                <div className="rounded-md bg-red-50 p-4">
                    <div className="text-sm text-red-700">{error}</div>
                </div>
            )}

            {/* Filters */}
            <div className="bg-white shadow rounded-lg">
                <div className="px-4 py-5 sm:p-6">
                    <div className="grid grid-cols-1 gap-4 sm:grid-cols-4">
                        {/* Search */}
                        <div className="sm:col-span-2">
                            <label
                                htmlFor="search"
                                className="block text-sm font-medium text-gray-700"
                            >
                                Search Users
                            </label>
                            <div className="mt-1 relative">
                                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <MagnifyingGlassIcon className="h-5 w-5 text-gray-400" />
                                </div>
                                <input
                                    type="text"
                                    id="search"
                                    value={searchTerm}
                                    onChange={(e) =>
                                        setSearchTerm(e.target.value)
                                    }
                                    className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-primary-500 focus:border-primary-500"
                                    placeholder="Search by name, username, or email..."
                                />
                            </div>
                        </div>

                        {/* Role Filter */}
                        <div>
                            <label
                                htmlFor="role"
                                className="block text-sm font-medium text-gray-700"
                            >
                                Role
                            </label>
                            <select
                                id="role"
                                value={selectedRole}
                                onChange={(e) =>
                                    setSelectedRole(e.target.value)
                                }
                                className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                            >
                                <option value="all">All Roles</option>
                                <option value="USER">User</option>
                                <option value="MODERATOR">Moderator</option>
                                <option value="ADMIN">Admin</option>
                            </select>
                        </div>

                        {/* Status Filter */}
                        <div>
                            <label
                                htmlFor="status"
                                className="block text-sm font-medium text-gray-700"
                            >
                                Status
                            </label>
                            <select
                                id="status"
                                value={statusFilter}
                                onChange={(e) =>
                                    setStatusFilter(e.target.value)
                                }
                                className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                            >
                                <option value="all">All Status</option>
                                <option value="active">Active</option>
                                <option value="disabled">Disabled</option>
                                <option value="locked">Locked</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            {/* Users Table */}
            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <div className="px-4 py-5 sm:px-6 border-b border-gray-200">
                    <div className="flex items-center justify-between">
                        <h3 className="text-lg leading-6 font-medium text-gray-900">
                            Users ({totalElements})
                        </h3>
                        {loading && <LoadingSpinner size="small" />}
                    </div>
                </div>

                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                            <tr>
                                <th
                                    className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                                    onClick={() => handleSort("username")}
                                >
                                    User {getSortIcon("username")}
                                </th>
                                <th
                                    className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                                    onClick={() => handleSort("role")}
                                >
                                    Role {getSortIcon("role")}
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Status
                                </th>
                                <th
                                    className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                                    onClick={() => handleSort("createdAt")}
                                >
                                    Created {getSortIcon("createdAt")}
                                </th>
                                <th
                                    className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
                                    onClick={() => handleSort("lastLogin")}
                                >
                                    Last Login {getSortIcon("lastLogin")}
                                </th>
                                {isAdmin && (
                                    <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        Actions
                                    </th>
                                )}
                            </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                            {users.map((user) => (
                                <tr key={user.id} className="hover:bg-gray-50">
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="flex items-center">
                                            <div className="flex-shrink-0 h-10 w-10">
                                                <div className="h-10 w-10 rounded-full bg-gray-300 flex items-center justify-center">
                                                    <span className="text-sm font-medium text-gray-700">
                                                        {getInitials(
                                                            user.firstName,
                                                            user.lastName,
                                                        )}
                                                    </span>
                                                </div>
                                            </div>
                                            <div className="ml-4">
                                                <div className="text-sm font-medium text-gray-900">
                                                    {user.firstName}{" "}
                                                    {user.lastName}
                                                </div>
                                                <div className="text-sm text-gray-500">
                                                    @{user.username} •{" "}
                                                    {user.email}
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        {isAdmin ? (
                                            <select
                                                value={user.role}
                                                onChange={(e) =>
                                                    handleRoleChange(
                                                        user.id,
                                                        e.target
                                                            .value as UserRole,
                                                    )
                                                }
                                                disabled={
                                                    !!actionLoading[user.id]
                                                }
                                                className="text-xs font-medium rounded-full px-2.5 py-0.5 border border-gray-300 focus:ring-primary-500 focus:border-primary-500"
                                            >
                                                <option value="USER">
                                                    User
                                                </option>
                                                <option value="MODERATOR">
                                                    Moderator
                                                </option>
                                                <option value="ADMIN">
                                                    Admin
                                                </option>
                                            </select>
                                        ) : (
                                            <span
                                                className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getRoleBadgeColor(user.role)}`}
                                            >
                                                {formatUserRole(user.role)}
                                            </span>
                                        )}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <span
                                            className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusBadgeColor(user.isEnabled, !user.isAccountNonLocked)}`}
                                        >
                                            {getStatusText(
                                                user.isEnabled,
                                                !user.isAccountNonLocked,
                                            )}
                                        </span>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {formatRelativeTime(user.createdAt)}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {formatRelativeTime(user.lastLogin)}
                                    </td>
                                    {isAdmin && (
                                        <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                            <div className="flex justify-end space-x-2">
                                                {actionLoading[user.id] ? (
                                                    <LoadingSpinner size="small" />
                                                ) : (
                                                    <>
                                                        <button
                                                            onClick={() =>
                                                                handleUserAction(
                                                                    user.id,
                                                                    user.isEnabled
                                                                        ? "disable"
                                                                        : "enable",
                                                                )
                                                            }
                                                            className={`p-1 rounded-md ${user.isEnabled ? "text-red-600 hover:text-red-900" : "text-green-600 hover:text-green-900"}`}
                                                            title={
                                                                user.isEnabled
                                                                    ? "Disable User"
                                                                    : "Enable User"
                                                            }
                                                        >
                                                            {user.isEnabled ? (
                                                                <XCircleIcon className="h-4 w-4" />
                                                            ) : (
                                                                <CheckCircleIcon className="h-4 w-4" />
                                                            )}
                                                        </button>
                                                        <button
                                                            onClick={() =>
                                                                handleUserAction(
                                                                    user.id,
                                                                    user.isAccountNonLocked
                                                                        ? "lock"
                                                                        : "unlock",
                                                                )
                                                            }
                                                            className={`p-1 rounded-md ${user.isAccountNonLocked ? "text-orange-600 hover:text-orange-900" : "text-blue-600 hover:text-blue-900"}`}
                                                            title={
                                                                user.isAccountNonLocked
                                                                    ? "Lock User"
                                                                    : "Unlock User"
                                                            }
                                                        >
                                                            {user.isAccountNonLocked ? (
                                                                <LockClosedIcon className="h-4 w-4" />
                                                            ) : (
                                                                <LockOpenIcon className="h-4 w-4" />
                                                            )}
                                                        </button>
                                                        <button
                                                            onClick={() =>
                                                                handleUserAction(
                                                                    user.id,
                                                                    "delete",
                                                                )
                                                            }
                                                            className="p-1 rounded-md text-red-600 hover:text-red-900"
                                                            title="Delete User"
                                                        >
                                                            <TrashIcon className="h-4 w-4" />
                                                        </button>
                                                    </>
                                                )}
                                            </div>
                                        </td>
                                    )}
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* Pagination */}
                {totalPages > 1 && (
                    <div className="bg-white px-4 py-3 border-t border-gray-200 sm:px-6">
                        <div className="flex items-center justify-between">
                            <div className="flex-1 flex justify-between sm:hidden">
                                <button
                                    onClick={() =>
                                        setCurrentPage(currentPage - 1)
                                    }
                                    disabled={currentPage === 0}
                                    className="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    Previous
                                </button>
                                <button
                                    onClick={() =>
                                        setCurrentPage(currentPage + 1)
                                    }
                                    disabled={currentPage >= totalPages - 1}
                                    className="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    Next
                                </button>
                            </div>
                            <div className="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
                                <div>
                                    <p className="text-sm text-gray-700">
                                        Showing page{" "}
                                        <span className="font-medium">
                                            {currentPage + 1}
                                        </span>{" "}
                                        of{" "}
                                        <span className="font-medium">
                                            {totalPages}
                                        </span>{" "}
                                        ({totalElements} total users)
                                    </p>
                                </div>
                                <div>
                                    <nav className="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                                        <button
                                            onClick={() =>
                                                setCurrentPage(currentPage - 1)
                                            }
                                            disabled={currentPage === 0}
                                            className="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                                        >
                                            <ChevronLeftIcon className="h-5 w-5" />
                                        </button>
                                        <span className="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium text-gray-700">
                                            {currentPage + 1} / {totalPages}
                                        </span>
                                        <button
                                            onClick={() =>
                                                setCurrentPage(currentPage + 1)
                                            }
                                            disabled={
                                                currentPage >= totalPages - 1
                                            }
                                            className="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                                        >
                                            <ChevronRightIcon className="h-5 w-5" />
                                        </button>
                                    </nav>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Users;
