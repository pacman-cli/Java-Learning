export function Footer() {
  return (
    <footer className="mt-24 py-10 border-t border-white/10">
      <div className="mx-auto max-w-6xl px-4 text-sm text-gray-400">
        © {new Date().getFullYear()} CSE Portfolio. Built with Next.js & Spring Boot.
      </div>
    </footer>
  )
}
