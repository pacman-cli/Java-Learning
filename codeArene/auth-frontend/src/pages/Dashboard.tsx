import React, { useState, useEffect } from 'react';
import {
  UsersIcon,
  UserGroupIcon,
  ShieldCheckIcon,
  ClockIcon,
  ChartBarIcon,
  EyeIcon,
} from '@heroicons/react/24/outline';
import { useAuth } from '../context/AuthContext';
import { User, UserStats } from '../types/auth';
import apiService from '../services/api';
import LoadingSpinner from '../components/LoadingSpinner';
import { formatDate, formatRelativeTime, getRoleBadgeColor, formatUserRole } from '../utils/formatters';
import { Link } from 'react-router-dom';

const Dashboard: React.FC = () => {
  const { user, isAdmin, isModerator } = useAuth();
  const [stats, setStats] = useState<UserStats | null>(null);
  const [recentUsers, setRecentUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        setError(null);

        const promises: Promise<any>[] = [];

        // Fetch stats if admin
        if (isAdmin) {
          promises.push(apiService.getUserStats());
        }

        // Fetch recent users if moderator or admin
        if (isModerator) {
          promises.push(apiService.getRecentUsers(5));
        }

        const results = await Promise.allSettled(promises);

        let resultIndex = 0;

        if (isAdmin) {
          const statsResult = results[resultIndex++];
          if (statsResult.status === 'fulfilled') {
            setStats(statsResult.value);
          }
        }

        if (isModerator) {
          const usersResult = results[resultIndex++];
          if (usersResult.status === 'fulfilled') {
            setRecentUsers(usersResult.value);
          }
        }
      } catch (err: any) {
        setError(err.response?.data?.message || 'Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, [isAdmin, isModerator]);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <LoadingSpinner size="large" />
      </div>
    );
  }

  const quickActions = [
    {
      name: 'View Profile',
      description: 'Manage your account settings',
      href: '/profile',
      icon: UsersIcon,
      show: true,
    },
    {
      name: 'Manage Users',
      description: 'View and manage all users',
      href: '/users',
      icon: UserGroupIcon,
      show: isModerator,
    },
    {
      name: 'Admin Panel',
      description: 'Access administrative functions',
      href: '/admin',
      icon: ShieldCheckIcon,
      show: isAdmin,
    },
    {
      name: 'Settings',
      description: 'Configure application settings',
      href: '/settings',
      icon: ChartBarIcon,
      show: true,
    },
  ].filter(action => action.show);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">
          Welcome back, {user?.firstName}!
        </h1>
        <p className="mt-1 text-sm text-gray-500">
          Here's what's happening with your Auth Service account today.
        </p>
      </div>

      {error && (
        <div className="rounded-md bg-red-50 p-4">
          <div className="text-sm text-red-700">{error}</div>
        </div>
      )}

      {/* User Info Card */}
      <div className="bg-white overflow-hidden shadow rounded-lg">
        <div className="px-4 py-5 sm:p-6">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <div className="h-16 w-16 rounded-full bg-primary-500 flex items-center justify-center">
                <span className="text-xl font-medium text-white">
                  {user?.firstName?.charAt(0)}{user?.lastName?.charAt(0)}
                </span>
              </div>
            </div>
            <div className="ml-4">
              <h3 className="text-lg font-medium text-gray-900">
                {user?.firstName} {user?.lastName}
              </h3>
              <p className="text-sm text-gray-500">@{user?.username}</p>
              <p className="text-sm text-gray-500">{user?.email}</p>
              <div className="mt-2">
                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getRoleBadgeColor(user?.role || '')}`}>
                  {formatUserRole(user?.role || '')}
                </span>
              </div>
            </div>
          </div>
          <div className="mt-4 grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div>
              <dt className="text-sm font-medium text-gray-500">Member Since</dt>
              <dd className="text-sm text-gray-900">{formatDate(user?.createdAt)}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Last Updated</dt>
              <dd className="text-sm text-gray-900">{formatRelativeTime(user?.updatedAt)}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Last Login</dt>
              <dd className="text-sm text-gray-900">{formatRelativeTime(user?.lastLogin)}</dd>
            </div>
          </div>
        </div>
      </div>

      {/* Stats Grid - Admin Only */}
      {isAdmin && stats && (
        <div>
          <h2 className="text-lg font-medium text-gray-900 mb-4">System Statistics</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="p-5">
                <div className="flex items-center">
                  <div className="flex-shrink-0">
                    <UsersIcon className="h-6 w-6 text-gray-400" />
                  </div>
                  <div className="ml-5 w-0 flex-1">
                    <dl>
                      <dt className="text-sm font-medium text-gray-500 truncate">Total Users</dt>
                      <dd className="text-lg font-medium text-gray-900">{stats.totalUsers}</dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>

            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="p-5">
                <div className="flex items-center">
                  <div className="flex-shrink-0">
                    <ShieldCheckIcon className="h-6 w-6 text-red-400" />
                  </div>
                  <div className="ml-5 w-0 flex-1">
                    <dl>
                      <dt className="text-sm font-medium text-gray-500 truncate">Administrators</dt>
                      <dd className="text-lg font-medium text-gray-900">{stats.adminCount}</dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>

            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="p-5">
                <div className="flex items-center">
                  <div className="flex-shrink-0">
                    <UserGroupIcon className="h-6 w-6 text-yellow-400" />
                  </div>
                  <div className="ml-5 w-0 flex-1">
                    <dl>
                      <dt className="text-sm font-medium text-gray-500 truncate">Moderators</dt>
                      <dd className="text-lg font-medium text-gray-900">{stats.moderatorCount}</dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>

            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="p-5">
                <div className="flex items-center">
                  <div className="flex-shrink-0">
                    <UsersIcon className="h-6 w-6 text-green-400" />
                  </div>
                  <div className="ml-5 w-0 flex-1">
                    <dl>
                      <dt className="text-sm font-medium text-gray-500 truncate">Regular Users</dt>
                      <dd className="text-lg font-medium text-gray-900">{stats.userCount}</dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Quick Actions */}
      <div>
        <h2 className="text-lg font-medium text-gray-900 mb-4">Quick Actions</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {quickActions.map((action) => (
            <Link
              key={action.name}
              to={action.href}
              className="relative group bg-white p-6 focus-within:ring-2 focus-within:ring-inset focus-within:ring-primary-500 rounded-lg shadow hover:shadow-md transition-shadow"
            >
              <div>
                <span className="rounded-lg inline-flex p-3 bg-primary-50 text-primary-600 group-hover:bg-primary-100">
                  <action.icon className="h-6 w-6" />
                </span>
              </div>
              <div className="mt-4">
                <h3 className="text-lg font-medium text-gray-900 group-hover:text-primary-600">
                  {action.name}
                </h3>
                <p className="mt-2 text-sm text-gray-500">{action.description}</p>
              </div>
            </Link>
          ))}
        </div>
      </div>

      {/* Recent Users - Moderator/Admin Only */}
      {isModerator && recentUsers.length > 0 && (
        <div>
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-medium text-gray-900">Recent Users</h2>
            <Link
              to="/users"
              className="text-sm text-primary-600 hover:text-primary-500 flex items-center"
            >
              View all
              <EyeIcon className="ml-1 h-4 w-4" />
            </Link>
          </div>
          <div className="bg-white shadow overflow-hidden sm:rounded-md">
            <ul className="divide-y divide-gray-200">
              {recentUsers.map((recentUser) => (
                <li key={recentUser.id}>
                  <div className="px-4 py-4 sm:px-6">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center">
                        <div className="flex-shrink-0 h-10 w-10">
                          <div className="h-10 w-10 rounded-full bg-gray-300 flex items-center justify-center">
                            <span className="text-sm font-medium text-gray-700">
                              {recentUser.firstName.charAt(0)}{recentUser.lastName.charAt(0)}
                            </span>
                          </div>
                        </div>
                        <div className="ml-4">
                          <div className="flex items-center">
                            <p className="text-sm font-medium text-gray-900">
                              {recentUser.firstName} {recentUser.lastName}
                            </p>
                            <span className={`ml-2 inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getRoleBadgeColor(recentUser.role)}`}>
                              {formatUserRole(recentUser.role)}
                            </span>
                          </div>
                          <p className="text-sm text-gray-500">@{recentUser.username}</p>
                        </div>
                      </div>
                      <div className="flex items-center text-sm text-gray-500">
                        <ClockIcon className="flex-shrink-0 mr-1.5 h-4 w-4" />
                        {formatRelativeTime(recentUser.createdAt)}
                      </div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        </div>
      )}

      {/* Account Status */}
      <div className="bg-white shadow rounded-lg">
        <div className="px-4 py-5 sm:p-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900">Account Status</h3>
          <div className="mt-5 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            <div className="flex items-center">
              <div className={`flex-shrink-0 h-2 w-2 rounded-full ${user?.isEnabled ? 'bg-green-400' : 'bg-red-400'}`} />
              <span className="ml-2 text-sm text-gray-600">
                {user?.isEnabled ? 'Account Enabled' : 'Account Disabled'}
              </span>
            </div>
            <div className="flex items-center">
              <div className={`flex-shrink-0 h-2 w-2 rounded-full ${user?.isAccountNonLocked ? 'bg-green-400' : 'bg-red-400'}`} />
              <span className="ml-2 text-sm text-gray-600">
                {user?.isAccountNonLocked ? 'Not Locked' : 'Account Locked'}
              </span>
            </div>
            <div className="flex items-center">
              <div className={`flex-shrink-0 h-2 w-2 rounded-full ${user?.isAccountNonExpired ? 'bg-green-400' : 'bg-red-400'}`} />
              <span className="ml-2 text-sm text-gray-600">
                {user?.isAccountNonExpired ? 'Not Expired' : 'Account Expired'}
              </span>
            </div>
            <div className="flex items-center">
              <div className={`flex-shrink-0 h-2 w-2 rounded-full ${user?.isCredentialsNonExpired ? 'bg-green-400' : 'bg-red-400'}`} />
              <span className="ml-2 text-sm text-gray-600">
                {user?.isCredentialsNonExpired ? 'Credentials Valid' : 'Credentials Expired'}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
