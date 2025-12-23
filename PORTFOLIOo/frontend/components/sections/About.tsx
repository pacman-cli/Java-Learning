import { motion } from 'framer-motion'

export function About() {
  return (
    <section id="about" className="mt-24">
      <div className="mx-auto max-w-6xl px-4">
        <motion.h2
          initial={{ opacity: 0, y: 10 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-2xl md:text-3xl font-semibold"
        >
          About Me
        </motion.h2>
        <motion.p
          initial={{ opacity: 0, y: 10 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="mt-4 text-gray-300"
        >
          Passionate CSE student focused on crafting high‑performance, accessible, and delightful experiences.
          I enjoy systems programming, backend architectures, and expressive frontends with robust engineering.
        </motion.p>
      </div>
    </section>
  )
}
