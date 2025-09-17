import "./globals.css";
import { ReactNode } from "react";

export const metadata = {
  title: "RabbitMQ Chat",
  description: "Real-time messaging with RabbitMQ and WebSocket",
};

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="en">
      <body className="bg-gradient-to-b from-purple-200 via-pink-100 to-blue-200 min-h-screen relative">
        {/* Floating navbar */}
        <nav className="fixed top-4 left-4 right-4 z-50 bg-white/40 backdrop-blur-md shadow-lg rounded-xl px-6 py-4 flex justify-between items-center">
          <div className="text-2xl font-bold text-purple-800">
            RabbitMQ Chat
          </div>
          <div className="space-x-4">
            <button className="px-4 py-2 rounded-md bg-purple-600 text-white hover:bg-purple-700 transition">
              Home
            </button>
            <button className="px-4 py-2 rounded-md bg-purple-600 text-white hover:bg-purple-700 transition">
              About
            </button>
            <button className="px-4 py-2 rounded-md bg-purple-600 text-white hover:bg-purple-700 transition">
              Contact
            </button>
          </div>
        </nav>

        {/* Main content */}
        <main className="pt-32 px-6 md:px-20 lg:px-40">
          {children}

          {/* Example lengthy content */}
          <section className="mt-12 space-y-12">
            {Array.from({ length: 10 }).map((_, idx) => (
              <div
                key={idx}
                className="p-6 bg-white/50 backdrop-blur-md rounded-xl shadow-md hover:scale-105 transition-transform"
              >
                <h2 className="text-xl font-semibold text-purple-700 mb-2">
                  Message Section {idx + 1}
                </h2>
                <p className="text-gray-700">
                  This is an example message content. Lorem ipsum dolor sit
                  amet, consectetur adipiscing elit. Curabitur nec diam vitae
                  purus vestibulum eleifend.
                </p>
              </div>
            ))}
          </section>
        </main>
      </body>
    </html>
  );
}
