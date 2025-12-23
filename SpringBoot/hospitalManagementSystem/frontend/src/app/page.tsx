"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/providers/AuthProvider";

export default function HomePage() {
    const { isAuthenticated, isLoading } = useAuth();
    const router = useRouter();

    useEffect(() => {
        if (!isLoading) {
            if (isAuthenticated) {
                router.push("/dashboard");
            } else {
                router.push("/login");
            }
        }
    }, [isAuthenticated, isLoading, router]);

    if (isLoading) {
        return (
            <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-primary-50 to-secondary-50 dark:from-neutral-900 dark:to-neutral-800">
                <div className="text-center">
                    <div className="loading-spinner mx-auto h-12 w-12 mb-4"></div>
                    <h1 className="text-2xl font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                        Hospital Management System
                    </h1>
                    <p className="text-neutral-600 dark:text-neutral-400">
                        Loading...
                    </p>
                </div>
            </div>
        );
    }

    return (
        <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-primary-50 to-secondary-50 dark:from-neutral-900 dark:to-neutral-800">
            <div className="text-center">
                <div className="loading-spinner mx-auto h-12 w-12 mb-4"></div>
                <h1 className="text-2xl font-semibold text-neutral-900 dark:text-neutral-100 mb-2">
                    Hospital Management System
                </h1>
                <p className="text-neutral-600 dark:text-neutral-400">
                    Redirecting...
                </p>
            </div>
        </div>
    );
}
