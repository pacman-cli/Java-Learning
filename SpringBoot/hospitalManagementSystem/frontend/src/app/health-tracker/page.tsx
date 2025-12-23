"use client";

import React, { useState } from "react";
import { withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  Heart,
  Activity,
  TrendingUp,
  Droplet,
  Wind,
  Zap,
  Plus,
  Calendar,
  Download,
  AlertCircle,
} from "lucide-react";
import toast from "react-hot-toast";

interface VitalReading {
  id: number;
  type: string;
  value: string;
  date: string;
  note?: string;
}

function HealthTrackerPage() {
  const [showAddModal, setShowAddModal] = useState(false);
  const [selectedVitalType, setSelectedVitalType] = useState("");
  const [vitalValue, setVitalValue] = useState("");
  const [vitalNote, setVitalNote] = useState("");
  const [readings, setReadings] = useState<VitalReading[]>([
    {
      id: 1,
      type: "Heart Rate",
      value: "72 bpm",
      date: "2024-12-01T08:00:00.000Z",
      note: "Morning reading",
    },
    {
      id: 2,
      type: "Blood Pressure",
      value: "120/80 mmHg",
      date: "2024-11-30T08:00:00.000Z",
      note: "After exercise",
    },
    {
      id: 3,
      type: "Blood Sugar",
      value: "95 mg/dL",
      date: "2024-11-29T08:00:00.000Z",
      note: "Fasting",
    },
  ]);

  const vitalTypes = [
    { name: "Heart Rate", unit: "bpm", icon: Heart, color: "red" },
    { name: "Blood Pressure", unit: "mmHg", icon: Activity, color: "blue" },
    { name: "Blood Sugar", unit: "mg/dL", icon: Droplet, color: "purple" },
    { name: "Temperature", unit: "°F", icon: TrendingUp, color: "orange" },
    { name: "Weight", unit: "kg", icon: Zap, color: "green" },
    { name: "Oxygen Level", unit: "%", icon: Wind, color: "cyan" },
  ];

  const handleAddReading = () => {
    if (!selectedVitalType || !vitalValue) {
      toast.error("Please fill in all required fields");
      return;
    }

    const newReading: VitalReading = {
      id: readings.length + 1,
      type: selectedVitalType,
      value: vitalValue,
      date: new Date().toISOString(),
      note: vitalNote,
    };

    setReadings([newReading, ...readings]);
    setShowAddModal(false);
    setSelectedVitalType("");
    setVitalValue("");
    setVitalNote("");
    toast.success("Vital reading added successfully!");
  };

  const getLatestReading = (type: string) => {
    return readings.find((r) => r.type === type)?.value || "--";
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const getVitalColor = (type: string) => {
    const vital = vitalTypes.find((v) => v.name === type);
    return vital?.color || "gray";
  };

  const getColorClasses = (color: string) => {
    const colorMap: { [key: string]: { bg: string; text: string } } = {
      red: {
        bg: "bg-red-100 dark:bg-red-900",
        text: "text-red-600 dark:text-red-400",
      },
      blue: {
        bg: "bg-blue-100 dark:bg-blue-900",
        text: "text-blue-600 dark:text-blue-400",
      },
      purple: {
        bg: "bg-purple-100 dark:bg-purple-900",
        text: "text-purple-600 dark:text-purple-400",
      },
      orange: {
        bg: "bg-orange-100 dark:bg-orange-900",
        text: "text-orange-600 dark:text-orange-400",
      },
      green: {
        bg: "bg-green-100 dark:bg-green-900",
        text: "text-green-600 dark:text-green-400",
      },
      cyan: {
        bg: "bg-cyan-100 dark:bg-cyan-900",
        text: "text-cyan-600 dark:text-cyan-400",
      },
    };
    return (
      colorMap[color] || {
        bg: "bg-gray-100 dark:bg-gray-900",
        text: "text-gray-600 dark:text-gray-400",
      }
    );
  };

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex justify-between items-start">
          <div>
            <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100">
              Health Tracker
            </h1>
            <p className="text-neutral-600 dark:text-neutral-400 mt-1">
              Monitor your health metrics and wellness data
            </p>
          </div>
          <button
            onClick={() => setShowAddModal(true)}
            className="flex items-center space-x-2 px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg"
          >
            <Plus className="h-5 w-5" />
            <span>Add Reading</span>
          </button>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {vitalTypes.slice(0, 3).map((vital) => {
            const Icon = vital.icon;
            const colors = getColorClasses(vital.color);
            return (
              <div
                key={vital.name}
                className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6"
              >
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-neutral-600 dark:text-neutral-400">
                      {vital.name}
                    </p>
                    <p className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 mt-2">
                      {getLatestReading(vital.name)}
                    </p>
                  </div>
                  <div className={`${colors.bg} p-3 rounded-lg`}>
                    <Icon className={`h-6 w-6 ${colors.text}`} />
                  </div>
                </div>
              </div>
            );
          })}
        </div>

        {/* Quick Actions */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
            Track Your Vitals
          </h2>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
            {vitalTypes.map((vital) => {
              const Icon = vital.icon;
              const colors = getColorClasses(vital.color);
              return (
                <button
                  key={vital.name}
                  onClick={() => {
                    setSelectedVitalType(vital.name);
                    setShowAddModal(true);
                  }}
                  className="flex flex-col items-center p-4 border border-neutral-200 dark:border-neutral-700 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700/50 transition-colors"
                >
                  <div className={`${colors.bg} p-3 rounded-lg mb-2`}>
                    <Icon className={`h-6 w-6 ${colors.text}`} />
                  </div>
                  <span className="text-sm font-medium text-neutral-900 dark:text-neutral-100 text-center">
                    {vital.name}
                  </span>
                </button>
              );
            })}
          </div>
        </div>

        {/* Recent Readings */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow">
          <div className="p-6 border-b border-neutral-200 dark:border-neutral-700">
            <div className="flex justify-between items-center">
              <h2 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100">
                Recent Readings
              </h2>
              <button
                onClick={() => toast.success("Export feature coming soon!")}
                className="flex items-center space-x-2 text-sm text-primary-600 hover:text-primary-700 dark:text-primary-400"
              >
                <Download className="h-4 w-4" />
                <span>Export</span>
              </button>
            </div>
          </div>

          {readings.length === 0 ? (
            <div className="p-12 text-center">
              <Activity className="h-12 w-12 text-neutral-400 mx-auto mb-4" />
              <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                No Readings Yet
              </h3>
              <p className="text-neutral-600 dark:text-neutral-400">
                Start tracking your health by adding your first vital reading
              </p>
            </div>
          ) : (
            <div className="p-6">
              <div className="space-y-4">
                {readings.map((reading) => {
                  const vital = vitalTypes.find((v) => v.name === reading.type);
                  const Icon = vital?.icon || Activity;
                  const colors = getColorClasses(getVitalColor(reading.type));
                  return (
                    <div
                      key={reading.id}
                      className="flex items-center justify-between p-4 border border-neutral-200 dark:border-neutral-700 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700/50"
                    >
                      <div className="flex items-center space-x-4">
                        <div className={`${colors.bg} p-3 rounded-lg`}>
                          <Icon className={`h-5 w-5 ${colors.text}`} />
                        </div>
                        <div>
                          <p className="font-semibold text-neutral-900 dark:text-neutral-100">
                            {reading.type}
                          </p>
                          <p className="text-sm text-neutral-600 dark:text-neutral-400 flex items-center space-x-2">
                            <Calendar className="h-4 w-4" />
                            <span>{formatDate(reading.date)}</span>
                          </p>
                          {reading.note && (
                            <p className="text-xs text-neutral-500 dark:text-neutral-500 mt-1">
                              Note: {reading.note}
                            </p>
                          )}
                        </div>
                      </div>
                      <div className="text-right">
                        <p className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                          {reading.value}
                        </p>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          )}
        </div>

        {/* Health Tips */}
        <div className="bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg p-6">
          <div className="flex items-start space-x-3">
            <AlertCircle className="h-6 w-6 text-blue-600 dark:text-blue-400 mt-1" />
            <div>
              <h3 className="text-lg font-semibold text-blue-900 dark:text-blue-200 mb-2">
                Health Monitoring Tips
              </h3>
              <ul className="text-sm text-blue-800 dark:text-blue-300 space-y-2">
                <li>
                  • Measure vitals at the same time each day for consistency
                </li>
                <li>• Keep your devices calibrated and properly maintained</li>
                <li>• Note any unusual symptoms or activities in the notes</li>
                <li>
                  • Share your readings with your doctor during appointments
                </li>
                <li>• Set reminders to track your vitals regularly</li>
              </ul>
            </div>
          </div>
        </div>

        {/* Add Reading Modal */}
        {showAddModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-md w-full">
              <div className="p-6">
                <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100 mb-6">
                  Add Vital Reading
                </h2>

                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                      Vital Type
                    </label>
                    <select
                      value={selectedVitalType}
                      onChange={(e) => setSelectedVitalType(e.target.value)}
                      className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                    >
                      <option value="">Select vital type</option>
                      {vitalTypes.map((vital) => (
                        <option key={vital.name} value={vital.name}>
                          {vital.name} ({vital.unit})
                        </option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                      Value
                    </label>
                    <input
                      type="text"
                      value={vitalValue}
                      onChange={(e) => setVitalValue(e.target.value)}
                      placeholder={
                        selectedVitalType
                          ? `Enter ${vitalTypes.find((v) => v.name === selectedVitalType)?.unit}`
                          : "Enter value"
                      }
                      className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                      Note (Optional)
                    </label>
                    <textarea
                      value={vitalNote}
                      onChange={(e) => setVitalNote(e.target.value)}
                      placeholder="Add any relevant notes..."
                      rows={3}
                      className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-700 text-neutral-900 dark:text-neutral-100"
                    />
                  </div>
                </div>

                <div className="flex justify-end space-x-3 mt-6">
                  <button
                    onClick={() => {
                      setShowAddModal(false);
                      setSelectedVitalType("");
                      setVitalValue("");
                      setVitalNote("");
                    }}
                    className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg text-neutral-700 dark:text-neutral-300 hover:bg-neutral-50 dark:hover:bg-neutral-700"
                  >
                    Cancel
                  </button>
                  <button
                    onClick={handleAddReading}
                    className="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg"
                  >
                    Add Reading
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}

export default withAuth(HealthTrackerPage, ["PATIENT"]);
