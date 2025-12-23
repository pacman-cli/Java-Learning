"use client";

import { cn } from "@/lib/utils";
import { X } from "lucide-react";
import * as React from "react";

const SheetContext = React.createContext<{
  open: boolean;
  setOpen: (open: boolean) => void;
}>({ open: false, setOpen: () => {} });

interface SheetProps {
    children: React.ReactNode;
    open?: boolean;
    onOpenChange?: (open: boolean) => void;
}

export const Sheet = ({ children, open: controlledOpen, onOpenChange: controlledOnOpenChange }: SheetProps) => {
  const [uncontrolledOpen, setUncontrolledOpen] = React.useState(false);

  const isControlled = controlledOpen !== undefined;
  const open = isControlled ? controlledOpen : uncontrolledOpen;
  const setOpen = React.useCallback((newOpen: boolean) => {
      if (controlledOnOpenChange) {
          controlledOnOpenChange(newOpen);
      }
      if (!isControlled) {
          setUncontrolledOpen(newOpen);
      }
  }, [controlledOnOpenChange, isControlled]);

  return (
    <SheetContext.Provider value={{ open, setOpen }}>
      {children}
    </SheetContext.Provider>
  );
};

export const SheetTrigger = ({
  children,
  asChild,
}: {
  children: React.ReactNode;
  asChild?: boolean;
}) => {
  const { setOpen } = React.useContext(SheetContext);
  return (
    <div onClick={() => setOpen(true)} className="cursor-pointer">
      {children}
    </div>
  );
};

export const SheetContent = ({
  children,
  side = "left",
  className,
}: {
  children: React.ReactNode;
  side?: "left" | "right";
  className?: string;
}) => {
  const { open, setOpen } = React.useContext(SheetContext);

  if (!open) return null;

  return (
    <>
      <div
        className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm"
        onClick={() => setOpen(false)}
      />
      <div
        className={cn(
          "fixed z-50 flex flex-col bg-background shadow-2xl transition ease-in-out data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:duration-300 data-[state=open]:duration-500 inset-y-0 left-0 h-full w-3/4 border-r sm:max-w-sm",
          className
        )}
      >
        <div
          className="absolute right-4 top-4 rounded-sm opacity-70 ring-offset-background transition-opacity hover:opacity-100 focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:pointer-events-none data-[state=open]:bg-secondary cursor-pointer"
          onClick={() => setOpen(false)}
        >
          <X className="h-4 w-4" />
          <span className="sr-only">Close</span>
        </div>
        {children}
      </div>
    </>
  );
};
