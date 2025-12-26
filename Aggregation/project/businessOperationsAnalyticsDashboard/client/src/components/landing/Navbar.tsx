"use client";

import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import { motion, useScroll } from "framer-motion";
import Link from "next/link";
import { useEffect, useState } from "react";

export function Navbar() {
  const { scrollY } = useScroll();
  const [isScrolled, setIsScrolled] = useState(false);

  useEffect(() => {
    return scrollY.onChange((latest) => {
      setIsScrolled(latest > 50);
    });
  }, [scrollY]);

  return (
    <motion.header
      initial={{ y: -100 }}
      animate={{ y: 0 }}
      transition={{ duration: 0.5 }}
      className={cn(
        "fixed top-0 left-0 right-0 z-50 flex items-center justify-center p-4 transition-all duration-300",
        isScrolled ? "pt-4" : "pt-6"
      )}
    >
      <nav
        className={cn(
          "flex items-center justify-between px-6 py-3 rounded-full transition-all duration-300 border",
          isScrolled
            ? "bg-background/70 backdrop-blur-xl border-border/50 shadow-lg w-full max-w-5xl"
            : "bg-transparent border-transparent w-full max-w-7xl"
        )}
      >
        {/* Logo */}
        <Link href="/" className="flex items-center gap-2 group">
          <div className="h-8 w-8 rounded-lg bg-gradient-to-tr from-primary to-indigo-600 flex items-center justify-center shadow-lg shadow-indigo-500/20 group-hover:scale-110 transition-transform duration-300">
            <span className="text-white font-bold text-lg">B</span>
          </div>
          <span className={cn("font-bold text-lg tracking-tight transition-opacity duration-300", isScrolled ? "opacity-100" : "opacity-0 sm:opacity-100")}>
            BusinessKPI
          </span>
        </Link>

        {/* Links (Desktop) */}
        <div className="hidden md:flex items-center gap-8 text-sm font-medium text-muted-foreground">
          {["Features", "How it works", "Pricing"].map((item) => (
            <Link
                key={item}
                href={`#${item.toLowerCase().replace(/\s+/g, '-')}`}
                className="hover:text-primary transition-colors relative group"
            >
                {item}
                <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-primary transition-all group-hover:w-full" />
            </Link>
          ))}
        </div>

        {/* Actions */}
        <div className="flex items-center gap-4">
          <Link href="/login">
            <Button variant="ghost" size="sm" className="hidden sm:inline-flex hover:bg-primary/5 hover:text-primary">
                Log in
            </Button>
          </Link>
          <Link href="/register">
            <Button size="sm" className="rounded-full px-5 bg-primary hover:bg-primary/90 shadow-md hover:shadow-lg transition-all hover:scale-105">
              Get Started
            </Button>
          </Link>
        </div>
      </nav>
    </motion.header>
  );
}
