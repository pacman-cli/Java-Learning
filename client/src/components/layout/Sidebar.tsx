"use client";

import { cn } from "@/lib/utils";
import {
    BarChart3,
    HelpCircle,
    LayoutDashboard,
    Package,
    Settings,
    ShoppingCart
} from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";

interface SidebarProps extends React.HTMLAttributes<HTMLDivElement> {
    onNavigate?: () => void; // Optional prop to close mobile drawer on click
}

export function Sidebar({ className, onNavigate }: SidebarProps) {
  const pathname = usePathname();

  const links = [
    { name: "Dashboard", href: "/dashboard", icon: LayoutDashboard },
    { name: "Orders", href: "/orders", icon: ShoppingCart },
    { name: "Products", href: "/products", icon: Package },
    { name: "Analytics", href: "/analytics", icon: BarChart3 },
  ];

  const secondaryLinks = [
      { name: "Settings", href: "/settings", icon: Settings },
      { name: "Help", href: "/help", icon: HelpCircle },
  ]

  return (
    <div className={cn("flex h-full flex-col gap-2 py-4", className)}>
      {/* Brand */}
      <div className="px-6 py-4 flex items-center gap-3 mb-2">
        <div className="h-9 w-9 rounded-xl bg-gradient-to-tr from-primary to-indigo-600 flex items-center justify-center shadow-lg shadow-primary/20 transition-transform hover:scale-105">
             <span className="text-white font-bold text-lg">B</span>
        </div>
        <h1 className="text-xl font-bold tracking-tight text-foreground/90">
          BusinessKPI
        </h1>
      </div>

      {/* Main Nav */}
      <div className="flex-1 px-4 space-y-8 overflow-y-auto no-scrollbar">
        <nav className="space-y-2">
          <p className="px-3 text-xs font-bold text-muted-foreground/60 uppercase tracking-widest mb-3">Platform</p>
          {links.map((link) => {
            const Icon = link.icon;
            const isActive = pathname === link.href;
            return (
              <Link
                key={link.href}
                href={link.href}
                onClick={onNavigate}
                className={cn(
                  "flex items-center gap-3 rounded-xl px-3.5 py-2.5 text-sm font-medium transition-all duration-300 group relative",
                  isActive
                    ? "bg-gradient-to-r from-white to-white/90 shadow-[0_4px_20px_rgba(var(--primary),0.2)] text-primary border border-white/60"
                    : "text-muted-foreground hover:bg-white/40 hover:text-foreground hover:translate-x-1"
                )}
              >
                <Icon className={cn("h-4 w-4 transition-transform duration-300 group-hover:scale-110", isActive ? "text-primary" : "")} />
                <span>{link.name}</span>
                {isActive && (
                    <span className="absolute right-3 h-2 w-2 rounded-full bg-primary shadow-sm" />
                )}
              </Link>
            );
          })}
        </nav>

        <nav className="space-y-2">
            <p className="px-3 text-xs font-bold text-muted-foreground/60 uppercase tracking-widest mb-3">Support</p>
            {secondaryLinks.map((link) => {
             const Icon = link.icon;
             const isActive = pathname === link.href;
             return (
               <Link
                 key={link.href}
                 href={link.href}
                 onClick={onNavigate}
                 className={cn(
                   "flex items-center gap-3 rounded-xl px-3.5 py-2.5 text-sm font-medium transition-all duration-300 group relative",
                   isActive
                     ? "bg-white shadow-md shadow-black/5 text-primary"
                     : "text-muted-foreground hover:bg-white/20 hover:text-foreground hover:translate-x-1"
                 )}
               >
                 <Icon className="h-4 w-4 transition-transform duration-300 group-hover:scale-110" />
                 <span>{link.name}</span>
               </Link>
             );
           })}
        </nav>
      </div>

      {/* User Footer */}
      <div className="px-4 pb-2 mt-auto">
        <div className="flex items-center gap-3 rounded-2xl bg-white/40 border border-white/40 p-3 shadow-sm backdrop-blur-md hover:bg-white/60 transition-all cursor-pointer group">
           <div className="h-10 w-10 rounded-full bg-gradient-to-tr from-indigo-500 to-violet-600 flex items-center justify-center text-white text-xs font-bold shadow-inner ring-2 ring-white/50 group-hover:scale-105 transition-transform duration-300">
              JD
           </div>
           <div className="flex flex-col overflow-hidden">
              <span className="text-sm font-bold truncate leading-none mb-1 text-foreground/90">John Doe</span>
              <span className="text-xs text-muted-foreground truncate leading-none">admin@kpi.com</span>
           </div>
        </div>
      </div>
    </div>
  );
}
