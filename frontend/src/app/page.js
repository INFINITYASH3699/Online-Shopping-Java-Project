"use client";

import React from "react";
import ProductList from "./components/ProductList";

export default function Home() {
  return (
    <main className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-6 text-center">Welcome to the Store</h1>
      <ProductList />
    </main>
  );
}
