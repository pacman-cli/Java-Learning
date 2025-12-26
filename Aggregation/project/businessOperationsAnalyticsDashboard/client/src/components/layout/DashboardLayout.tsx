"use client";

import { FloatingParticles } from "@/components/landing/FloatingParticles";
import { Header } from "@/components/layout/Header";
import { Sidebar } from "@/components/layout/Sidebar";
import { cn } from "@/lib/utils";

interface DashboardLayoutProps {
  children: React.ReactNode;
  className?: string;
}

export function DashboardLayout({ children, className }: DashboardLayoutProps) {
  return (
    <div className="min-h-screen bg-background font-sans selection:bg-primary/20 relative">
      <FloatingParticles />
      {/*
        Grid Layout:
        Desktop: 280px fixed sidebar | 1fr content
        Mobile: 1fr content (Sidebar is hidden/drawer)
      */}
      <div className="grid lg:grid-cols-[280px_1fr] h-screen overflow-hidden">

        {/* Desktop Sidebar */}
        <aside className="hidden lg:flex flex-col m-4 mr-0 glass-sidebar z-30 transition-all duration-300">
          <Sidebar />
        </aside>

        {/* Main Content Area */}
        <div className="flex flex-col h-full overflow-hidden relative bg-background">
          <Header />

          <main className="flex-1 overflow-y-auto overflow-x-hidden p-4 md:p-6 lg:p-8 scroll-smooth will-change-transform">
            {/* Max Width Container for Centering */}
            <div className={cn(
              "mx-auto w-full max-w-[1440px] space-y-6 md:space-y-8 animate-in fade-in slide-in-from-bottom-3 duration-500 ease-out",
              className
            )}>
              {children}
            </div>

            {/* Footer Spacing */}
            <div className="h-10 md:h-20" />
          </main>
        </div>
      </div>
    </div>
  );
}
