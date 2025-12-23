"use client";

import React, { useState, useEffect } from "react";
import { useAuth, withAuth } from "@/providers/AuthProvider";
import DashboardLayout from "@/components/layout/DashboardLayout";
import {
  Calendar,
  Clock,
  Plus,
  X,
  Save,
  Trash2,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";
import toast from "react-hot-toast";

interface TimeSlot {
  id: string;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
  isAvailable: boolean;
  maxAppointments: number;
}

interface BlockedDate {
  id: string;
  date: string;
  reason: string;
}

const daysOfWeek = [
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday",
  "Sunday",
];

const timeOptions = Array.from({ length: 48 }, (_, i) => {
  const hour = Math.floor(i / 2);
  const minute = i % 2 === 0 ? "00" : "30";
  return `${hour.toString().padStart(2, "0")}:${minute}`;
});

function MySchedulePage() {
  const {} = useAuth();
  const [timeSlots, setTimeSlots] = useState<TimeSlot[]>([]);
  const [blockedDates, setBlockedDates] = useState<BlockedDate[]>([]);

  const [showAddSlotModal, setShowAddSlotModal] = useState(false);
  const [showBlockDateModal, setShowBlockDateModal] = useState(false);
  const [currentMonth, setCurrentMonth] = useState(new Date());

  const [slotForm, setSlotForm] = useState({
    dayOfWeek: "",
    startTime: "",
    endTime: "",
    maxAppointments: "4",
  });

  const [blockForm, setBlockForm] = useState({
    date: "",
    reason: "",
  });

  const loadSchedule = () => {
    try {
      const savedSlots = localStorage.getItem("doctorSchedule");
      const savedBlocked = localStorage.getItem("blockedDates");

      if (savedSlots) {
        setTimeSlots(JSON.parse(savedSlots));
      } else {
        // Default schedule
        const defaultSlots: TimeSlot[] = [
          {
            id: "1",
            dayOfWeek: "Monday",
            startTime: "09:00",
            endTime: "12:00",
            isAvailable: true,
            maxAppointments: 6,
          },
          {
            id: "2",
            dayOfWeek: "Monday",
            startTime: "14:00",
            endTime: "17:00",
            isAvailable: true,
            maxAppointments: 6,
          },
          {
            id: "3",
            dayOfWeek: "Wednesday",
            startTime: "09:00",
            endTime: "12:00",
            isAvailable: true,
            maxAppointments: 6,
          },
          {
            id: "4",
            dayOfWeek: "Wednesday",
            startTime: "14:00",
            endTime: "17:00",
            isAvailable: true,
            maxAppointments: 6,
          },
          {
            id: "5",
            dayOfWeek: "Friday",
            startTime: "09:00",
            endTime: "13:00",
            isAvailable: true,
            maxAppointments: 8,
          },
        ];
        setTimeSlots(defaultSlots);
        localStorage.setItem("doctorSchedule", JSON.stringify(defaultSlots));
      }

      if (savedBlocked) {
        setBlockedDates(JSON.parse(savedBlocked));
      }
    } catch (error) {
      console.error("Failed to load schedule:", error);
    }
  };

  // Load schedule from localStorage (mock data)
  useEffect(() => {
    loadSchedule();
  }, []);

  const saveSchedule = (newSlots: TimeSlot[]) => {
    localStorage.setItem("doctorSchedule", JSON.stringify(newSlots));
    setTimeSlots(newSlots);
  };

  const saveBlockedDates = (newBlocked: BlockedDate[]) => {
    localStorage.setItem("blockedDates", JSON.stringify(newBlocked));
    setBlockedDates(newBlocked);
  };

  const handleAddTimeSlot = (e: React.FormEvent) => {
    e.preventDefault();
    if (
      !slotForm.dayOfWeek ||
      !slotForm.startTime ||
      !slotForm.endTime ||
      !slotForm.maxAppointments
    ) {
      toast.error("Please fill in all fields");
      return;
    }

    if (slotForm.startTime >= slotForm.endTime) {
      toast.error("End time must be after start time");
      return;
    }

    const newSlot: TimeSlot = {
      id: `slot-${Math.random().toString(36).substr(2, 9)}`,
      dayOfWeek: slotForm.dayOfWeek,
      startTime: slotForm.startTime,
      endTime: slotForm.endTime,
      isAvailable: true,
      maxAppointments: parseInt(slotForm.maxAppointments),
    };

    const updatedSlots = [...timeSlots, newSlot];
    saveSchedule(updatedSlots);
    toast.success("Time slot added successfully");
    setShowAddSlotModal(false);
    resetSlotForm();
  };

  const handleDeleteSlot = (id: string) => {
    if (!confirm("Are you sure you want to delete this time slot?")) return;
    const updatedSlots = timeSlots.filter((slot) => slot.id !== id);
    saveSchedule(updatedSlots);
    toast.success("Time slot deleted successfully");
  };

  const handleToggleAvailability = (id: string) => {
    const updatedSlots = timeSlots.map((slot) =>
      slot.id === id ? { ...slot, isAvailable: !slot.isAvailable } : slot,
    );
    saveSchedule(updatedSlots);
    toast.success("Availability updated");
  };

  const handleBlockDate = (e: React.FormEvent) => {
    e.preventDefault();
    if (!blockForm.date || !blockForm.reason) {
      toast.error("Please fill in all fields");
      return;
    }

    const newBlock: BlockedDate = {
      id: `block-${Math.random().toString(36).substr(2, 9)}`,
      date: blockForm.date,
      reason: blockForm.reason,
    };

    const updatedBlocked = [...blockedDates, newBlock];
    saveBlockedDates(updatedBlocked);
    toast.success("Date blocked successfully");
    setShowBlockDateModal(false);
    resetBlockForm();
  };

  const handleUnblockDate = (id: string) => {
    if (!confirm("Are you sure you want to unblock this date?")) return;
    const updatedBlocked = blockedDates.filter((block) => block.id !== id);
    saveBlockedDates(updatedBlocked);
    toast.success("Date unblocked successfully");
  };

  const resetSlotForm = () => {
    setSlotForm({
      dayOfWeek: "",
      startTime: "",
      endTime: "",
      maxAppointments: "4",
    });
  };

  const resetBlockForm = () => {
    setBlockForm({
      date: "",
      reason: "",
    });
  };

  // Calendar helpers
  const getDaysInMonth = (date: Date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDayOfWeek = firstDay.getDay();

    return { daysInMonth, startingDayOfWeek };
  };

  const isDateBlocked = (date: Date) => {
    const dateStr = date.toISOString().split("T")[0];
    return blockedDates.some((block) => block.date === dateStr);
  };

  const isDayAvailable = (dayName: string) => {
    return timeSlots.some(
      (slot) => slot.dayOfWeek === dayName && slot.isAvailable,
    );
  };

  const { daysInMonth, startingDayOfWeek } = getDaysInMonth(currentMonth);

  const calendarDays = [];
  for (let i = 0; i < startingDayOfWeek; i++) {
    calendarDays.push(null);
  }
  for (let i = 1; i <= daysInMonth; i++) {
    calendarDays.push(i);
  }

  const getSlotsForDay = (dayOfWeek: string) => {
    return timeSlots.filter((slot) => slot.dayOfWeek === dayOfWeek);
  };

  const getDayName = (date: Date) => {
    return daysOfWeek[date.getDay() === 0 ? 6 : date.getDay() - 1];
  };

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-neutral-900 dark:text-neutral-100 flex items-center">
              <Calendar className="h-8 w-8 mr-3 text-teal-600" />
              My Schedule
            </h1>
            <p className="mt-2 text-neutral-600 dark:text-neutral-400">
              Manage your working hours and availability
            </p>
          </div>
          <div className="flex space-x-3">
            <button
              onClick={() => setShowBlockDateModal(true)}
              className="btn-secondary btn-sm flex items-center space-x-2"
            >
              <X className="h-4 w-4" />
              <span>Block Date</span>
            </button>
            <button
              onClick={() => setShowAddSlotModal(true)}
              className="btn-primary btn-sm flex items-center space-x-2"
            >
              <Plus className="h-4 w-4" />
              <span>Add Time Slot</span>
            </button>
          </div>
        </div>

        {/* Calendar View */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100">
              {currentMonth.toLocaleDateString("en-US", {
                month: "long",
                year: "numeric",
              })}
            </h2>
            <div className="flex space-x-2">
              <button
                onClick={() =>
                  setCurrentMonth(
                    new Date(
                      currentMonth.getFullYear(),
                      currentMonth.getMonth() - 1,
                    ),
                  )
                }
                className="p-2 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
              >
                <ChevronLeft className="h-5 w-5" />
              </button>
              <button
                onClick={() => setCurrentMonth(new Date())}
                className="px-4 py-2 text-sm font-medium hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
              >
                Today
              </button>
              <button
                onClick={() =>
                  setCurrentMonth(
                    new Date(
                      currentMonth.getFullYear(),
                      currentMonth.getMonth() + 1,
                    ),
                  )
                }
                className="p-2 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
              >
                <ChevronRight className="h-5 w-5" />
              </button>
            </div>
          </div>

          {/* Calendar Grid */}
          <div className="grid grid-cols-7 gap-2">
            {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map((day) => (
              <div
                key={day}
                className="text-center text-sm font-semibold text-neutral-600 dark:text-neutral-400 py-2"
              >
                {day}
              </div>
            ))}
            {calendarDays.map((day, index) => {
              if (day === null) {
                return <div key={`empty-${index}`} className="p-2" />;
              }

              const date = new Date(
                currentMonth.getFullYear(),
                currentMonth.getMonth(),
                day,
              );
              const isBlocked = isDateBlocked(date);
              const dayName = getDayName(date);
              const isAvailable = isDayAvailable(dayName);
              const isToday = date.toDateString() === new Date().toDateString();

              return (
                <div
                  key={day}
                  className={`p-2 min-h-20 border rounded-lg cursor-pointer transition ${
                    isToday
                      ? "border-teal-500 bg-teal-50 dark:bg-teal-900/20"
                      : isBlocked
                        ? "border-red-300 bg-red-50 dark:bg-red-900/20"
                        : isAvailable
                          ? "border-green-300 bg-green-50 dark:bg-green-900/20"
                          : "border-neutral-200 dark:border-neutral-700"
                  } hover:shadow-md`}
                >
                  <div className="flex flex-col h-full">
                    <span
                      className={`text-sm font-medium ${
                        isToday
                          ? "text-teal-700 dark:text-teal-300"
                          : "text-neutral-900 dark:text-neutral-100"
                      }`}
                    >
                      {day}
                    </span>
                    {isBlocked && (
                      <span className="text-xs text-red-600 dark:text-red-400 mt-1">
                        Blocked
                      </span>
                    )}
                    {!isBlocked && isAvailable && (
                      <span className="text-xs text-green-600 dark:text-green-400 mt-1">
                        Available
                      </span>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Weekly Schedule */}
        <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-6">
            Weekly Schedule
          </h2>
          <div className="space-y-4">
            {daysOfWeek.map((day) => {
              const slots = getSlotsForDay(day);
              return (
                <div
                  key={day}
                  className="border border-neutral-200 dark:border-neutral-700 rounded-lg p-4"
                >
                  <h3 className="text-lg font-semibold text-neutral-900 dark:text-neutral-100 mb-3">
                    {day}
                  </h3>
                  {slots.length === 0 ? (
                    <p className="text-neutral-500 dark:text-neutral-400 text-sm">
                      No time slots scheduled
                    </p>
                  ) : (
                    <div className="space-y-2">
                      {slots.map((slot) => (
                        <div
                          key={slot.id}
                          className={`flex items-center justify-between p-3 rounded-lg ${
                            slot.isAvailable
                              ? "bg-green-50 dark:bg-green-900/20"
                              : "bg-neutral-100 dark:bg-neutral-700"
                          }`}
                        >
                          <div className="flex items-center space-x-4">
                            <Clock className="h-5 w-5 text-teal-600" />
                            <span className="font-medium text-neutral-900 dark:text-neutral-100">
                              {slot.startTime} - {slot.endTime}
                            </span>
                            <span className="text-sm text-neutral-600 dark:text-neutral-400">
                              Max: {slot.maxAppointments} appointments
                            </span>
                          </div>
                          <div className="flex items-center space-x-2">
                            <button
                              onClick={() => handleToggleAvailability(slot.id)}
                              className={`px-3 py-1 rounded-lg text-sm font-medium ${
                                slot.isAvailable
                                  ? "bg-green-600 text-white hover:bg-green-700"
                                  : "bg-neutral-400 text-white hover:bg-neutral-500"
                              }`}
                            >
                              {slot.isAvailable ? "Available" : "Unavailable"}
                            </button>
                            <button
                              onClick={() => handleDeleteSlot(slot.id)}
                              className="p-2 text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition"
                              title="Delete"
                            >
                              <Trash2 className="h-4 w-4" />
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </div>

        {/* Blocked Dates */}
        {blockedDates.length > 0 && (
          <div className="bg-white dark:bg-neutral-800 rounded-lg shadow p-6">
            <h2 className="text-xl font-semibold text-neutral-900 dark:text-neutral-100 mb-4">
              Blocked Dates
            </h2>
            <div className="space-y-2">
              {blockedDates.map((block) => (
                <div
                  key={block.id}
                  className="flex items-center justify-between p-4 bg-red-50 dark:bg-red-900/20 rounded-lg"
                >
                  <div>
                    <p className="font-medium text-neutral-900 dark:text-neutral-100">
                      {new Date(block.date).toLocaleDateString("en-US", {
                        weekday: "long",
                        year: "numeric",
                        month: "long",
                        day: "numeric",
                      })}
                    </p>
                    <p className="text-sm text-neutral-600 dark:text-neutral-400 mt-1">
                      {block.reason}
                    </p>
                  </div>
                  <button
                    onClick={() => handleUnblockDate(block.id)}
                    className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition"
                  >
                    Unblock
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Add Time Slot Modal */}
        {showAddSlotModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-md w-full">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                  Add Time Slot
                </h2>
                <button
                  onClick={() => {
                    setShowAddSlotModal(false);
                    resetSlotForm();
                  }}
                  className="p-2 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
                >
                  <X className="h-5 w-5" />
                </button>
              </div>
              <form onSubmit={handleAddTimeSlot} className="p-6 space-y-4">
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Day of Week *
                  </label>
                  <select
                    value={slotForm.dayOfWeek}
                    onChange={(e) =>
                      setSlotForm({ ...slotForm, dayOfWeek: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  >
                    <option value="">Select Day</option>
                    {daysOfWeek.map((day) => (
                      <option key={day} value={day}>
                        {day}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Start Time *
                  </label>
                  <select
                    value={slotForm.startTime}
                    onChange={(e) =>
                      setSlotForm({ ...slotForm, startTime: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  >
                    <option value="">Select Time</option>
                    {timeOptions.map((time) => (
                      <option key={time} value={time}>
                        {time}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    End Time *
                  </label>
                  <select
                    value={slotForm.endTime}
                    onChange={(e) =>
                      setSlotForm({ ...slotForm, endTime: e.target.value })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  >
                    <option value="">Select Time</option>
                    {timeOptions.map((time) => (
                      <option key={time} value={time}>
                        {time}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Max Appointments *
                  </label>
                  <input
                    type="number"
                    min="1"
                    max="20"
                    value={slotForm.maxAppointments}
                    onChange={(e) =>
                      setSlotForm({
                        ...slotForm,
                        maxAppointments: e.target.value,
                      })
                    }
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  />
                </div>
                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => {
                      setShowAddSlotModal(false);
                      resetSlotForm();
                    }}
                    className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="btn-primary flex items-center"
                  >
                    <Save className="h-4 w-4 mr-2" />
                    Add Time Slot
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* Block Date Modal */}
        {showBlockDateModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
            <div className="bg-white dark:bg-neutral-800 rounded-lg shadow-xl max-w-md w-full">
              <div className="flex items-center justify-between p-6 border-b border-neutral-200 dark:border-neutral-700">
                <h2 className="text-2xl font-bold text-neutral-900 dark:text-neutral-100">
                  Block Date
                </h2>
                <button
                  onClick={() => {
                    setShowBlockDateModal(false);
                    resetBlockForm();
                  }}
                  className="p-2 hover:bg-neutral-100 dark:hover:bg-neutral-700 rounded-lg"
                >
                  <X className="h-5 w-5" />
                </button>
              </div>
              <form onSubmit={handleBlockDate} className="p-6 space-y-4">
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Date *
                  </label>
                  <input
                    type="date"
                    value={blockForm.date}
                    onChange={(e) =>
                      setBlockForm({ ...blockForm, date: e.target.value })
                    }
                    min={new Date().toISOString().split("T")[0]}
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                    Reason *
                  </label>
                  <textarea
                    value={blockForm.reason}
                    onChange={(e) =>
                      setBlockForm({ ...blockForm, reason: e.target.value })
                    }
                    rows={3}
                    className="w-full px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg focus:ring-2 focus:ring-teal-500 dark:bg-neutral-700 dark:text-neutral-100"
                    placeholder="e.g., Personal leave, Conference, etc."
                    required
                  />
                </div>
                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => {
                      setShowBlockDateModal(false);
                      resetBlockForm();
                    }}
                    className="px-4 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="btn-primary flex items-center"
                  >
                    <Save className="h-4 w-4 mr-2" />
                    Block Date
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}

export default withAuth(MySchedulePage, ["DOCTOR"]);
