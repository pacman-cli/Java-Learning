"use client";

import { useDebounce } from "@/hooks/use-debounce"; // We'll need to create this hook if it doesn't exist, or implement debounce internally
import { cn } from "@/lib/utils";
import { Search, X } from "lucide-react";
import * as React from "react";

interface SearchInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  onSearch: (value: string) => void;
  className?: string;
}

export function SearchInput({ onSearch, className, ...props }: SearchInputProps) {
  const [value, setValue] = React.useState("");
  const debouncedValue = useDebounce(value, 300);

  const inputRef = React.useRef<HTMLInputElement>(null);

  React.useEffect(() => {
    onSearch(debouncedValue);
  }, [debouncedValue, onSearch]);

  const handleClear = () => {
    setValue("");
    onSearch("");
    inputRef.current?.focus();
  };

  return (
    <div className={cn("relative group", className)}>
      <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground transition-colors group-focus-within:text-primary" />
      <input
        {...props}
        ref={inputRef}
        value={value}
        onChange={(e) => setValue(e.target.value)}
        className={cn(
          "w-full h-11 rounded-2xl border-0 bg-white/40 pl-10 pr-10 text-sm font-medium text-foreground placeholder:text-muted-foreground/70 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary/20 disabled:cursor-not-allowed disabled:opacity-50 transition-all duration-300 shadow-sm hover:bg-white/60 hover:shadow-md backdrop-blur-md",
          className
        )}
      />
      {value && (
        <button
          onClick={handleClear}
          className="absolute right-3 top-1/2 -translate-y-1/2 h-5 w-5 rounded-full hover:bg-muted flex items-center justify-center text-muted-foreground transition-colors"
        >
          <X className="h-3 w-3" />
          <span className="sr-only">Clear search</span>
        </button>
      )}
    </div>
  );
}


