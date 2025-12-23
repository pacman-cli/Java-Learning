"use client";

import { Button } from "@/components/ui/button";
import { motion, useScroll, useTransform } from "framer-motion";
import { ArrowRight, PlayCircle } from "lucide-react";
import Link from "next/link";
import { useRef } from "react";
import { StackPreview } from "./StackPreview";
export function Hero() {
  const containerRef = useRef<HTMLDivElement>(null);
  const { scrollYProgress } = useScroll({
    target: containerRef,
    offset: ["start start", "end start"],
  });

  const y = useTransform(scrollYProgress, [0, 1], ["0%", "50%"]);
  const opacity = useTransform(scrollYProgress, [0, 0.5], [1, 0]);

  return (
    <section ref={containerRef} className="relative min-h-screen flex flex-col items-center justify-center pt-32 pb-20 overflow-hidden">
      {/* Dynamic Background */}
      <div className="absolute inset-0 -z-10">
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[1000px] h-[600px] bg-primary/20 rounded-full blur-[120px] opacity-40 animate-blob mix-blend-multiply dark:mix-blend-screen" />
        <div className="absolute bottom-0 right-0 w-[800px] h-[600px] bg-indigo-500/10 rounded-full blur-[100px] opacity-30 animate-blob animation-delay-2000" />
        <div className="absolute top-1/2 left-0 w-[600px] h-[400px] bg-purple-500/10 rounded-full blur-[100px] opacity-30 animate-blob animation-delay-4000" />
      </div>

      <div className="container px-4 text-center z-10">
        {/* Script Label */}
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            className="mb-4"
        >
            <span style={{ fontFamily: 'var(--font-script)' }} className="text-3xl md:text-4xl text-amber-500 tracking-wide rotate-[-2deg] inline-block">
                Master Business Intelligence
            </span>
        </motion.div>

        {/* Massive Headline */}
        <motion.h1
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.1 }}
            className="text-6xl md:text-8xl lg:text-9xl font-black tracking-tighter mb-6 leading-[0.9]"
        >
          ANALYTICS <br className="hidden md:block" />
          <span className="bg-gradient-to-r from-orange-400 via-amber-500 to-orange-600 bg-clip-text text-transparent pb-4">
            REIMAGINED
          </span>
        </motion.h1>

        {/* Subtitle */}
        <motion.p
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.2 }}
            className="text-lg md:text-2xl font-medium text-muted-foreground/80 max-w-3xl mx-auto mb-12 leading-relaxed"
        >
          Transform raw data into <span className="text-foreground font-semibold">clear, actionable insights</span>.
          The premium dashboard designed for modern speed and clarity.
        </motion.p>

        {/* CTAs */}
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.3 }}
            className="flex flex-col sm:flex-row items-center justify-center gap-5 mb-24"
        >
          <Link href="/register">
            <Button className="rounded-full h-14 px-10 text-lg font-semibold bg-orange-500 hover:bg-orange-600 shadow-xl shadow-orange-500/20 hover:shadow-2xl hover:scale-105 active:scale-95 transition-all duration-300 ring-2 ring-orange-500/20">
              Get Started Now <ArrowRight className="ml-2 h-5 w-5" />
            </Button>
          </Link>
          <Link href="#demo">
             <Button variant="outline" className="rounded-full h-14 px-10 text-lg font-medium border-2 border-foreground/5 bg-transparent hover:bg-foreground/5 hover:text-foreground backdrop-blur-sm transition-all duration-300">
               <PlayCircle className="mr-2 h-5 w-5" />
               Watch Demo
             </Button>
          </Link>
        </motion.div>
      </div>

      {/* 3D Preview */}
      <motion.div
        style={{ y, opacity }}
        className="w-full max-w-6xl mx-auto px-4 perspective-1000 mt-12"
      >
        <StackPreview />
      </motion.div>
    </section>
  );
}
