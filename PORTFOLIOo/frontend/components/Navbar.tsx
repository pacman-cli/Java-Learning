import { ThemeToggle } from './ThemeToggle'
import { motion } from 'framer-motion'

const links = [
  { href: '#about', label: 'About' },
  { href: '#skills', label: 'Skills' },
  { href: '#projects', label: 'Projects' },
  { href: '#education', label: 'Education' },
  { href: '#experience', label: 'Experience' },
  { href: '#contact', label: 'Contact' },
]

export function Navbar() {
  return (
    <nav className="fixed top-0 left-0 right-0 z-50">
      <div className="mx-auto max-w-6xl px-4">
        <div className="mt-4 glass rounded-2xl px-4 py-3 flex items-center justify-between">
          <motion.a
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            href="#top"
            className="font-semibold text-white"
          >
            <span className="text-brand">CSE</span> Portfolio
          </motion.a>
          <div className="hidden md:flex items-center gap-6">
            {links.map((l) => (
              <motion.a
                key={l.href}
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                whileHover={{ y: -2 }}
                href={l.href}
                className="text-sm text-gray-300 hover:text-white transition"
              >
                {l.label}
              </motion.a>
            ))}
          </div>
          <ThemeToggle />
        </div>
      </div>
    </nav>
  )
}
