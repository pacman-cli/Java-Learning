import './globals.css'
import type { ReactNode } from 'react'
import { Providers } from '@/components/Providers'

export const metadata = {
  title: 'CSE Portfolio',
  description: 'Futuristic, interactive portfolio for a CSE student',
}

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className="min-h-screen bg-[#0b0f1a] text-gray-100 gradient-bg">
        <Providers>{children}</Providers>
      </body>
    </html>
  )
}
