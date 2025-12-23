import Link from 'next/link';
import { LayoutDashboard, GraduationCap } from 'lucide-react';

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-muted/40 flex">
      {/* Sidebar */}
      <aside className="fixed inset-y-0 left-0 z-10 hidden w-64 flex-col border-r bg-background sm:flex">
        <div className="border-b px-6 py-4 flex items-center gap-2 h-16">
           <div className="flex h-8 w-8 items-center justify-center rounded bg-primary text-primary-foreground">
             <GraduationCap className="h-5 w-5" />
           </div>
           <span className="font-semibold text-lg tracking-tight">EduDash</span>
        </div>
        <nav className="flex flex-col gap-1 p-4">
          <Link
            href="/"
            className="flex items-center gap-3 rounded-lg px-3 py-2 text-muted-foreground transition-all hover:text-primary hover:bg-muted"
            suppressHydrationWarning
          >
            <LayoutDashboard className="h-4 w-4" />
            Overview
          </Link>
           <Link
            href="/results"
             className="flex items-center gap-3 rounded-lg px-3 py-2 text-muted-foreground transition-all hover:text-primary hover:bg-muted"
             suppressHydrationWarning
          >
            <GraduationCap className="h-4 w-4" />
            Exam Results
          </Link>
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 sm:pl-64 flex flex-col">
        <header className="sticky top-0 z-30 flex h-16 items-center gap-4 border-b bg-background px-6 shadow-sm sm:static sm:h-auto sm:border-0 sm:bg-transparent sm:px-10 sm:py-6">
           <h1 className="text-2xl font-bold tracking-tight">Dashboard Overview</h1>
        </header>
        <div className="p-4 sm:px-10 sm:py-0 pb-10">
          {children}
        </div>
      </main>
    </div>
  );
}
