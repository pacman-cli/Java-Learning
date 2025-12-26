import Link from "next/link";

export function Footer() {
  return (
    <footer className="py-12 border-t border-border/50 bg-muted/10">
      <div className="container mx-auto px-4 flex flex-col md:flex-row justify-between items-center gap-6">
        <div className="flex items-center gap-2">
            <div className="h-6 w-6 rounded-md bg-gradient-to-tr from-primary to-indigo-600 flex items-center justify-center shadow-sm">
                <span className="text-white font-bold text-xs">B</span>
            </div>
            <span className="font-semibold text-sm">BusinessKPI</span>
        </div>

        <div className="flex gap-8 text-sm text-muted-foreground">
            <Link href="#" className="hover:text-foreground">Privacy</Link>
            <Link href="#" className="hover:text-foreground">Terms</Link>
            <Link href="#" className="hover:text-foreground">Contact</Link>
        </div>

        <p className="text-xs text-muted-foreground">
            © 2024 BusinessKPI Inc. All rights reserved.
        </p>
      </div>
    </footer>
  );
}
