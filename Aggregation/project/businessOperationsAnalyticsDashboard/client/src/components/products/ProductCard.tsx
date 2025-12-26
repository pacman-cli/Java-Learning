"use client";

import { Product } from "@/data/mock";
import { cn } from "@/lib/utils";
import { motion } from "framer-motion";
import { Plus } from "lucide-react";

interface ProductCardProps {
  product: Product;
}

export function ProductCard({ product }: ProductCardProps) {
  // Determine status color
  const statusColor =
    product.status === "In Stock" ? "text-emerald-500 bg-emerald-500/10 border-emerald-500/20" :
    product.status === "Low Stock" ? "text-amber-500 bg-amber-500/10 border-amber-500/20" :
    "text-rose-500 bg-rose-500/10 border-rose-500/20";

  return (
    <motion.div
        layout
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        exit={{ opacity: 0, scale: 0.95 }}
        transition={{ duration: 0.3, ease: "easeOut" }}
        whileHover={{ y: -5, scale: 1.03 }}
        className="group glass-panel relative flex flex-col overflow-hidden transition-all hover:shadow-2xl hover:shadow-primary/5"
    >
      {/* Image Container */}
      <div className="relative aspect-[4/3] overflow-hidden bg-muted">
         <div
            className="absolute inset-0 bg-cover bg-center transition-transform duration-500 group-hover:scale-110"
            style={{ backgroundImage: `url(${product.image})` }}
         />
         {/* Overlay */}
         <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-0 transition-opacity duration-300 group-hover:opacity-100" />

         {/* Floating Action */}
         <div className="absolute bottom-4 right-4 translate-y-4 opacity-0 transition-all duration-300 group-hover:translate-y-0 group-hover:opacity-100">
             <button className="flex h-10 w-10 items-center justify-center rounded-full bg-white text-black shadow-lg transition-transform hover:scale-110 active:scale-95">
                 <Plus className="h-5 w-5" />
                 <span className="sr-only">Add to cart</span>
             </button>
         </div>
      </div>

      {/* Content */}
      <div className="flex flex-1 flex-col p-5">
        <div className="flex items-start justify-between gap-4">
            <div>
                <span className="inline-block text-[10px] font-bold uppercase tracking-wider text-muted-foreground/70 mb-1">
                    {product.category}
                </span>
                <h3 className="line-clamp-1 font-semibold leading-tight text-foreground group-hover:text-primary transition-colors">
                    {product.name}
                </h3>
            </div>
            <div className="text-right">
                <p className="font-mono text-lg font-bold">
                    ${product.price.toFixed(2)}
                </p>
            </div>
        </div>

        <div className="mt-auto pt-4 flex items-center justify-between">
            <span className={cn("px-2.5 py-1 rounded-full text-[10px] font-bold uppercase tracking-wide border", statusColor)}>
                {product.status}
            </span>
            <span className="text-xs text-muted-foreground font-medium">
                {product.stock} in stock
            </span>
        </div>
      </div>
    </motion.div>
  );
}
