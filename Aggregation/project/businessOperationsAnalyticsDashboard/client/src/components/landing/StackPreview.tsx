"use client";

import { motion, useScroll, useTransform } from "framer-motion";
import { DollarSign, TrendingUp, Users } from "lucide-react";
import { useRef } from "react";

export function StackPreview() {
  const containerRef = useRef<HTMLDivElement>(null);
  const { scrollYProgress } = useScroll({
    target: containerRef,
    offset: ["start end", "end start"],
  });

  const y = useTransform(scrollYProgress, [0, 1], ["0%", "20%"]);
  const rotateOr = useTransform(scrollYProgress, [0, 1], [0, 5]);

  return (
    <div ref={containerRef} className="relative w-full max-w-[800px] aspect-[4/3] mx-auto perspective-1000 group">
      {/* Back Card (Abstract Graph) */}
      <motion.div
        style={{ y: useTransform(scrollYProgress, [0, 1], ["0%", "10%"]), rotateX: 10 }}
        className="absolute top-0 left-0 right-0 h-full bg-white/40 backdrop-blur-md rounded-3xl border border-white/40 shadow-2xl scale-[0.85] translate-y-[-40px] z-10 transition-transform duration-500 group-hover:translate-y-[-60px] group-hover:rotate-x-12 overflow-hidden"
      >
        <div className="absolute inset-0 bg-gradient-to-br from-indigo-500/5 to-purple-500/5" />
        <div className="p-8 h-full flex items-end justify-between gap-4 opacity-30">
             {[40, 70, 50, 90, 60, 80].map((h, i) => (
                 <div key={i} className="w-full bg-slate-800 rounded-t-lg" style={{ height: `${h}%` }} />
             ))}
        </div>
      </motion.div>

      {/* Middle Card (UI Snippet) */}
      <motion.div
        style={{ y: useTransform(scrollYProgress, [0, 1], ["0%", "5%"]), rotateX: 5 }}
        className="absolute top-4 left-4 right-4 h-full bg-white/60 backdrop-blur-xl rounded-3xl border border-white/50 shadow-2xl shadow-black/5 scale-[0.92] translate-y-[-20px] z-20 transition-transform duration-500 group-hover:translate-y-[-30px] group-hover:rotate-x-6 overflow-hidden"
      >
        <div className="p-6 space-y-4">
            <div className="flex items-center justify-between border-b border-black/5 pb-4">
                <div className="h-4 w-24 bg-slate-800/10 rounded-full" />
                <div className="h-8 w-8 bg-slate-800/10 rounded-full" />
            </div>
            <div className="space-y-3">
                {[1, 2, 3].map((i) => (
                    <div key={i} className="flex items-center gap-4 p-3 rounded-xl bg-white/40">
                        <div className="h-10 w-10 rounded-full bg-orange-100 flex items-center justify-center text-orange-600">
                            <DollarSign className="h-5 w-5" />
                        </div>
                        <div className="flex-1 space-y-2">
                             <div className="h-3 w-1/3 bg-slate-800/10 rounded-full" />
                             <div className="h-2 w-1/4 bg-slate-800/5 rounded-full" />
                        </div>
                    </div>
                ))}
            </div>
        </div>
      </motion.div>

      {/* Front Card (Key Metric) */}
      <motion.div
        style={{ y, rotateX: 0 }}
        whileHover={{ scale: 1.02, rotateX: 2 }}
        className="absolute inset-x-8 top-12 bottom-0 bg-white/80 backdrop-blur-2xl rounded-3xl border border-white/60 shadow-[0_20px_50px_rgba(0,0,0,0.1)] z-30 flex flex-col items-center justify-center text-center p-8 transition-all duration-500"
      >
        <div className="absolute inset-0 bg-gradient-to-b from-white/50 to-transparent pointer-events-none" />

        <div className="mb-6 p-4 rounded-2xl bg-gradient-to-br from-orange-400 to-amber-500 shadow-lg shadow-orange-500/30 text-white">
            <TrendingUp className="h-12 w-12" />
        </div>

        <h3 className="text-4xl font-bold text-slate-800 mb-2">
            +24.8%
        </h3>
        <p className="text-lg text-slate-500 font-medium">Monthly Revenue Growth</p>

        <div className="mt-8 flex items-center justify-center gap-2 text-sm font-medium text-slate-400">
            <Users className="h-4 w-4" />
            <span>Active Users: 14,203</span>
        </div>
      </motion.div>
    </div>
  );
}
