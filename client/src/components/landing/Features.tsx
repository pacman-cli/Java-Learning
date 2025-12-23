"use client";

import { cn } from "@/lib/utils";
import { motion } from "framer-motion";
import { BarChart3, Lock, MousePointer2, Zap } from "lucide-react";

const features = [
  {
    icon: BarChart3,
    title: "Real-time Analytics",
    description: "Watch your business grow live. No more refreshing spreadsheets.",
    className: "md:col-span-2",
  },
  {
    icon: Zap,
    title: "Instant Search",
    description: "Filter millions of records in milliseconds.",
    className: "md:col-span-1",
  },
  {
    icon: Lock,
    title: "Enterprise Security",
    description: "Bank-grade encryption by default.",
    className: "md:col-span-1",
  },
  {
    icon: MousePointer2,
    title: "Drag & Drop",
    description: "Customize your dashboard your way.",
    className: "md:col-span-2",
  },
];

export function Features() {
  return (
    <section id="features" className="py-24 bg-muted/10 relative overflow-hidden">
      <div className="container px-4 mx-auto">
        <div className="text-center max-w-3xl mx-auto mb-16">
          <motion.h2
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-3xl md:text-5xl font-bold tracking-tight mb-4"
          >
            Powerful features. <br />
            <span className="text-muted-foreground">Zero complexity.</span>
          </motion.h2>
          <p className="text-lg text-muted-foreground">
            Everything you need to manage your business operations without the headache.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-6xl mx-auto">
          {features.map((feature, index) => {
            const Icon = feature.icon;
            return (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ delay: index * 0.1 }}
                className={cn(
                  "group relative overflow-hidden rounded-3xl bg-card border border-border/50 p-8 shadow-sm hover:shadow-xl transition-all hover:border-primary/20",
                  feature.className
                )}
              >
                 {/* Hover Gradient */}
                 <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500" />

                 <div className="relative z-10 flex flex-col h-full">
                    <div className="h-12 w-12 rounded-2xl bg-muted/50 border border-border flex items-center justify-center mb-6 group-hover:scale-110 group-hover:bg-primary/10 group-hover:text-primary transition-all duration-300">
                        <Icon className="h-6 w-6" />
                    </div>
                    <h3 className="text-2xl font-semibold mb-2">{feature.title}</h3>
                    <p className="text-muted-foreground leading-relaxed">
                        {feature.description}
                    </p>
                 </div>
              </motion.div>
            );
          })}
        </div>
      </div>
    </section>
  );
}
