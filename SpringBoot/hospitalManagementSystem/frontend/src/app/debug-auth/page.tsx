"use client";

import React from "react";
import { useAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import { Shield, CheckCircle, XCircle, User, Lock } from "lucide-react";

export default function DebugAuthPage() {
  const { user, hasRole, hasAnyRole } = useAuth();

  const rolesToCheck = [
    "ADMIN",
    "DOCTOR",
    "PATIENT",
    "NURSE",
    "RECEPTIONIST",
    "ROLE_ADMIN",
    "ROLE_DOCTOR",
    "ROLE_PATIENT",
  ];

  const roleGroups = [
    { name: "Admin Pages", roles: ["ADMIN"] },
    { name: "Doctor Pages", roles: ["DOCTOR"] },
    { name: "Patient Pages", roles: ["PATIENT"] },
    { name: "Settings", roles: ["ADMIN", "DOCTOR", "PATIENT"] },
    { name: "Appointments", roles: ["ADMIN", "DOCTOR"] },
  ];

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <div className="flex items-center space-x-3">
            <Shield className="h-8 w-8 text-blue-600" />
            <div>
              <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
                Authentication Debug
              </h1>
              <p className="text-neutral-600 dark:text-neutral-400 mt-1">
                View your current authentication status and permissions
              </p>
            </div>
          </div>
        </div>

        {/* User Info */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4 flex items-center space-x-2">
            <User className="h-6 w-6" />
            <span>Current User</span>
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                ID
              </p>
              <p className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                {user?.id || "N/A"}
              </p>
            </div>
            <div>
              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                Username
              </p>
              <p className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                {user?.username || "N/A"}
              </p>
            </div>
            <div>
              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                Full Name
              </p>
              <p className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                {user?.fullName || "N/A"}
              </p>
            </div>
            <div>
              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                Email
              </p>
              <p className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                {user?.email || "N/A"}
              </p>
            </div>
          </div>
        </div>

        {/* Raw Roles */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4 flex items-center space-x-2">
            <Lock className="h-6 w-6" />
            <span>Raw Roles Data</span>
          </h2>
          <div className="bg-neutral-50 dark:bg-neutral-900 rounded-lg p-4 font-mono text-sm">
            <pre className="text-neutral-900 dark:text-neutral-100 overflow-x-auto">
              {JSON.stringify(user?.roles, null, 2)}
            </pre>
          </div>
          <div className="mt-4">
            <p className="text-sm text-neutral-600 dark:text-neutral-400">
              Array format:
            </p>
            <div className="flex flex-wrap gap-2 mt-2">
              {user?.roles && Array.isArray(user.roles) ? (
                user.roles.map((role, index) => (
                  <span
                    key={index}
                    className="px-3 py-1 bg-blue-100 dark:bg-blue-900/30 text-blue-800 dark:text-blue-400 rounded-full text-sm font-medium"
                  >
                    {role}
                  </span>
                ))
              ) : (
                <span className="text-neutral-600 dark:text-neutral-400">
                  No roles found or not an array
                </span>
              )}
            </div>
          </div>
        </div>

        {/* Role Checks */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
            Individual Role Checks
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
            {rolesToCheck.map((role) => {
              const hasIt = hasRole(role);
              return (
                <div
                  key={role}
                  className={`flex items-center justify-between p-3 rounded-lg border ${
                    hasIt
                      ? "bg-green-50 dark:bg-green-900/20 border-green-200 dark:border-green-800"
                      : "bg-red-50 dark:bg-red-900/20 border-red-200 dark:border-red-800"
                  }`}
                >
                  <span
                    className={`text-sm font-medium ${
                      hasIt
                        ? "text-green-800 dark:text-green-400"
                        : "text-red-800 dark:text-red-400"
                    }`}
                  >
                    {role}
                  </span>
                  {hasIt ? (
                    <CheckCircle className="h-5 w-5 text-green-600 dark:text-green-400" />
                  ) : (
                    <XCircle className="h-5 w-5 text-red-600 dark:text-red-400" />
                  )}
                </div>
              );
            })}
          </div>
        </div>

        {/* Page Access Checks */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
            Page Access Permissions
          </h2>
          <div className="space-y-3">
            {roleGroups.map((group) => {
              const hasAccess = hasAnyRole(group.roles);
              return (
                <div
                  key={group.name}
                  className={`flex items-center justify-between p-4 rounded-lg border ${
                    hasAccess
                      ? "bg-green-50 dark:bg-green-900/20 border-green-200 dark:border-green-800"
                      : "bg-red-50 dark:bg-red-900/20 border-red-200 dark:border-red-800"
                  }`}
                >
                  <div>
                    <p
                      className={`font-medium ${
                        hasAccess
                          ? "text-green-800 dark:text-green-400"
                          : "text-red-800 dark:text-red-400"
                      }`}
                    >
                      {group.name}
                    </p>
                    <p className="text-sm text-neutral-600 dark:text-neutral-400">
                      Requires: {group.roles.join(" or ")}
                    </p>
                  </div>
                  {hasAccess ? (
                    <CheckCircle className="h-6 w-6 text-green-600 dark:text-green-400" />
                  ) : (
                    <XCircle className="h-6 w-6 text-red-600 dark:text-red-400" />
                  )}
                </div>
              );
            })}
          </div>
        </div>

        {/* Full User Object */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
            Complete User Object
          </h2>
          <div className="bg-neutral-50 dark:bg-neutral-900 rounded-lg p-4 font-mono text-xs">
            <pre className="text-neutral-900 dark:text-neutral-100 overflow-x-auto">
              {JSON.stringify(user, null, 2)}
            </pre>
          </div>
        </div>

        {/* Instructions */}
        <div className="bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-blue-900 dark:text-blue-400 mb-2">
            How to Use This Page
          </h3>
          <ul className="space-y-2 text-sm text-blue-800 dark:text-blue-300">
            <li>• This page shows your current authentication state</li>
            <li>
              • Check if your roles are correct (should include ROLE_DOCTOR for
              doctors)
            </li>
            <li>
              • Green checkmarks = you have that permission / can access that
              page
            </li>
            <li>• Red X marks = you don't have that permission</li>
            <li>
              • If roles look wrong, try logging out and logging back in
            </li>
            <li>
              • Take a screenshot of this page if reporting an access issue
            </li>
          </ul>
        </div>
      </div>
    </DashboardLayout>
  );
}
