'use client'
import { useEffect, useState } from 'react'
import { motion } from 'framer-motion'
import { fetchJSON, Skill } from '@/lib/api'

export function Skills() {
  const [skills, setSkills] = useState<Skill[]>([])
  useEffect(() => {
    fetchJSON<Skill[]>('/skills').then(setSkills).catch(() => {
      setSkills([
        { id: 1, name: 'Java', level: 85, category: 'Backend' },
        { id: 2, name: 'Spring Boot', level: 80, category: 'Backend' },
        { id: 3, name: 'Next.js', level: 75, category: 'Frontend' },
        { id: 4, name: 'MySQL', level: 70, category: 'Database' },
        { id: 5, name: 'Docker', level: 65, category: 'DevOps' },
      ])
    })
  }, [])

  return (
    <section id="skills" className="mt-24">
      <div className="mx-auto max-w-6xl px-4">
        <h2 className="text-2xl md:text-3xl font-semibold">Skills</h2>
        <div className="mt-6 grid md:grid-cols-2 gap-6">
          {skills.map((s, i) => (
            <div key={s.id} className="glass rounded-xl p-4">
              <div className="flex justify-between text-sm">
                <span className="text-gray-300">{s.name}</span>
                <span className="text-gray-400">{s.level}%</span>
              </div>
              <div className="mt-2 h-2 bg-white/10 rounded-full overflow-hidden">
                <motion.div
                  initial={{ width: 0 }}
                  whileInView={{ width: `${s.level}%` }}
                  viewport={{ once: true }}
                  transition={{ duration: 0.8, delay: i * 0.05 }}
                  className="h-full bg-gradient-to-r from-brand via-brand-neon to-brand-pink"
                />
              </div>
              <div className="mt-2 text-xs text-gray-500">{s.category}</div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
