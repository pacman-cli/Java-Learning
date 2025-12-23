/**
 * Root Layout Component
 * Wraps all pages and provides global configuration
 *
 * @author ZedCode Frontend
 * @version 1.0
 */

import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import { Toaster } from 'react-hot-toast';
import './globals.css';

// ============================================================================
// FONT CONFIGURATION
// ============================================================================

const inter = Inter({
  subsets: ['latin'],
  display: 'swap',
  variable: '--font-inter',
});

// ============================================================================
// METADATA
// ============================================================================

export const metadata: Metadata = {
  title: {
    default: 'User Management System',
    template: '%s | User Management System',
  },
  description: 'A comprehensive user management system built with Next.js and Spring Boot',
  keywords: [
    'user management',
    'admin panel',
    'spring boot',
    'next.js',
    'react',
    'typescript',
  ],
  authors: [
    {
      name: 'ZedCode',
    },
  ],
  creator: 'ZedCode',
  openGraph: {
    type: 'website',
    locale: 'en_US',
    url: 'http://localhost:3000',
    siteName: 'User Management System',
    title: 'User Management System',
    description: 'A comprehensive user management system built with Next.js and Spring Boot',
  },
  robots: {
    index: true,
    follow: true,
  },
  icons: {
    icon: '/favicon.ico',
  },
};

// ============================================================================
// ROOT LAYOUT COMPONENT
// ============================================================================

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" className={inter.variable}>
      <head>
        <meta charSet="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
      </head>
      <body className={inter.className}>
        {/* Main Content */}
        <div className="min-h-screen bg-gray-50">
          {children}
        </div>

        {/* Toast Notifications */}
        <Toaster
          position="top-right"
          reverseOrder={false}
          gutter={8}
          toastOptions={{
            // Default options
            duration: 4000,
            style: {
              background: '#fff',
              color: '#363636',
              padding: '16px',
              borderRadius: '8px',
              boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)',
            },
            // Success toast style
            success: {
              duration: 3000,
              iconTheme: {
                primary: '#10b981',
                secondary: '#fff',
              },
              style: {
                background: '#f0fdf4',
                color: '#166534',
                border: '1px solid #86efac',
              },
            },
            // Error toast style
            error: {
              duration: 5000,
              iconTheme: {
                primary: '#ef4444',
                secondary: '#fff',
              },
              style: {
                background: '#fef2f2',
                color: '#991b1b',
                border: '1px solid #fca5a5',
              },
            },
            // Loading toast style
            loading: {
              iconTheme: {
                primary: '#3b82f6',
                secondary: '#fff',
              },
            },
          }}
        />
      </body>
    </html>
  );
}
