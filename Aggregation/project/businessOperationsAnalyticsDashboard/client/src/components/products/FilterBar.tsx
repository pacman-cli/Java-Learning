"use client";

import { SearchInput } from "@/components/ui/search-input";
import { cn } from "@/lib/utils";
import { Filter, LayoutGrid, List } from "lucide-react";

interface FilterBarProps {
  onSearch: (value: string) => void;
  categories: string[];
  selectedCategory: string;
  onCategoryChange: (category: string) => void;
  view: "grid" | "list";
  onViewChange: (view: "grid" | "list") => void;
  className?: string;
}

export function FilterBar({
  onSearch,
  categories,
  selectedCategory,
  onCategoryChange,
  view,
  onViewChange,
  className,
}: FilterBarProps) {
  return (
    <div
      className={cn(
        "flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between p-4 mb-6 transition-all duration-300 ease-in-out glass-panel",
        className
      )}
    >

      {/* Search - Flexible Width */}
      <div className="w-full sm:max-w-md transition-all duration-300">
        <SearchInput
          placeholder="Search products..."
          onSearch={onSearch}
          className="w-full bg-white/40 hover:bg-white/60 focus-within:bg-white/80 transition-all duration-300 shadow-sm border-0"
        />
      </div>

      {/* Filters & View Toggle */}
      <div className="flex items-center gap-2 sm:gap-4 overflow-x-auto pb-2 sm:pb-0 no-scrollbar">
        {/* Category Filter */}
        <div className="relative min-w-[140px]">
          <select
            value={selectedCategory}
            onChange={(e) => onCategoryChange(e.target.value)}
            className={cn(
                "w-full h-10 appearance-none rounded-xl border-0 pl-9 pr-8 text-sm shadow-sm transition-all cursor-pointer focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary/20 disabled:cursor-not-allowed disabled:opacity-50",
                "bg-white/40 hover:bg-white/60 text-foreground font-medium backdrop-blur-sm"
            )}
          >
            {categories.map((cat) => (
              <option key={cat} value={cat}>
                {cat === "ALL" ? "All Categories" : cat}
              </option>
            ))}
          </select>
          <Filter className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground pointer-events-none" />
        </div>

        {/* View Toggle */}
        <div className={cn(
            "flex items-center rounded-lg p-1 shadow-sm h-10 transition-colors bg-white/40 backdrop-blur-sm"
        )}>
            <button
                onClick={() => onViewChange("grid")}
                className={cn(
                    "flex h-8 w-8 items-center justify-center rounded-md transition-all",
                    view === "grid"
                        ? "bg-white text-primary shadow-sm"
                        : "text-muted-foreground hover:bg-white/20 hover:text-foreground"
                )}
                aria-label="Grid view"
            >
                <LayoutGrid className="h-4 w-4" />
            </button>
            <button
                onClick={() => onViewChange("list")}
                className={cn(
                    "flex h-8 w-8 items-center justify-center rounded-md transition-all",
                    view === "list"
                        ? "bg-white text-primary shadow-sm"
                        : "text-muted-foreground hover:bg-white/20 hover:text-foreground"
                )}
                aria-label="List view"
            >
                <List className="h-4 w-4" />
            </button>
        </div>
      </div>
    </div>
  );
}
