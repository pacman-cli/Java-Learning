"use client";

import { cn } from "@/lib/utils";
import { motion } from "framer-motion";
import { BarChart3, Globe, Zap } from "lucide-react";
import { useRef } from "react";

const features = [
  {
    icon: BarChart3,
    title: "Real-time Analytics",
    description: "Watch your business grow live. Visualization that updates instantly as data flows in.",
    color: "bg-orange-500",
    gradient: "from-orange-500/20 to-amber-500/20",
    chart: (
        <div className="relative w-full h-40 mt-4 flex items-end gap-1 px-4">
             {[30, 45, 35, 60, 50, 75, 65, 80, 70, 90].map((h, i) => (
                 <div key={i} className="flex-1 bg-orange-500/20 rounded-t-sm relative overflow-hidden group-hover:bg-orange-500/30 transition-colors" style={{ height: `${h}%` }}>
                      <div className="absolute bottom-0 w-full bg-orange-500 opacity-60" style={{ height: '10%' }} />
                 </div>
             ))}
        </div>
    )
  },
  {
    icon: Globe,
    title: "Global Connectivity",
    description: "Connect with customers worldwide. Automated currency conversion and localization.",
    color: "bg-blue-500",
    gradient: "from-blue-500/20 to-indigo-500/20",
    chart: (
        <div className="relative w-full h-40 mt-4 flex items-center justify-center">
            <div className="w-32 h-32 rounded-full border-2 border-blue-500/20 flex items-center justify-center animate-pulse">
                <div className="w-24 h-24 rounded-full border-2 border-blue-500/30 flex items-center justify-center">
                     <div className="w-16 h-16 rounded-full bg-blue-500/10 backdrop-blur-md" />
                </div>
            </div>
            <div className="absolute inset-0 flex items-center justify-center gap-12">
                 <div className="w-2 h-2 rounded-full bg-blue-400 animate-bounce" style={{ animationDelay: '0s' }} />
                 <div className="w-2 h-2 rounded-full bg-blue-400 animate-bounce" style={{ animationDelay: '0.2s' }} />
                 <div className="w-2 h-2 rounded-full bg-blue-400 animate-bounce" style={{ animationDelay: '0.4s' }} />
            </div>
        </div>
    )
  },
  {
    icon: Zap,
    title: "Smart Operations",
    description: "Automate repetitive tasks with our AI-driven workflow engine.",
    color: "bg-emerald-500",
    gradient: "from-emerald-500/20 to-teal-500/20",
    chart: (
        <div className="relative w-full h-40 mt-4 p-4 space-y-3">
             <div className="flex items-center gap-3">
                 <div className="w-8 h-8 rounded-lg bg-emerald-500/20 flex items-center justify-center text-emerald-600">
                     <Zap className="h-4 w-4" />
                 </div>
                 <div className="h-2 w-24 bg-emerald-500/20 rounded-full" />
                 <div className="ml-auto w-12 h-6 rounded-full bg-emerald-500/10" />
             </div>
             <div className="flex items-center gap-3 opacity-60">
                 <div className="w-8 h-8 rounded-lg bg-emerald-500/20 flex items-center justify-center text-emerald-600">
                     <Zap className="h-4 w-4" />
                 </div>
                 <div className="h-2 w-32 bg-emerald-500/20 rounded-full" />
                 <div className="ml-auto w-12 h-6 rounded-full bg-emerald-500/10" />
             </div>
             <div className="flex items-center gap-3 opacity-30">
                 <div className="w-8 h-8 rounded-lg bg-emerald-500/20 flex items-center justify-center text-emerald-600">
                     <Zap className="h-4 w-4" />
                 </div>
                 <div className="h-2 w-20 bg-emerald-500/20 rounded-full" />
                 <div className="ml-auto w-12 h-6 rounded-full bg-emerald-500/10" />
             </div>
        </div>
    )
  },
];

export function ScrollStack() {
  const containerRef = useRef<HTMLDivElement>(null);

  // Optional: Background color shift based on scroll could go here if containerRef covers enough height

  return (
    <section ref={containerRef} className="py-24 bg-background relative" id="features">
       <div className="container px-4 mx-auto mb-20 text-center">
            <motion.h2
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              className="text-3xl md:text-5xl font-bold tracking-tight mb-4"
            >
              Built for <span className="text-primary italic">scale</span>.
            </motion.h2>
            <p className="text-muted-foreground text-lg max-w-2xl mx-auto">
                Scroll to see how we stack up against operational complexity.
            </p>
       </div>

       <div className="max-w-5xl mx-auto space-y-8 pb-32">
          {features.map((feature, index) => {
              const Icon = feature.icon;
              return (
                  <div
                    key={index}
                    className="sticky top-32 min-h-[400px]" // Sticky stacking magic
                  >
                      <motion.div
                        initial={{ opacity: 0, y: 50, scale: 0.9 }}
                        whileInView={{ opacity: 1, y: 0, scale: 1 }}
                        viewport={{ once: true, margin: "-100px" }}
                        transition={{ duration: 0.5 }}
                        className="relative overflow-hidden rounded-3xl border border-white/40 bg-white/80 backdrop-blur-xl shadow-2xl shadow-black/5 p-8 md:p-12 h-full flex flex-col md:flex-row items-center gap-12"
                      >
                         {/* Content */}
                         <div className="flex-1 space-y-6 z-10">
                             <div className={cn("w-14 h-14 rounded-2xl flex items-center justify-center text-white shadow-lg", feature.color)}>
                                 <Icon className="h-7 w-7" />
                             </div>
                             <div>
                                 <h3 className="text-3xl font-bold mb-3">{feature.title}</h3>
                                 <p className="text-muted-foreground text-lg leading-relaxed">
                                     {feature.description}
                                 </p>
                             </div>
                         </div>

                         {/* Visual */}
                         <div className={cn(
                             "flex-1 w-full aspect-video rounded-2xl bg-gradient-to-br border border-white/50 shadow-inner flex flex-col justify-end overflow-hidden",
                             feature.gradient
                         )}>
                             {feature.chart}
                         </div>

                         {/* Background Decoration */}
                         <div className="absolute top-0 right-0 w-64 h-64 bg-gradient-to-br from-white/20 to-transparent rounded-full blur-3xl -z-10" />
                      </motion.div>
                  </div>
              )
          })}
       </div>
    </section>
  );
}
