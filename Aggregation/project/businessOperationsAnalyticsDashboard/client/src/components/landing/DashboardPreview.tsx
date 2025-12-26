"use client";

import { motion, useMotionValue, useSpring, useTransform } from "framer-motion";
import { MouseEvent } from "react";

export function DashboardPreview() {
  const x = useMotionValue(0);
  const y = useMotionValue(0);

  const rotateX = useTransform(y, [-100, 100], [5, -5]);
  const rotateY = useTransform(x, [-100, 100], [-5, 5]);

  const springConfig = { damping: 20, stiffness: 100 };
  const smoothRotateX = useSpring(rotateX, springConfig);
  const smoothRotateY = useSpring(rotateY, springConfig);

  const handleMouseMove = (event: MouseEvent<HTMLDivElement>) => {
    const rect = event.currentTarget.getBoundingClientRect();
    const centerX = rect.left + rect.width / 2;
    const centerY = rect.top + rect.height / 2;
    x.set(event.clientX - centerX);
    y.set(event.clientY - centerY);
  };

  const handleMouseLeave = () => {
    x.set(0);
    y.set(0);
  };

  return (
    <motion.div
      onMouseMove={handleMouseMove}
      onMouseLeave={handleMouseLeave}
      style={{ rotateX: smoothRotateX, rotateY: smoothRotateY }}
      className="relative w-full aspect-[16/10] rounded-2xl bg-card border border-border/50 shadow-2xl overflow-hidden cursor-move group"
    >
        {/* Glow Effects */}
        <div className="absolute inset-0 bg-gradient-to-tr from-primary/10 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 pointer-events-none" />

        {/* Browser Chrome */}
        <div className="h-10 bg-muted/80 backdrop-blur-md border-b border-border/50 flex items-center px-4 gap-2 z-10 relative">
            <div className="flex gap-1.5">
                <div className="w-3 h-3 rounded-full bg-rose-500/50" />
                <div className="w-3 h-3 rounded-full bg-amber-500/50" />
                <div className="w-3 h-3 rounded-full bg-emerald-500/50" />
            </div>
            <div className="mx-auto w-1/3 h-5 bg-background/50 rounded-md text-[10px] flex items-center justify-center text-muted-foreground shadow-inner">
                app.businesskpi.com
            </div>
        </div>

        {/* CSS Mock Interface */}
        <div className="flex h-full bg-background/50">
            {/* Sidebar */}
            <div className="w-64 border-r border-border/50 bg-muted/5 p-4 space-y-6 hidden md:block">
                <div className="h-8 w-32 bg-primary/10 rounded-lg animate-pulse" />
                <div className="space-y-2">
                     <div className="h-8 w-full bg-muted/50 rounded-lg" />
                     <div className="h-8 w-full bg-muted/50 rounded-lg" />
                     <div className="h-8 w-full bg-primary/5 rounded-lg border border-primary/10" />
                     <div className="h-8 w-full bg-muted/50 rounded-lg" />
                </div>
            </div>

            {/* Main Content */}
            <div className="flex-1 p-6 space-y-6 overflow-hidden relative">
                 {/* Header Mock */}
                 <div className="flex justify-between items-center">
                     <div className="h-8 w-48 bg-muted/50 rounded-lg" />
                     <div className="h-9 w-24 bg-primary rounded-full shadow-lg shadow-primary/20" />
                 </div>

                 {/* Charts Grid */}
                 <div className="grid grid-cols-4 gap-4">
                     {[...Array(4)].map((_, i) => (
                         <div key={i} className="h-32 rounded-xl border border-border/50 bg-card p-4 flex flex-col justify-between hover:border-primary/20 transition-colors">
                             <div className="h-4 w-16 bg-muted/50 rounded" />
                             <div className="h-8 w-24 bg-gradient-to-r from-primary/20 to-transparent rounded" />
                         </div>
                     ))}
                 </div>

                 {/* Big Chart */}
                 <div className="h-64 rounded-xl border border-border/50 bg-card p-6 relative overflow-hidden">
                      <div className="absolute bottom-0 left-0 right-0 h-32 bg-gradient-to-t from-primary/10 to-transparent" />
                      <div className="w-full h-full flex items-end justify-between gap-2 px-4 pb-4">
                          {[...Array(12)].map((_, i) => (
                              <div
                                key={i}
                                className="w-full bg-primary/20 rounded-t-sm"
                                style={{ height: `${Math.random() * 60 + 20}%` }}
                              />
                          ))}
                      </div>
                 </div>
            </div>
        </div>

        {/* Reflection */}
        <div className="absolute inset-0 bg-gradient-to-tr from-transparent via-white/5 to-transparent pointer-events-none mix-blend-overlay" />
    </motion.div>
  );
}
