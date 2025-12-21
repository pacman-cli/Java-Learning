"use client";

import { Moon, Sun } from "lucide-react";
import { useTheme } from "next-themes";

import { MobileSidebar } from "./MobileSidebar";

export function Header() {
  const { setTheme, theme } = useTheme();

  return (
    <header className="sticky top-0 z-30 flex h-16 w-full items-center justify-between px-6 transition-all">
      <div className="flex items-center gap-4">
        <MobileSidebar />
        <span className="text-xl font-bold bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent md:hidden">MyDashboard</span>
      </div>
      <div className="flex items-center gap-3">
        <button
          onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
          className="relative inline-flex h-9 w-9 items-center justify-center rounded-full border border-border/40 bg-background/50 hover:bg-muted/50 transition-colors"
        >
          <Sun className="h-4 w-4 rotate-0 scale-100 transition-all duration-300 dark:-rotate-90 dark:scale-0 text-amber-500" />
          <Moon className="absolute h-4 w-4 rotate-90 scale-0 transition-all duration-300 dark:rotate-0 dark:scale-100 text-indigo-400" />
          <span className="sr-only">Toggle theme</span>
        </button>
      </div>
    </header>
  );
}
