'use client'
import { motion } from 'framer-motion'
import { useState } from 'react'
import { fetchJSON } from '@/lib/api'

export function Contact() {
  const [loading, setLoading] = useState(false)
  const [status, setStatus] = useState<string | null>(null)

  async function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()
    setLoading(true)
    setStatus(null)
    const fd = new FormData(e.currentTarget)
    const payload = {
      name: fd.get('name'),
      email: fd.get('email'),
      message: fd.get('message'),
    }
    try {
      await fetchJSON('/contact', { method: 'POST', body: JSON.stringify(payload) })
      setStatus('Sent! I will get back to you soon.')
      e.currentTarget.reset()
    } catch {
      setStatus('Something went wrong. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <section id="contact" className="mt-24">
      <div className="mx-auto max-w-6xl px-4">
        <h2 className="text-2xl md:text-3xl font-semibold">Contact</h2>
        <motion.form
          onSubmit={onSubmit}
          initial={{ opacity: 0, y: 10 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="mt-6 glass rounded-2xl p-6 grid md:grid-cols-2 gap-4"
        >
          <input name="name" required placeholder="Name" className="px-4 py-3 rounded-xl bg-white/5 border border-white/10 focus:outline-none focus:ring-2 focus:ring-brand" />
          <input name="email" type="email" required placeholder="Email" className="px-4 py-3 rounded-xl bg-white/5 border border-white/10 focus:outline-none focus:ring-2 focus:ring-brand" />
          <textarea name="message" required placeholder="Message" className="md:col-span-2 px-4 py-3 rounded-xl bg-white/5 border border-white/10 h-32 focus:outline-none focus:ring-2 focus:ring-brand" />
          <motion.button
            type="submit"
            whileTap={{ scale: 0.98 }}
            className="md:col-span-2 px-5 py-3 rounded-xl bg-brand hover:shadow-glow transition disabled:opacity-50"
            disabled={loading}
          >
            {loading ? 'Sending...' : 'Send Message'}
          </motion.button>
          {status && <div className="md:col-span-2 text-sm text-gray-300">{status}</div>}
        </motion.form>
      </div>
    </section>
  )
}
