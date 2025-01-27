"use client";

import React from "react";
import { useCart } from "../context/CartContext";
import formatCurrency from "../utils/formatCurrency";
import api from "../services/api";

export default function ProductCard({ product }) {
  const { cart, dispatch } = useCart();

  const handleAddToCart = async () => {
    try {
      console.log("Sending API Request:", {
        productId: product.id,
        quantity: 1,
      });
  
      // Make the API call with query parameters
      const updatedCart = await api.post(
        `/carts/${cart.customerId}/add?productId=${product.id}&quantity=1`
      );
  
      // Update the cart context with the backend response
      dispatch({ type: "SET_CART", payload: updatedCart });
    } catch (error) {
      console.error("Failed to add product to cart:", error);
      alert("Failed to add product to cart. Please try again.");
    }
  };
  

  return (
    <div className="bg-white shadow rounded-lg p-4 flex flex-col">
      <h2 className="text-lg font-bold">{product.name}</h2>
      <p className="text-gray-600 mb-2">{formatCurrency(product.price)}</p>
      <button
        onClick={handleAddToCart}
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 mt-auto"
      >
        Add to Cart
      </button>
    </div>
  );
}
