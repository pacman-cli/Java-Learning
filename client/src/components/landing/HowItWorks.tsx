"use client";

import { motion } from "framer-motion";

const steps = [
  {
    number: "01",
    title: "Connect your data source",
    description: "Integrate with your existing database or API in seconds using our secure connectors."
  },
  {
    number: "02",
    title: "Customize your dashboard",
    description: "Drag and drop widgets to create the perfect view for your specific workflow."
  },
  {
    number: "03",
    title: "Get actionable insights",
    description: "Receive AI-powered recommendations and detailed reports automatically."
  }
];

export function HowItWorks() {
  return (
    <section id="how-it-works" className="py-32 relative">
      <div className="container px-4 mx-auto">
        <div className="grid md:grid-cols-2 gap-16 items-center">

            <motion.div
                initial={{ opacity: 0, x: -50 }}
                whileInView={{ opacity: 1, x: 0 }}
                viewport={{ once: true }}
                className="relative"
            >
                <h2 className="text-4xl md:text-5xl font-bold tracking-tight mb-6 leading-tight">
                    Set up in minutes. <br />
                    <span className="text-primary">Scale forever.</span>
                </h2>
                <p className="text-lg text-muted-foreground mb-8">
                    We've optimized every step of the process so you can focus on making decisions, not configuring software.
                </p>
            </motion.div>

            <div className="space-y-12 relative">
                {/* Connecting Line (Simulated) */}
                <div className="absolute left-6 top-8 bottom-8 w-0.5 bg-gradient-to-b from-primary/50 to-transparent hidden md:block" />

                {steps.map((step, index) => (
                    <motion.div
                        key={index}
                        initial={{ opacity: 0, x: 50 }}
                        whileInView={{ opacity: 1, x: 0 }}
                        viewport={{ once: true }}
                        transition={{ delay: index * 0.2 }}
                        className="flex gap-6 relative"
                    >
                        <div className="flex-shrink-0 h-12 w-12 rounded-full bg-card border border-border shadow-sm flex items-center justify-center font-bold text-lg z-10 relative group hover:scale-110 transition-transform hover:border-primary hover:text-primary">
                            {step.number}
                        </div>
                        <div>
                            <h3 className="text-xl font-bold mb-2">{step.title}</h3>
                            <p className="text-muted-foreground">{step.description}</p>
                        </div>
                    </motion.div>
                ))}
            </div>
        </div>
      </div>
    </section>
  );
}
