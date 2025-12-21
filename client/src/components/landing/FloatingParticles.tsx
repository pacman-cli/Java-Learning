"use client";

import { motion } from "framer-motion";

export function FloatingParticles() {
  return (
    <div className="absolute inset-0 overflow-hidden pointer-events-none -z-10">
      {/* Particle 1 */}
      <motion.div
        animate={{
          y: [0, -40, 0],
          x: [0, 20, 0],
          opacity: [0.3, 0.6, 0.3],
        }}
        transition={{
          duration: 10,
          repeat: Infinity,
          ease: "easeInOut",
        }}
        className="absolute top-1/4 left-1/4 w-64 h-64 bg-amber-200/20 rounded-full blur-[80px] mix-blend-multiply"
      />

      {/* Particle 2 */}
      <motion.div
        animate={{
          y: [0, 40, 0],
          x: [0, -30, 0],
          opacity: [0.3, 0.5, 0.3],
        }}
        transition={{
          duration: 15,
          repeat: Infinity,
          ease: "easeInOut",
          delay: 2,
        }}
        className="absolute top-1/3 right-1/4 w-96 h-96 bg-orange-200/20 rounded-full blur-[100px] mix-blend-multiply"
      />

      {/* Particle 3 */}
      <motion.div
        animate={{
          scale: [1, 1.2, 1],
          opacity: [0.2, 0.4, 0.2],
        }}
        transition={{
          duration: 12,
          repeat: Infinity,
          ease: "easeInOut",
          delay: 5,
        }}
        className="absolute bottom-1/4 left-1/3 w-72 h-72 bg-rose-200/20 rounded-full blur-[90px] mix-blend-multiply"
      />
    </div>
  );
}
