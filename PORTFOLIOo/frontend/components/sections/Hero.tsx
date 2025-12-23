import { motion } from 'framer-motion'

export function Hero() {
  return (
    <section id="top" className="pt-28">
      <div className="mx-auto max-w-6xl px-4">
        <div className="grid md:grid-cols-2 gap-8 items-center">
          <div>
            <motion.h1
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
              className="text-4xl md:text-6xl font-bold tracking-tight"
            >
              Building futuristic software with elegance and performance
            </motion.h1>
            <motion.p
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2, duration: 0.6 }}
              className="mt-4 text-gray-300"
            >
              I’m a CSE student specializing in full‑stack development, systems design, and AI‑inspired interfaces.
            </motion.p>
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.4, duration: 0.6 }}
              className="mt-6 flex gap-4"
            >
              <a href="#projects" className="px-5 py-3 rounded-xl bg-brand hover:shadow-glow transition">View Projects</a>
              <a href="#contact" className="px-5 py-3 rounded-xl glass hover:shadow-glow transition">Get in touch</a>
            </motion.div>
          </div>
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.2, duration: 0.6 }}
            className="hover-3d glass rounded-2xl p-8"
          >
            <div className="hover-3d-child">
              <div className="h-56 bg-gradient-to-tr from-brand via-brand-pink to-brand-neon rounded-xl blur-[1px] opacity-80" />
              <p className="mt-4 text-sm text-gray-300">
                Smooth gradients, micro‑interactions, and subtle parallax create an immersive experience.
              </p>
            </div>
          </motion.div>
        </div>
      </div>
    </section>
  )
}
