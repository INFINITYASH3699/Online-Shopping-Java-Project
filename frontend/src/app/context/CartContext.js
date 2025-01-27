"use client";

import { createContext, useContext, useReducer, useEffect } from "react";
import api from "../services/api";

const CartContext = createContext();

const initialState = {
  customerId: localStorage.getItem("customerId") || 15, // Replace with dynamic logic if needed
  items: [],
};

const cartReducer = (state, action) => {
  switch (action.type) {
    case "SET_CART":
      const itemsArray = Object.values(action.payload.items || {}).map((item) => ({
        ...item,
        id: parseInt(item.id, 10),
      }));
      return { ...state, ...action.payload, items: itemsArray };

    case "CLEAR_CART":
      return { ...state, items: [] };

    default:
      return state;
  }
};

export const CartProvider = ({ children }) => {
  const [cart, dispatch] = useReducer(cartReducer, initialState);

  useEffect(() => {
    // Fetch the cart on initialization
    const fetchCart = async () => {
      try {
        const response = await api.get(`/carts/${cart.customerId}`);
        dispatch({ type: "SET_CART", payload: response });
      } catch (error) {
        console.error("Failed to fetch cart data:", error);
      }
    };

    if (cart.customerId) fetchCart();
  }, [cart.customerId]);

  return (
    <CartContext.Provider value={{ cart, dispatch }}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => useContext(CartContext);
