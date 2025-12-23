 'use client'
import { motion } from 'framer-motion'
import { useEffect, useState } from 'react'
import { fetchJSON, Education as Edu } from '@/lib/api'

export function Education() {
  const [items, setItems] = useState<Edu[]>([])
  useEffect(() => {
    fetchJSON<Edu[]>('/education').then(setItems).catch(() => {
      setItems([
        { id: 1, school: 'Tech University', degree: 'B.Tech', field: 'Computer Science & Engineering', startYear: 2022, endYear: 2026 },
      ])
    })
  }, [])

  return (
    <section id="education" className="mt-24">
      <div className="mx-auto max-w-6xl px-4">
        <h2 className="text-2xl md:text-3xl font-semibold">Education</h2>
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
              <div className="font-semibold">{e.school}</div>
              <div className="text-sm text-gray-300">{e.degree} • {e.field}</div>
              <div className="text-xs text-gray-500">{e.startYear} — {e.endYear ?? 'Present'}</div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  )
}
