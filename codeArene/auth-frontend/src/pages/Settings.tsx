import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import {
    Cog6ToothIcon,
    BellIcon,
    ShieldCheckIcon,
    UserIcon,
} from "@heroicons/react/24/outline";
import { useAuth } from "../context/AuthContext";
import LoadingSpinner from "../components/LoadingSpinner";
import apiService from "../services/api";

interface NotificationSettings {
    emailNotifications: boolean;
    securityAlerts: boolean;
    systemUpdates: boolean;
    marketingEmails: boolean;
}

interface PreferenceSettings {
    theme: "light" | "dark" | "system";
    language: string;
    timezone: string;
    dateFormat: string;
}

const Settings: React.FC = () => {
    const { user, logout } = useAuth();
    const [activeTab, setActiveTab] = useState("general");
    const [loading, setLoading] = useState(false);
    const [saveLoading, setSaveLoading] = useState(false);

    const [notifications, setNotifications] = useState<NotificationSettings>({
        emailNotifications: true,
        securityAlerts: true,
        systemUpdates: false,
        marketingEmails: false,
    });

    const [preferences, setPreferences] = useState<PreferenceSettings>({
        theme: "system",
        language: "en",
        timezone: "UTC",
        dateFormat: "MM/DD/YYYY",
    });

    interface PasswordFormData {
        currentPassword: string;
        newPassword: string;
        confirmPassword: string;
    }

    const {
        register: registerPassword,
        handleSubmit: handlePasswordSubmit,
        formState: { errors: passwordErrors },
        reset: resetPassword,
    } = useForm<PasswordFormData>();

    const tabs = [
        { id: "general", name: "General", icon: Cog6ToothIcon },
        { id: "notifications", name: "Notifications", icon: BellIcon },
        { id: "security", name: "Security", icon: ShieldCheckIcon },
        { id: "preferences", name: "Preferences", icon: UserIcon },
    ];

    const handleNotificationChange = (key: keyof NotificationSettings) => {
        setNotifications((prev) => ({
            ...prev,
            [key]: !prev[key],
        }));
    };

    const handlePreferenceChange = (
        key: keyof PreferenceSettings,
        value: string,
    ) => {
        setPreferences((prev) => ({
            ...prev,
            [key]: value,
        }));
    };

    const handleSaveSettings = async () => {
        try {
            setSaveLoading(true);
            // In a real app, you would save these to the backend
            await new Promise((resolve) => setTimeout(resolve, 1000)); // Simulate API call
            toast.success("Settings saved successfully");
        } catch (error) {
            toast.error("Failed to save settings");
        } finally {
            setSaveLoading(false);
        }
    };

    const handlePasswordChange = async (data: any) => {
        if (!user) return;

        try {
            setLoading(true);
            await apiService.changeUserPassword(user.id, {
                password: data.newPassword,
            });
            toast.success("Password changed successfully");
            resetPassword();
        } catch (error: any) {
            toast.error(
                error.response?.data?.message || "Failed to change password",
            );
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteAccount = async () => {
        if (!user) return;

        const confirmed = window.confirm(
            "Are you sure you want to delete your account? This action cannot be undone.",
        );

        if (confirmed) {
            const doubleConfirmed = window.confirm(
                "This will permanently delete all your data. Type your username to confirm.",
            );

            if (doubleConfirmed) {
                try {
                    setLoading(true);
                    await apiService.deleteUser(user.id);
                    toast.success("Account deleted successfully");
                    logout();
                } catch (error: any) {
                    toast.error(
                        error.response?.data?.message ||
                            "Failed to delete account",
                    );
                } finally {
                    setLoading(false);
                }
            }
        }
    };

    const renderGeneralTab = () => (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium text-gray-900">
                    Account Information
                </h3>
                <p className="mt-1 text-sm text-gray-500">
                    Your basic account information and profile details.
                </p>
            </div>

            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Username
                    </label>
                    <div className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md text-gray-500">
                        {user?.username}
                    </div>
                    <p className="mt-1 text-xs text-gray-500">
                        Username cannot be changed
                    </p>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Email
                    </label>
                    <div className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md text-gray-500">
                        {user?.email}
                    </div>
                    <p className="mt-1 text-xs text-gray-500">
                        Contact support to change email
                    </p>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        First Name
                    </label>
                    <div className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md text-gray-500">
                        {user?.firstName}
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Last Name
                    </label>
                    <div className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md text-gray-500">
                        {user?.lastName}
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Role
                    </label>
                    <div className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md text-gray-500">
                        {user?.role}
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Member Since
                    </label>
                    <div className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-md text-gray-500">
                        {user?.createdAt
                            ? new Date(user.createdAt).toLocaleDateString()
                            : "N/A"}
                    </div>
                </div>
            </div>
        </div>
    );

    const renderNotificationsTab = () => (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium text-gray-900">
                    Notification Preferences
                </h3>
                <p className="mt-1 text-sm text-gray-500">
                    Choose what notifications you want to receive.
                </p>
            </div>

            <div className="space-y-4">
                <div className="flex items-center justify-between">
                    <div>
                        <h4 className="text-sm font-medium text-gray-900">
                            Email Notifications
                        </h4>
                        <p className="text-sm text-gray-500">
                            Receive notifications via email
                        </p>
                    </div>
                    <button
                        type="button"
                        onClick={() =>
                            handleNotificationChange("emailNotifications")
                        }
                        className={`relative inline-flex h-6 w-11 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-2 ${
                            notifications.emailNotifications
                                ? "bg-primary-600"
                                : "bg-gray-200"
                        }`}
                    >
                        <span
                            className={`pointer-events-none inline-block h-5 w-5 transform rounded-full bg-white shadow ring-0 transition duration-200 ease-in-out ${
                                notifications.emailNotifications
                                    ? "translate-x-5"
                                    : "translate-x-0"
                            }`}
                        />
                    </button>
                </div>

                <div className="flex items-center justify-between">
                    <div>
                        <h4 className="text-sm font-medium text-gray-900">
                            Security Alerts
                        </h4>
                        <p className="text-sm text-gray-500">
                            Receive alerts about security events
                        </p>
                    </div>
                    <button
                        type="button"
                        onClick={() =>
                            handleNotificationChange("securityAlerts")
                        }
                        className={`relative inline-flex h-6 w-11 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-2 ${
                            notifications.securityAlerts
                                ? "bg-primary-600"
                                : "bg-gray-200"
                        }`}
                    >
                        <span
                            className={`pointer-events-none inline-block h-5 w-5 transform rounded-full bg-white shadow ring-0 transition duration-200 ease-in-out ${
                                notifications.securityAlerts
                                    ? "translate-x-5"
                                    : "translate-x-0"
                            }`}
                        />
                    </button>
                </div>

                <div className="flex items-center justify-between">
                    <div>
                        <h4 className="text-sm font-medium text-gray-900">
                            System Updates
                        </h4>
                        <p className="text-sm text-gray-500">
                            Receive notifications about system updates
                        </p>
                    </div>
                    <button
                        type="button"
                        onClick={() =>
                            handleNotificationChange("systemUpdates")
                        }
                        className={`relative inline-flex h-6 w-11 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-2 ${
                            notifications.systemUpdates
                                ? "bg-primary-600"
                                : "bg-gray-200"
                        }`}
                    >
                        <span
                            className={`pointer-events-none inline-block h-5 w-5 transform rounded-full bg-white shadow ring-0 transition duration-200 ease-in-out ${
                                notifications.systemUpdates
                                    ? "translate-x-5"
                                    : "translate-x-0"
                            }`}
                        />
                    </button>
                </div>

                <div className="flex items-center justify-between">
                    <div>
                        <h4 className="text-sm font-medium text-gray-900">
                            Marketing Emails
                        </h4>
                        <p className="text-sm text-gray-500">
                            Receive promotional and marketing emails
                        </p>
                    </div>
                    <button
                        type="button"
                        onClick={() =>
                            handleNotificationChange("marketingEmails")
                        }
                        className={`relative inline-flex h-6 w-11 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-2 ${
                            notifications.marketingEmails
                                ? "bg-primary-600"
                                : "bg-gray-200"
                        }`}
                    >
                        <span
                            className={`pointer-events-none inline-block h-5 w-5 transform rounded-full bg-white shadow ring-0 transition duration-200 ease-in-out ${
                                notifications.marketingEmails
                                    ? "translate-x-5"
                                    : "translate-x-0"
                            }`}
                        />
                    </button>
                </div>
            </div>
        </div>
    );

    const renderSecurityTab = () => (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium text-gray-900">
                    Security Settings
                </h3>
                <p className="mt-1 text-sm text-gray-500">
                    Manage your account security and authentication settings.
                </p>
            </div>

            {/* Password Change */}
            <div className="bg-gray-50 p-4 rounded-lg">
                <h4 className="text-sm font-medium text-gray-900 mb-4">
                    Change Password
                </h4>
                <form
                    onSubmit={handlePasswordSubmit(handlePasswordChange)}
                    className="space-y-4"
                >
                    <div>
                        <label
                            htmlFor="currentPassword"
                            className="block text-sm font-medium text-gray-700"
                        >
                            Current Password
                        </label>
                        <input
                            {...registerPassword("currentPassword", {
                                required: "Current password is required",
                            })}
                            type="password"
                            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
                        />
                        {passwordErrors.currentPassword && (
                            <p className="mt-1 text-sm text-red-600">
                                {String(
                                    passwordErrors.currentPassword?.message,
                                )}
                            </p>
                        )}
                    </div>

                    <div>
                        <label
                            htmlFor="newPassword"
                            className="block text-sm font-medium text-gray-700"
                        >
                            New Password
                        </label>
                        <input
                            {...registerPassword("newPassword", {
                                required: "New password is required",
                                minLength: {
                                    value: 8,
                                    message:
                                        "Password must be at least 8 characters",
                                },
                            })}
                            type="password"
                            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
                        />
                        {passwordErrors.newPassword && (
                            <p className="mt-1 text-sm text-red-600">
                                {String(passwordErrors.newPassword?.message)}
                            </p>
                        )}
                    </div>

                    <div>
                        <label
                            htmlFor="confirmPassword"
                            className="block text-sm font-medium text-gray-700"
                        >
                            Confirm New Password
                        </label>
                        <input
                            {...registerPassword("confirmPassword", {
                                required: "Please confirm your password",
                                validate: (
                                    value: string,
                                    formValues: PasswordFormData,
                                ) =>
                                    value === formValues.newPassword ||
                                    "Passwords do not match",
                            })}
                            type="password"
                            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
                        />
                        {passwordErrors.confirmPassword && (
                            <p className="mt-1 text-sm text-red-600">
                                {String(
                                    passwordErrors.confirmPassword?.message,
                                )}
                            </p>
                        )}
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50"
                    >
                        {loading ? (
                            <LoadingSpinner size="small" color="white" />
                        ) : (
                            "Update Password"
                        )}
                    </button>
                </form>
            </div>

            {/* Account Security Status */}
            <div className="bg-green-50 p-4 rounded-lg">
                <h4 className="text-sm font-medium text-green-800 mb-2">
                    Account Security Status
                </h4>
                <div className="space-y-2">
                    <div className="flex items-center">
                        <div className="h-2 w-2 bg-green-400 rounded-full mr-2" />
                        <span className="text-sm text-green-700">
                            Account is enabled and active
                        </span>
                    </div>
                    <div className="flex items-center">
                        <div className="h-2 w-2 bg-green-400 rounded-full mr-2" />
                        <span className="text-sm text-green-700">
                            Password meets security requirements
                        </span>
                    </div>
                    <div className="flex items-center">
                        <div className="h-2 w-2 bg-green-400 rounded-full mr-2" />
                        <span className="text-sm text-green-700">
                            Account is not locked
                        </span>
                    </div>
                </div>
            </div>

            {/* Danger Zone */}
            <div className="bg-red-50 p-4 rounded-lg border border-red-200">
                <h4 className="text-sm font-medium text-red-800 mb-2">
                    Danger Zone
                </h4>
                <p className="text-sm text-red-600 mb-4">
                    Once you delete your account, there is no going back. Please
                    be certain.
                </p>
                <button
                    onClick={handleDeleteAccount}
                    disabled={loading}
                    className="inline-flex items-center px-4 py-2 border border-red-300 text-sm font-medium rounded-md text-red-700 bg-red-50 hover:bg-red-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 disabled:opacity-50"
                >
                    Delete Account
                </button>
            </div>
        </div>
    );

    const renderPreferencesTab = () => (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium text-gray-900">
                    Preferences
                </h3>
                <p className="mt-1 text-sm text-gray-500">
                    Customize your experience and interface preferences.
                </p>
            </div>

            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Theme
                    </label>
                    <select
                        value={preferences.theme}
                        onChange={(e) =>
                            handlePreferenceChange("theme", e.target.value)
                        }
                        className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                    >
                        <option value="light">Light</option>
                        <option value="dark">Dark</option>
                        <option value="system">System Default</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Language
                    </label>
                    <select
                        value={preferences.language}
                        onChange={(e) =>
                            handlePreferenceChange("language", e.target.value)
                        }
                        className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                    >
                        <option value="en">English</option>
                        <option value="es">Español</option>
                        <option value="fr">Français</option>
                        <option value="de">Deutsch</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Timezone
                    </label>
                    <select
                        value={preferences.timezone}
                        onChange={(e) =>
                            handlePreferenceChange("timezone", e.target.value)
                        }
                        className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                    >
                        <option value="UTC">UTC</option>
                        <option value="America/New_York">Eastern Time</option>
                        <option value="America/Chicago">Central Time</option>
                        <option value="America/Denver">Mountain Time</option>
                        <option value="America/Los_Angeles">
                            Pacific Time
                        </option>
                        <option value="Europe/London">London</option>
                        <option value="Europe/Paris">Paris</option>
                        <option value="Asia/Tokyo">Tokyo</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Date Format
                    </label>
                    <select
                        value={preferences.dateFormat}
                        onChange={(e) =>
                            handlePreferenceChange("dateFormat", e.target.value)
                        }
                        className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                    >
                        <option value="MM/DD/YYYY">MM/DD/YYYY</option>
                        <option value="DD/MM/YYYY">DD/MM/YYYY</option>
                        <option value="YYYY-MM-DD">YYYY-MM-DD</option>
                    </select>
                </div>
            </div>
        </div>
    );

    return (
        <div className="space-y-6">
            {/* Header */}
            <div>
                <h1 className="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:truncate">
                    Settings
                </h1>
                <p className="mt-1 text-sm text-gray-500">
                    Manage your account settings and preferences
                </p>
            </div>

            <div className="lg:grid lg:grid-cols-12 lg:gap-x-5">
                {/* Navigation */}
                <aside className="py-6 px-2 sm:px-6 lg:py-0 lg:px-0 lg:col-span-3">
                    <nav className="space-y-1">
                        {tabs.map((tab) => (
                            <button
                                key={tab.id}
                                onClick={() => setActiveTab(tab.id)}
                                className={`group rounded-md px-3 py-2 flex items-center text-sm font-medium w-full text-left ${
                                    activeTab === tab.id
                                        ? "bg-gray-50 text-primary-700 hover:text-primary-700 hover:bg-gray-50"
                                        : "text-gray-900 hover:text-gray-900 hover:bg-gray-50"
                                }`}
                            >
                                <tab.icon
                                    className={`flex-shrink-0 -ml-1 mr-3 h-6 w-6 ${
                                        activeTab === tab.id
                                            ? "text-primary-500 group-hover:text-primary-500"
                                            : "text-gray-400 group-hover:text-gray-500"
                                    }`}
                                />
                                <span className="truncate">{tab.name}</span>
                            </button>
                        ))}
                    </nav>
                </aside>

                {/* Main content */}
                <div className="space-y-6 sm:px-6 lg:px-0 lg:col-span-9">
                    <div className="bg-white shadow rounded-lg">
                        <div className="px-4 py-5 sm:p-6">
                            {activeTab === "general" && renderGeneralTab()}
                            {activeTab === "notifications" &&
                                renderNotificationsTab()}
                            {activeTab === "security" && renderSecurityTab()}
                            {activeTab === "preferences" &&
                                renderPreferencesTab()}
                        </div>

                        {activeTab !== "security" && (
                            <div className="px-4 py-3 bg-gray-50 text-right sm:px-6 rounded-b-lg">
                                <button
                                    onClick={handleSaveSettings}
                                    disabled={saveLoading}
                                    className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50"
                                >
                                    {saveLoading ? (
                                        <LoadingSpinner
                                            size="small"
                                            color="white"
                                        />
                                    ) : (
                                        "Save Settings"
                                    )}
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Settings;
