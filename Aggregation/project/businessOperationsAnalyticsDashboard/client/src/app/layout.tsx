import type { Metadata } from "next"
import { Inter, Pacifico } from "next/font/google"
import "./globals.css"
import Providers from "./providers"

const inter = Inter({
  subsets: ["latin"],
  variable: "--font-sans",
})

const pacifico = Pacifico({
  subsets: ["latin"],
  weight: "400",
  variable: "--font-script",
})

export const metadata: Metadata = {
  title: "Business Analytics Dashboard",
  description: "Operations Analytics Dashboard",
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang= "en" >
    <body className={ `${inter.variable} ${pacifico.variable} font-sans antialiased` }>
      <Providers>{ children } </Providers>
      </body>
      </html>
  );
}
