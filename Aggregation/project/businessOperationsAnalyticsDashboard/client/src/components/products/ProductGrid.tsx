"use client";

import { Product } from "@/data/mock";
import { cn } from "@/lib/utils";
import { AnimatePresence } from "framer-motion";
import { PackageOpen } from "lucide-react";
import { useMemo, useState } from "react";
import { FilterBar } from "./FilterBar";
import { ProductCard } from "./ProductCard";
import { ProductListItem } from "./ProductListItem";

interface ProductGridProps {
    products: Product[];
}

export function ProductGrid({ products }: ProductGridProps) {
    const [view, setView] = useState<"grid" | "list">("grid");
    const [search, setSearch] = useState("");
    const [categoryFilter, setCategoryFilter] = useState("ALL");

    // Get unique categories
    const categories = useMemo(() =>
        ["ALL", ...Array.from(new Set(products.map(p => p.category)))],
    [products]);

    const filteredProducts = useMemo(() => {
        return products.filter(product => {
            const matchesSearch = product.name.toLowerCase().includes(search.toLowerCase());
            const matchesCategory = categoryFilter === "ALL" || product.category === categoryFilter;
            return matchesSearch && matchesCategory;
        });
    }, [products, search, categoryFilter]);

    return (
        <div className="space-y-6">
            <FilterBar
                onSearch={setSearch}
                categories={categories}
                selectedCategory={categoryFilter}
                onCategoryChange={setCategoryFilter}
                view={view}
                onViewChange={setView}
            />

            {/* Results */}
            {filteredProducts.length === 0 ? (
                <div className="flex flex-col items-center justify-center min-h-[400px] border border-dashed border-border/50 rounded-2xl bg-card/20 animate-in fade-in zoom-in-95 duration-500">
                    <div className="h-20 w-20 rounded-full bg-muted/50 flex items-center justify-center mb-4">
                        <PackageOpen className="h-10 w-10 text-muted-foreground/50" />
                    </div>
                    <h3 className="text-lg font-semibold">No products found</h3>
                    <p className="text-sm text-muted-foreground text-center max-w-sm mt-1">
                        We couldn't find any products matching &quot;{search}&quot; in the selected category.
                    </p>
                    <button
                        onClick={() => { setSearch(""); setCategoryFilter("ALL"); }}
                        className="mt-6 text-primary text-sm font-medium hover:underline"
                    >
                        Clear all filters
                    </button>
                </div>
            ) : (
                <div className={cn(
                    "grid gap-6 transition-all",
                    view === "grid"
                        ? "grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4"
                        : "grid-cols-1"
                )}>
                    <AnimatePresence mode="popLayout">
                        {filteredProducts.map((product) => (
                             view === "grid" ? (
                                <ProductCard key={product.id} product={product} />
                             ) : (
                                <ProductListItem key={product.id} product={product} />
                             )
                        ))}
                    </AnimatePresence>
                </div>
            )}
        </div>
    );
}
