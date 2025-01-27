"use client";

import React, { useEffect, useState } from "react";
import { useCart } from "../context/CartContext";
import formatCurrency from "../utils/formatCurrency";
import api from "../services/api";
import { useRouter } from "next/navigation";

const CartPage = () => {
  const { cart, dispatch } = useCart();
  const router = useRouter();
  const [isProcessing, setIsProcessing] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!cart.customerId) {
      setError("Customer ID is missing. Please log in again.");
      return;
    }

    const fetchCart = async () => {
      try {
        const response = await api.get(`/carts/${cart.customerId}`);
        dispatch({ type: "SET_CART", payload: response });
      } catch (err) {
        console.error("Failed to fetch cart data:", err);
        setError("Failed to load cart. Please try again.");
      }
    };

    fetchCart();
  }, [cart.customerId, dispatch]);

  const handleRemove = async (id) => {
    try {
      await api.delete(`/carts/${cart.customerId}/remove?productId=${id}`);
      dispatch({
        type: "SET_CART",
        payload: {
          ...cart,
          items: cart.items.filter((item) => item.id !== id),
        },
      });
    } catch (err) {
      console.error("Failed to remove product:", err);
      setError("Failed to remove product. Please try again.");
    }
  };

  const handleQuantityChange = async (id, quantity) => {
    if (quantity > 0) {
      try {
        console.log(`Updating quantity for product ${id} to ${quantity}`);
        // Use query parameters for the request
        const updatedCart = await api.post(
          `/carts/${cart.customerId}/add?productId=${id}&quantity=${quantity}`
        );
        dispatch({ type: "SET_CART", payload: updatedCart });
      } catch (error) {
        console.error("Error updating quantity:", error);
        alert("Failed to update quantity. Please try again.");
      }
    } else {
      alert("Quantity must be greater than zero.");
    }
  };
  

  const handleCheckout = async () => {
    try {
      setIsProcessing(true);
      await api.post(`/carts/${cart.customerId}/checkout`);
      dispatch({ type: "CLEAR_CART" });
      router.push("/orders");
    } catch (err) {
      console.error("Checkout failed:", err);
      setError("Checkout failed. Please try again.");
    } finally {
      setIsProcessing(false);
    }
  };

  if (!cart.customerId) {
    return (
      <div className="text-red-500">Customer ID is missing. Please log in.</div>
    );
  }

  return (
    <div className="container mx-auto text-center py-8">
      <h1 className="text-2xl font-bold mb-4">Shopping Cart</h1>
      {cart.items.length === 0 ? (
        <p>Your cart is empty.</p>
      ) : (
        <>
          <table className="w-full table-auto border-collapse">
            <thead>
              <tr>
                <th className="border p-2 text-left">Product</th>
                <th className="border p-2 text-right">Price</th>
                <th className="border p-2 text-center">Quantity</th>
                <th className="border p-2 text-right">Total</th>
                <th className="border p-2 text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {cart.items.map((item) => (
                <tr key={item.id}>
                  <td className="border p-2">{item.name}</td>
                  <td className="border p-2 text-right">
                    {formatCurrency(item.price)}
                  </td>
                  <td className="border p-2 text-center">
                    <input
                      type="number"
                      className="border w-16 text-center"
                      value={item.quantity}
                      min="1"
                      onChange={(e) =>
                        handleQuantityChange(
                          item.id,
                          parseInt(e.target.value, 10)
                        )
                      }
                    />
                  </td>
                  <td className="border p-2 text-right">
                    {formatCurrency(item.price * item.quantity)}
                  </td>
                  <td className="border p-2 text-center">
                    <button
                      onClick={() => handleRemove(item.id)}
                      className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                    >
                      Remove
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="mt-4 flex justify-between items-center">
            <h2 className="text-xl font-bold">
              Total:{" "}
              {formatCurrency(
                cart.items.reduce(
                  (total, item) => total + item.price * item.quantity,
                  0
                )
              )}
            </h2>
            <button
              onClick={handleCheckout}
              className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
              disabled={isProcessing}
            >
              {isProcessing ? "Processing..." : "Checkout"}
            </button>
          </div>
          {error && <p className="text-red-500 mt-4">{error}</p>}
        </>
      )}
    </div>
  );
};

export default CartPage;
