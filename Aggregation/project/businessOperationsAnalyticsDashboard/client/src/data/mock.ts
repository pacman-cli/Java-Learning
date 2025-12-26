
export interface Product {
  id: string;
  name: string;
  category: string;
  price: number;
  stock: number;
  status: "In Stock" | "Low Stock" | "Out of Stock";
  image: string;
}

export const MOCK_PRODUCTS: Product[] = [
  {
    id: "PROD-001",
    name: "Premium Wireless Headphones",
    category: "Electronics",
    price: 299.99,
    stock: 45,
    status: "In Stock",
    image: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-002",
    name: "Ergonomic Office Chair",
    category: "Furniture",
    price: 350.00,
    stock: 12,
    status: "Low Stock",
    image: "https://images.unsplash.com/photo-1592078615290-033ee584e267?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-003",
    name: "Mechanical Gaming Keyboard",
    category: "Electronics",
    price: 159.50,
    stock: 120,
    status: "In Stock",
    image: "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-004",
    name: "Smart Watch Series 5",
    category: "Wearables",
    price: 399.00,
    stock: 0,
    status: "Out of Stock",
    image: "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-005",
    name: "4K Monitor 27-inch",
    category: "Electronics",
    price: 450.00,
    stock: 25,
    status: "In Stock",
    image: "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-006",
    name: "Minimalist Desk Lamp",
    category: "Home Decor",
    price: 49.99,
    stock: 200,
    status: "In Stock",
    image: "https://images.unsplash.com/photo-1507473888900-52e1ad1459ee?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-007",
    name: "Bluetooth Speaker",
    category: "Audio",
    price: 79.99,
    stock: 8,
    status: "Low Stock",
    image: "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-008",
    name: "Ceramic Coffee Mug Set",
    category: "Home & Kitchen",
    price: 24.99,
    stock: 50,
    status: "In Stock",
    image: "https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-009",
    name: "Leather Laptop Sleeve",
    category: "Accessories",
    price: 55.00,
    stock: 30,
    status: "In Stock",
    image: "https://images.unsplash.com/photo-1603539272373-f9250af6dc16?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-010",
    name: "Fitness Tracker Band",
    category: "Wearables",
    price: 89.95,
    stock: 15,
    status: "In Stock",
    image: "https://images.unsplash.com/photo-1576243345690-8e41f026b537?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-011",
    name: "Wireless Charging Pad",
    category: "Electronics",
    price: 29.99,
    stock: 5,
    status: "Low Stock",
    image: "https://images.unsplash.com/photo-1586953208448-b95a79798f07?w=500&auto=format&fit=crop&q=60"
  },
  {
    id: "PROD-012",
    name: "Noise Cancelling Earbuds",
    category: "Audio",
    price: 199.99,
    stock: 60,
    status: "In Stock",
    image: "https://images.unsplash.com/photo-1610465299993-e6675c9f9efa?w=500&auto=format&fit=crop&q=60"
  }
];
