 'use client'
import { motion } from 'framer-motion'
import { useEffect, useState } from 'react'
import { fetchJSON, Experience as Exp } from '@/lib/api'

export function Experience() {
  const [items, setItems] = useState<Exp[]>([])
  useEffect(() => {
    fetchJSON<Exp[]>('/experience').then(setItems).catch(() => {
      setItems([
        { id: 1, company: 'Open Source', role: 'Contributor', startDate: '2024-01-01', description: 'Contributed to tooling and UI libraries.' },
      ])
    })
  }, [])

  return (
    <section id="experience" className="mt-24">
      <div className="mx-auto max-w-6xl px-4">
        <h2 className="text-2xl md:text-3xl font-semibold">Experience</h2>
        <div className="mt-6 grid gap-4">
          {items.map((e, i) => (
            <motion.div
              key={e.id}
              initial={{ opacity: 0, y: 10 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: i * 0.05 }}
              className="glass rounded-xl p-4"
            >
              <div className="font-semibold">{e.company}</div>
              <div className="text-sm text-gray-300">{e.role}</div>
              <div className="text-xs text-gray-500">{new Date(e.startDate).toLocaleDateString()} — {e.endDate ? new Date(e.endDate).toLocaleDateString() : 'Present'}</div>
              <p className="mt-2 text-sm text-gray-300">{e.description}</p>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  )
}
