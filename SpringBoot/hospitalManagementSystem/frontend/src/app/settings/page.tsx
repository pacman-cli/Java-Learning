"use client";

import React, { useState } from "react";
import { withAuth, useAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  User,
  Lock,
  Bell,
  Mail,
  Phone,
  Calendar,
  MapPin,
  Save,
  Shield,
  Eye,
  EyeOff,
  CheckCircle,
  DollarSign,
} from "lucide-react";
import toast from "react-hot-toast";

function SettingsPage() {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState("profile");
  const [loading, setLoading] = useState(false);

  // Profile settings
  const [profileData, setProfileData] = useState({
    fullName: user?.fullName || "",
    email: user?.email || "",
    phone: "",
    dateOfBirth: "",
    address: "",
    bloodGroup: "",
    emergencyContact: "",
    emergencyPhone: "",
  });

  // Security settings
  const [securityData, setSecurityData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [showPasswords, setShowPasswords] = useState({
    current: false,
    new: false,
    confirm: false,
  });

  // Notification settings
  const [notifications, setNotifications] = useState({
    emailNotifications: true,
    smsNotifications: true,
    appointmentReminders: true,
    labResultsNotification: true,
    prescriptionReminders: true,
    billingAlerts: true,
    newsletterSubscription: false,
  });

  const handleProfileUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1000));
      toast.success("Profile updated successfully!");
    } catch {
      toast.error("Failed to update profile");
    } finally {
      setLoading(false);
    }
  };

  const handlePasswordChange = async (e: React.FormEvent) => {
    e.preventDefault();

    if (securityData.newPassword !== securityData.confirmPassword) {
      toast.error("New passwords do not match");
      return;
    }

    if (securityData.newPassword.length < 8) {
      toast.error("Password must be at least 8 characters long");
      return;
    }

    setLoading(true);

    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1000));
      toast.success("Password changed successfully!");
      setSecurityData({
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
      });
    } catch {
      toast.error("Failed to change password");
    } finally {
      setLoading(false);
    }
  };

  const handleNotificationUpdate = async () => {
    setLoading(true);

    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 500));
      toast.success("Notification preferences updated!");
    } catch {
      toast.error("Failed to update notification preferences");
    } finally {
      setLoading(false);
    }
  };

  const tabs = [
    { id: "profile", label: "Profile", icon: User },
    { id: "security", label: "Security", icon: Lock },
    { id: "notifications", label: "Notifications", icon: Bell },
  ];

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
            Settings
          </h1>
          <p className="text-neutral-600 dark:text-neutral-400 mt-1">
            Manage your account settings and preferences
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          {/* Sidebar Tabs */}
          <div className="lg:col-span-1">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-2">
              <nav className="space-y-1">
                {tabs.map((tab) => {
                  const Icon = tab.icon;
                  return (
                    <button
                      key={tab.id}
                      onClick={() => setActiveTab(tab.id)}
                      className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg text-left transition-colors ${
                        activeTab === tab.id
                          ? "bg-primary-100 dark:bg-primary-900/30 text-primary-700 dark:text-primary-400"
                          : "text-neutral-700 dark:text-neutral-300 hover:bg-neutral-100 dark:hover:bg-neutral-700"
                      }`}
                    >
                      <Icon className="h-5 w-5" />
                      <span className="font-medium">{tab.label}</span>
                    </button>
                  );
                })}
              </nav>
            </div>
          </div>

          {/* Content Area */}
          <div className="lg:col-span-3">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow">
              {/* Profile Tab */}
              {activeTab === "profile" && (
                <div className="p-6">
                  <h2 className="text-xl font-bold text-neutral-900 dark:text-neutral-100 mb-6">
                    Profile Information
                  </h2>
                  <form onSubmit={handleProfileUpdate} className="space-y-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <div>
                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                          Full Name
                        </label>
                        <div className="relative">
                          <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                          <input
                            type="text"
                            value={profileData.fullName}
                            onChange={(e) =>
                              setProfileData({
                                ...profileData,
                                fullName: e.target.value,
                              })
                            }
                            className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                          />
                        </div>
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                          Email Address
                        </label>
                        <div className="relative">
                          <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                          <input
                            type="email"
                            value={profileData.email}
                            onChange={(e) =>
                              setProfileData({
                                ...profileData,
                                email: e.target.value,
                              })
                            }
                            className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                          />
                        </div>
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                          Phone Number
                        </label>
                        <div className="relative">
                          <Phone className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                          <input
                            type="tel"
                            value={profileData.phone}
                            onChange={(e) =>
                              setProfileData({
                                ...profileData,
                                phone: e.target.value,
                              })
                            }
                            className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                          />
                        </div>
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                          Date of Birth
                        </label>
                        <div className="relative">
                          <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                          <input
                            type="date"
                            value={profileData.dateOfBirth}
                            onChange={(e) =>
                              setProfileData({
                                ...profileData,
                                dateOfBirth: e.target.value,
                              })
                            }
                            className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                          />
                        </div>
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                          Blood Group
                        </label>
                        <select
                          value={profileData.bloodGroup}
                          onChange={(e) =>
                            setProfileData({
                              ...profileData,
                              bloodGroup: e.target.value,
                            })
                          }
                          className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                        >
                          <option value="">Select Blood Group</option>
                          <option value="A+">A+</option>
                          <option value="A-">A-</option>
                          <option value="B+">B+</option>
                          <option value="B-">B-</option>
                          <option value="AB+">AB+</option>
                          <option value="AB-">AB-</option>
                          <option value="O+">O+</option>
                          <option value="O-">O-</option>
                        </select>
                      </div>

                      <div className="md:col-span-2">
                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                          Address
                        </label>
                        <div className="relative">
                          <MapPin className="absolute left-3 top-3 h-5 w-5 text-neutral-400" />
                          <textarea
                            value={profileData.address}
                            onChange={(e) =>
                              setProfileData({
                                ...profileData,
                                address: e.target.value,
                              })
                            }
                            rows={3}
                            className="w-full pl-10 pr-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                          />
                        </div>
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                          Emergency Contact Name
                        </label>
                        <input
                          type="text"
                          value={profileData.emergencyContact}
                          onChange={(e) =>
                            setProfileData({
                              ...profileData,
                              emergencyContact: e.target.value,
                            })
                          }
                          className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                        />
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                          Emergency Contact Phone
                        </label>
                        <input
                          type="tel"
                          value={profileData.emergencyPhone}
                          onChange={(e) =>
                            setProfileData({
                              ...profileData,
                              emergencyPhone: e.target.value,
                            })
                          }
                          className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                        />
                      </div>
                    </div>

                    <div className="flex justify-end">
                      <button
                        type="submit"
                        disabled={loading}
                        className="flex items-center space-x-2 px-6 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        <Save className="h-5 w-5" />
                        <span>{loading ? "Saving..." : "Save Changes"}</span>
                      </button>
                    </div>
                  </form>
                </div>
              )}

              {/* Security Tab */}
              {activeTab === "security" && (
                <div className="p-6">
                  <h2 className="text-xl font-bold text-neutral-900 dark:text-neutral-100 mb-6">
                    Security Settings
                  </h2>
                  <form onSubmit={handlePasswordChange} className="space-y-6">
                    <div className="bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg p-4">
                      <div className="flex items-start space-x-3">
                        <Shield className="h-5 w-5 text-blue-600 dark:text-blue-400 mt-0.5" />
                        <div>
                          <h3 className="font-semibold text-blue-900 dark:text-blue-200">
                            Password Requirements
                          </h3>
                          <ul className="text-sm text-blue-800 dark:text-blue-300 mt-2 space-y-1">
                            <li>• At least 8 characters long</li>
                            <li>• Include uppercase and lowercase letters</li>
                            <li>• Include at least one number</li>
                            <li>• Include at least one special character</li>
                          </ul>
                        </div>
                      </div>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                        Current Password
                      </label>
                      <div className="relative">
                        <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                        <input
                          type={showPasswords.current ? "text" : "password"}
                          value={securityData.currentPassword}
                          onChange={(e) =>
                            setSecurityData({
                              ...securityData,
                              currentPassword: e.target.value,
                            })
                          }
                          className="w-full pl-10 pr-12 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                        />
                        <button
                          type="button"
                          onClick={() =>
                            setShowPasswords({
                              ...showPasswords,
                              current: !showPasswords.current,
                            })
                          }
                          className="absolute right-3 top-1/2 transform -translate-y-1/2 text-neutral-400 hover:text-neutral-600"
                        >
                          {showPasswords.current ? (
                            <EyeOff className="h-5 w-5" />
                          ) : (
                            <Eye className="h-5 w-5" />
                          )}
                        </button>
                      </div>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                        New Password
                      </label>
                      <div className="relative">
                        <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                        <input
                          type={showPasswords.new ? "text" : "password"}
                          value={securityData.newPassword}
                          onChange={(e) =>
                            setSecurityData({
                              ...securityData,
                              newPassword: e.target.value,
                            })
                          }
                          className="w-full pl-10 pr-12 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                        />
                        <button
                          type="button"
                          onClick={() =>
                            setShowPasswords({
                              ...showPasswords,
                              new: !showPasswords.new,
                            })
                          }
                          className="absolute right-3 top-1/2 transform -translate-y-1/2 text-neutral-400 hover:text-neutral-600"
                        >
                          {showPasswords.new ? (
                            <EyeOff className="h-5 w-5" />
                          ) : (
                            <Eye className="h-5 w-5" />
                          )}
                        </button>
                      </div>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                        Confirm New Password
                      </label>
                      <div className="relative">
                        <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-neutral-400" />
                        <input
                          type={showPasswords.confirm ? "text" : "password"}
                          value={securityData.confirmPassword}
                          onChange={(e) =>
                            setSecurityData({
                              ...securityData,
                              confirmPassword: e.target.value,
                            })
                          }
                          className="w-full pl-10 pr-12 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                        />
                        <button
                          type="button"
                          onClick={() =>
                            setShowPasswords({
                              ...showPasswords,
                              confirm: !showPasswords.confirm,
                            })
                          }
                          className="absolute right-3 top-1/2 transform -translate-y-1/2 text-neutral-400 hover:text-neutral-600"
                        >
                          {showPasswords.confirm ? (
                            <EyeOff className="h-5 w-5" />
                          ) : (
                            <Eye className="h-5 w-5" />
                          )}
                        </button>
                      </div>
                    </div>

                    <div className="flex justify-end">
                      <button
                        type="submit"
                        disabled={loading}
                        className="flex items-center space-x-2 px-6 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        <Shield className="h-5 w-5" />
                        <span>
                          {loading ? "Updating..." : "Change Password"}
                        </span>
                      </button>
                    </div>
                  </form>
                </div>
              )}

              {/* Notifications Tab */}
              {activeTab === "notifications" && (
                <div className="p-6">
                  <h2 className="text-xl font-bold text-neutral-900 dark:text-neutral-100 mb-6">
                    Notification Preferences
                  </h2>
                  <div className="space-y-6">
                    <div>
                      <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                        Communication Channels
                      </h3>
                      <div className="space-y-4">
                        <label className="flex items-center justify-between p-4 border border-neutral-200 dark:border-neutral-700 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700/50 cursor-pointer">
                          <div className="flex items-center space-x-3">
                            <Mail className="h-5 w-5 text-neutral-600 dark:text-neutral-400" />
                            <div>
                              <p className="font-medium text-neutral-900 dark:text-neutral-100">
                                Email Notifications
                              </p>
                              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                                Receive updates via email
                              </p>
                            </div>
                          </div>
                          <input
                            type="checkbox"
                            checked={notifications.emailNotifications}
                            onChange={(e) =>
                              setNotifications({
                                ...notifications,
                                emailNotifications: e.target.checked,
                              })
                            }
                            className="w-5 h-5 text-primary-600 rounded focus:ring-2 focus:ring-primary-500"
                          />
                        </label>

                        <label className="flex items-center justify-between p-4 border border-neutral-200 dark:border-neutral-700 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700/50 cursor-pointer">
                          <div className="flex items-center space-x-3">
                            <Phone className="h-5 w-5 text-neutral-600 dark:text-neutral-400" />
                            <div>
                              <p className="font-medium text-neutral-900 dark:text-neutral-100">
                                SMS Notifications
                              </p>
                              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                                Receive updates via text message
                              </p>
                            </div>
                          </div>
                          <input
                            type="checkbox"
                            checked={notifications.smsNotifications}
                            onChange={(e) =>
                              setNotifications({
                                ...notifications,
                                smsNotifications: e.target.checked,
                              })
                            }
                            className="w-5 h-5 text-primary-600 rounded focus:ring-2 focus:ring-primary-500"
                          />
                        </label>
                      </div>
                    </div>

                    <div className="border-t border-neutral-200 dark:border-neutral-700 pt-6">
                      <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
                        Notification Types
                      </h3>
                      <div className="space-y-4">
                        <label className="flex items-center justify-between p-4 border border-neutral-200 dark:border-neutral-700 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700/50 cursor-pointer">
                          <div className="flex items-center space-x-3">
                            <Calendar className="h-5 w-5 text-neutral-600 dark:text-neutral-400" />
                            <div>
                              <p className="font-medium text-neutral-900 dark:text-neutral-100">
                                Appointment Reminders
                              </p>
                              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                                Get reminded about upcoming appointments
                              </p>
                            </div>
                          </div>
                          <input
                            type="checkbox"
                            checked={notifications.appointmentReminders}
                            onChange={(e) =>
                              setNotifications({
                                ...notifications,
                                appointmentReminders: e.target.checked,
                              })
                            }
                            className="w-5 h-5 text-primary-600 rounded focus:ring-2 focus:ring-primary-500"
                          />
                        </label>

                        <label className="flex items-center justify-between p-4 border border-neutral-200 dark:border-neutral-700 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700/50 cursor-pointer">
                          <div className="flex items-center space-x-3">
                            <CheckCircle className="h-5 w-5 text-neutral-600 dark:text-neutral-400" />
                            <div>
                              <p className="font-medium text-neutral-900 dark:text-neutral-100">
                                Lab Results Notification
                              </p>
                              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                                Get notified when lab results are ready
                              </p>
                            </div>
                          </div>
                          <input
                            type="checkbox"
                            checked={notifications.labResultsNotification}
                            onChange={(e) =>
                              setNotifications({
                                ...notifications,
                                labResultsNotification: e.target.checked,
                              })
                            }
                            className="w-5 h-5 text-primary-600 rounded focus:ring-2 focus:ring-primary-500"
                          />
                        </label>

                        <label className="flex items-center justify-between p-4 border border-neutral-200 dark:border-neutral-700 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700/50 cursor-pointer">
                          <div className="flex items-center space-x-3">
                            <Bell className="h-5 w-5 text-neutral-600 dark:text-neutral-400" />
                            <div>
                              <p className="font-medium text-neutral-900 dark:text-neutral-100">
                                Prescription Reminders
                              </p>
                              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                                Reminders to take your medications
                              </p>
                            </div>
                          </div>
                          <input
                            type="checkbox"
                            checked={notifications.prescriptionReminders}
                            onChange={(e) =>
                              setNotifications({
                                ...notifications,
                                prescriptionReminders: e.target.checked,
                              })
                            }
                            className="w-5 h-5 text-primary-600 rounded focus:ring-2 focus:ring-primary-500"
                          />
                        </label>

                        <label className="flex items-center justify-between p-4 border border-neutral-200 dark:border-neutral-700 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700/50 cursor-pointer">
                          <div className="flex items-center space-x-3">
                            <DollarSign className="h-5 w-5 text-neutral-600 dark:text-neutral-400" />
                            <div>
                              <p className="font-medium text-neutral-900 dark:text-neutral-100">
                                Billing Alerts
                              </p>
                              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                                Notifications about bills and payments
                              </p>
                            </div>
                          </div>
                          <input
                            type="checkbox"
                            checked={notifications.billingAlerts}
                            onChange={(e) =>
                              setNotifications({
                                ...notifications,
                                billingAlerts: e.target.checked,
                              })
                            }
                            className="w-5 h-5 text-primary-600 rounded focus:ring-2 focus:ring-primary-500"
                          />
                        </label>

                        <label className="flex items-center justify-between p-4 border border-neutral-200 dark:border-neutral-700 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700/50 cursor-pointer">
                          <div className="flex items-center space-x-3">
                            <Mail className="h-5 w-5 text-neutral-600 dark:text-neutral-400" />
                            <div>
                              <p className="font-medium text-neutral-900 dark:text-neutral-100">
                                Newsletter Subscription
                              </p>
                              <p className="text-sm text-neutral-600 dark:text-neutral-400">
                                Health tips and hospital updates
                              </p>
                            </div>
                          </div>
                          <input
                            type="checkbox"
                            checked={notifications.newsletterSubscription}
                            onChange={(e) =>
                              setNotifications({
                                ...notifications,
                                newsletterSubscription: e.target.checked,
                              })
                            }
                            className="w-5 h-5 text-primary-600 rounded focus:ring-2 focus:ring-primary-500"
                          />
                        </label>
                      </div>
                    </div>

                    <div className="flex justify-end pt-4">
                      <button
                        onClick={handleNotificationUpdate}
                        disabled={loading}
                        className="flex items-center space-x-2 px-6 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        <Save className="h-5 w-5" />
                        <span>
                          {loading ? "Saving..." : "Save Preferences"}
                        </span>
                      </button>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

export default withAuth(SettingsPage, ["ADMIN", "DOCTOR", "PATIENT"]);
