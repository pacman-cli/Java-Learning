"use client";

import { Product } from "@/data/mock";
import { cn } from "@/lib/utils";
import { motion } from "framer-motion";
import { MoreHorizontal } from "lucide-react";

interface ProductListItemProps {
  product: Product;
}

export function ProductListItem({ product }: ProductListItemProps) {
    const statusColor =
    product.status === "In Stock" ? "text-emerald-500" :
    product.status === "Low Stock" ? "text-amber-500" :
    "text-rose-500";

  return (
    <motion.div
        layout
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        exit={{ opacity: 0, y: -10 }}
        className="group flex items-center gap-4 rounded-xl border border-border/50 bg-card p-4 shadow-sm transition-all hover:border-primary/20 hover:shadow-md"
    >
        {/* Image */}
        <div className="h-16 w-16 shrink-0 overflow-hidden rounded-lg bg-muted border border-border/50">
             <div
                className="h-full w-full bg-cover bg-center"
                style={{ backgroundImage: `url(${product.image})` }}
             />
        </div>

        {/* Info */}
        <div className="flex-1 grid grid-cols-1 md:grid-cols-4 gap-4 items-center">
            <div className="md:col-span-2">
                 <p className="text-[10px] font-bold uppercase tracking-wider text-muted-foreground/70 mb-0.5">
                    {product.category}
                </p>
                <h3 className="font-semibold text-foreground group-hover:text-primary transition-colors">
                    {product.name}
                </h3>
            </div>

             <div className="hidden md:block">
                 <div className="flex items-center gap-2">
                    <div className={cn("h-2 w-2 rounded-full bg-current", statusColor)} />
                    <span className="text-sm font-medium text-muted-foreground">{product.status}</span>
                 </div>
                 <p className="text-xs text-muted-foreground pl-4">{product.stock} units</p>
             </div>

             <div className="flex items-center justify-between md:justify-end gap-4">
                 <span className="font-mono font-bold text-lg">
                    ${product.price.toFixed(2)}
                 </span>
                 <button className="h-8 w-8 rounded-lg hover:bg-muted flex items-center justify-center text-muted-foreground transition-colors">
                     <MoreHorizontal className="h-4 w-4" />
                 </button>
             </div>
        </div>
    </motion.div>
  );
}
