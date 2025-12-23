import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Microservices Testing Dashboard",
  description: "Frontend for testing Kafka-based microservices backend",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="antialiased bg-gray-50 min-h-screen">
        <nav className="bg-blue-600 text-white p-4">
          <div className="container mx-auto">
            <h1 className="text-xl font-bold">
              Microservices Testing Dashboard
            </h1>
            <p className="text-blue-100 text-sm">
              Test your Kafka-based microservices backend
            </p>
          </div>
        </nav>
        <main className="container mx-auto p-4">{children}</main>
      </body>
    </html>
  );
}
