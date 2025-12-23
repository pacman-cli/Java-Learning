"use client";

import { DashboardLayout } from "@/components/layout/DashboardLayout";
import { PageHeader } from "@/components/layout/PageHeader";
import { ProductGrid } from "@/components/products/ProductGrid";
import { MOCK_PRODUCTS } from "@/data/mock";
import { Plus } from "lucide-react";

export default function ProductsPage() {
  return (
    <DashboardLayout>
      <PageHeader
        title="Products"
        description="Manage your product catalog and inventory."
      >
        <button className="inline-flex items-center justify-center gap-2 rounded-lg bg-primary px-4 py-2 text-sm font-medium text-primary-foreground shadow transition-colors hover:bg-primary/90 focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50">
            <Plus className="h-4 w-4" />
            Add Product
        </button>
      </PageHeader>

      <ProductGrid products={MOCK_PRODUCTS} />
    </DashboardLayout>
  );
}
