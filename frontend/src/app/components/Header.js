"use client";

import React from "react";
import Link from "next/link";
import { useCart } from "../context/CartContext";
import { FaShoppingCart } from "react-icons/fa";

export default function Header() {
  const { cart } = useCart();

  const getCartItemCount = () =>
    cart.items.reduce((total, item) => total + item.quantity, 0);

  return (
    <header className="bg-blue-500 text-white p-4 shadow">
      <nav className="container mx-auto flex justify-between items-center">
        <div>
          <Link href="/" className="text-xl font-bold">
            Online Shopping
          </Link>
        </div>
        <div className="flex gap-8">
          <Link href="/cart" className="relative">
            <FaShoppingCart size={24} className="" />
            <span className="absolute bottom-4 left-6 bg-red-500 text-white rounded-full text-sm w-5 h-5 flex justify-center items-center">
              {getCartItemCount()}
            </span>
          </Link>
          <Link href="/orders">Orders</Link>
        </div>
      </nav>
    </header>
  );
}
