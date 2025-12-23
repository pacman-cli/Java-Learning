 'use client'
import { motion } from 'framer-motion'
import { useEffect, useMemo, useState } from 'react'
import { fetchJSON, Project } from '@/lib/api'

const filters = ['All', 'Frontend', 'Backend', 'AI', 'Systems']

export function Projects() {
  const [projects, setProjects] = useState<Project[]>([])
  const [filter, setFilter] = useState('All')

  useEffect(() => {
    fetchJSON<Project[]>('/projects').then(setProjects).catch(() => {
      setProjects([
        { id: 1, title: 'AI Study Assistant', description: 'LLM‑powered note summarizer and Q&A.', tags: ['AI', 'Frontend'], demoUrl: '#', repoUrl: '#' },
        { id: 2, title: 'Portfolio Engine', description: 'Next.js + Spring Boot full‑stack portfolio.', tags: ['Frontend', 'Backend'], demoUrl: '#', repoUrl: '#' },
        { id: 3, title: 'Microservice Catalog', description: 'Event‑driven product catalog.', tags: ['Backend', 'Systems'], demoUrl: '#', repoUrl: '#' },
      ])
    })
  }, [])

  const filtered = useMemo(
    () => (filter === 'All' ? projects : projects.filter(p => p.tags.includes(filter))),
    [projects, filter]
  )

  return (
    <section id="projects" className="mt-24">
      <div className="mx-auto max-w-6xl px-4">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl md:text-3xl font-semibold">Projects</h2>
          <div className="flex gap-2">
            {filters.map(f => (
              <button
                key={f}
                onClick={() => setFilter(f)}
                className={`px-3 py-1 rounded-full text-xs ${filter === f ? 'bg-brand text-white' : 'glass text-gray-300'}`}
              >
                {f}
              </button>
            ))}
          </div>
        </div>
        <div className="mt-6 grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {filtered.map((p, i) => (
            <motion.a
              key={p.id}
              href={p.demoUrl || p.repoUrl || '#'}
              target="_blank"
              rel="noopener noreferrer"
              initial={{ opacity: 0, y: 10 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: i * 0.05 }}
              className="glass rounded-2xl p-4 hover:shadow-glow hover-3d"
            >
              <div className="hover-3d-child">
                <h3 className="text-lg font-semibold">{p.title}</h3>
                <p className="mt-2 text-sm text-gray-300">{p.description}</p>
                <div className="mt-4 flex flex-wrap gap-2">
                  {p.tags.map(t => (
                    <span key={t} className="px-2 py-1 text-xs rounded-full bg-white/10 text-gray-200 border border-white/10">{t}</span>
                  ))}
                </div>
              </div>
            </motion.a>
          ))}
        </div>
      </div>
    </section>
  )
}
